package org.fh.controller.fhoa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.apache.ibatis.scripting.xmltags.VarDeclSqlNode;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.fh.controller.act.AcStartController;
import org.fh.controller.act.rutask.RuTaskController;
import org.fh.controller.base.BaseController;
import org.fh.entity.Page;
import org.fh.util.DateUtil;
import org.fh.util.Jurisdiction;
import org.fh.util.ObjectExcelView;
import org.fh.util.Tools;
import org.fh.entity.PageData;
import org.fh.service.fhoa.WorkplanService;
import org.fh.service.system.UsersService;
import org.fh.service.fhoa.WorkplanMxService;

/** 
 * 说明：工作计划流程
 * 作者：
 * 时间：
 * 官网：www.fhadmin.org
 */
@Controller
@RequestMapping("/workplan")
public class WorkplanController extends AcStartController {
	
	@Autowired
	private WorkplanService workplanService;
	
	@Autowired
	private WorkplanMxService workplanmxService;
	
	@Autowired
	private RuTaskController ruTaskController;
	
	@Autowired
	private UsersService usersService;
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	@RequiresPermissions("workplan:add")
	public String save(Model model) throws Exception{
		Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
		PageData pd = new PageData();
		pd = this.getPageData();
        String ASSIGNEE = pd.getString("ASSIGNEE_2");
        String pdName=usersService.selectNames(ASSIGNEE);
		pd.put("TONAME", pdName);	
		pd.put("WORKPLAN_ID", this.get32UUID());	//主键
		pd.put("NAME", Jurisdiction.getName());
		pd.put("STARTTIME", date);
		workplanService.save(pd);
		if(pd.get("MSG").equals("yes")) {
			String[] ids=pd.getString("DATA_IDS").split(",");
			List<PageData> pageDatas1=workplanmxService.selectIds(ids);
			for(PageData pd1:pageDatas1) {
				pd1.put("WORKPLAN_ID", pd.getString("WORKPLAN_ID"));
				pd1.put("WORKPLANMX_ID",this.get32UUID());
			}
			workplanmxService.insertBatch(pageDatas1);
		}
		pd = workplanService.findById(pd);	//根据ID读取
		try {
			/** 工作流的操作 **/
			Map<String,Object> map = new LinkedHashMap<String, Object>();
			map.put("提交人员", Jurisdiction.getName());			//当前用户的姓名
//			map.put("接收人员", dataUser.get("NAME"));
			map.put("开始时间", createdate);
//			map.put("工作计划", "");
//			map.put("结束时间", pd.getString("ENDTIME"));
//			map.put("截止时间", pd.getString("DEADLINE"));
//			map.put("协同进度", pd.getString("STATE"));
//			map.put("执行人意见", pd.getString("OPINIONS"));
			map.put("USERNAME", Jurisdiction.getUsername());			//指派代理人为当前用户
			String procInstId=startProcessInstanceByKeyHasVariables("key_work_plan",map);	//启动流程实例(协同单流程)通过KEY
			pd.put("PROC_INST_ID_", procInstId);
			workplanService.edit(pd);									//记录存入数据库
			ruTaskController.submit(procInstId, ASSIGNEE);
			model.addAttribute("ASSIGNEE_",ASSIGNEE);	//用于给待办人发送新任务消息
//			model.addAttribute("msg","success");
			model.addAttribute("msg", "edit");
			model.addAttribute("isEdit", "yes");
			model.addAttribute("pd", pd);
		} catch (Exception e) {
			model.addAttribute("errer","errer");
			model.addAttribute("msgContent","请联系管理员部署相应业务流程!");
		}
		return "fhoa/workplan/workplan_edit";
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	@RequiresPermissions("workplan:del")
	public Object delete() throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		try{
			if(Integer.parseInt(workplanmxService.findCount(pd).get("zs").toString()) > 0){
				errInfo = "error";
			}else{
				workplanService.delete(pd);
			}
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
	}
	
	/**修改
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/edit")
	@RequiresPermissions("workplan:edit")
	public String edit(Model model) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		workplanService.edit(pd);
		model.addAttribute("msg","success");
		return "transferPage";
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("workplan:list")
	public String list(Page page, Model model) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		if(!Jurisdiction.getName().equals("系统管理员")) {
			pd.put("NAME", Jurisdiction.getName());
		}
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = workplanService.list(page);	//列出Workplan列表
		model.addAttribute("varList", varList);
		model.addAttribute("pd", pd);
		return "fhoa/workplan/workplan_list";
	}
	
	
	
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	@RequiresPermissions("workplan:add")
	public String goAdd(Page page,Model model)throws Exception{
		PageData pd = new PageData();
		pd.put("NAME", Jurisdiction.getName());
		page.setPd(pd);
		List<PageData>	varList = workplanmxService.listNoEnd(page);	//列出未完成完成WorkplanMx列表
		model.addAttribute("varList", varList);
		model.addAttribute("msg", "save");
		model.addAttribute("pd", pd);
		return "fhoa/workplan/workplan_edit";
	}
	
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goifAdd")
	@RequiresPermissions("workplan:add")
	public String goifAdd(Page page,Model model)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = "";						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = workplanmxService.listNoEnd(page);	//列出未完成完成WorkplanMx列表
	    if (varList.size()>0) {
			model.addAttribute("varList", varList);
			model.addAttribute("pd", pd);
			return "fhoa/workplan/workplan_view";
		} else {
			model.addAttribute("msg", "save");
			model.addAttribute("pd", pd);
			return "fhoa/workplan/workplan_edit";
        }
	}
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("workplan:edit")
	public String goEdit(Model model)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = workplanService.findById(pd);	//根据ID读取
		model.addAttribute("msg", "edit");
		model.addAttribute("pd", pd);
		return "fhoa/workplan/workplan_edit";
	}	
	
	 /**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/excel")
	@RequiresPermissions("toExcel")
	public ModelAndView exportExcel() throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("提交人");	//1
		titles.add("接收人");	//2
		titles.add("抄送人");	//3
		titles.add("工作内容");	//4
		titles.add("开始时间");	//5
		titles.add("结束时间");	//6
		titles.add("流程ID");	//7
		titles.add("流程进度");	//8
		dataMap.put("titles", titles);
		List<PageData> varOList = workplanService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("NAME"));	    //1
			vpd.put("var2", varOList.get(i).getString("TONAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("COPYNAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("CONTENET"));	    //4
			vpd.put("var5", varOList.get(i).getString("STARTTIME"));	    //5
			vpd.put("var6", varOList.get(i).getString("ENDTIME"));	    //6
			vpd.put("var7", varOList.get(i).getString("PROC_INST_ID_"));	    //7
			vpd.put("var8", varOList.get(i).getString("STATE"));	    //8
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/report")
	@RequiresPermissions("workplan:report")
	public String report(Page page, Model model) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if (Tools.isEmpty(pd.getString("STARTCOMMITTIME"))) {
			String STARTCOMMITTIME=DateUtil.getBeforeDayDate("6");
			String ENDTCOMMITIME=DateUtil.getDay();
			pd.put("STARTCOMMITTIME", STARTCOMMITTIME);
			pd.put("ENDTCOMMITIME", ENDTCOMMITIME);
		}
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData> varList = workplanService.listReportPage(page);	//列出Workplan列表
		model.addAttribute("varList", varList);
		model.addAttribute("pd", pd);
		return "fhoa/workplan/workplan_report";
	}
	
	
	/**导出到excel
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/reportExcel")
	@RequiresPermissions("toExcel")
	public ModelAndView reportExcel(Page page) throws Exception{
		ModelAndView mv = new ModelAndView();
		PageData pd = new PageData();
		pd = this.getPageData();
		Map<String,Object> dataMap = new HashMap<String,Object>();
		List<String> titles = new ArrayList<String>();
		titles.add("部门");	//1
		titles.add("提交人");	//2
		titles.add("开始时间");	//3
		titles.add("是否提交");	//4
		titles.add("平均分");	//5
		dataMap.put("titles", titles);
		page.setPd(pd);
		List<PageData> varOList = workplanService.listReportPage(page);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("DEPARTMENT"));	    //1
			vpd.put("var2", varOList.get(i).getString("zname"));	    //2
			vpd.put("var3", varOList.get(i).get("STARTTIME"));	    //3
			vpd.put("var4", varOList.get(i).get("submit").equals(0)?"否":"是");    //4
			vpd.put("var5", varOList.get(i).get("SCORE")==null?"未评分":varOList.get(i).get("SCORE").toString());	    //5
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
}
