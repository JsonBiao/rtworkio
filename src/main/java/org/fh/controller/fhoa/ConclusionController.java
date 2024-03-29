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
import org.fh.service.act.RuprocdefService;
import org.fh.service.fhoa.ConclusionService;
import org.fh.service.system.UsersService;

/** 
 * 说明：工作总结流程
 * 作者：FH
 * 时间：2019-04-30
 * 官网：
 */
@Controller
@RequestMapping("/conclusion")
public class ConclusionController extends AcStartController {
	
	@Autowired
	private ConclusionService conclusionService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private RuprocdefService ruprocdefService;
	
	@Autowired
	private RuTaskController ruTaskController;
	
	/**保存
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/save")
	@RequiresPermissions("conclusion:add")
	public String save(Model model) throws Exception{
		Date date = new Date();
		PageData pd = new PageData();
		pd = this.getPageData();
        String ASSIGNEE = pd.getString("ASSIGNEE_2");
        String pdName=usersService.selectNames(ASSIGNEE);
		pd.put("CONCLUSION_ID", this.get32UUID());	//主键
		pd.put("NAME", Jurisdiction.getName());
		pd.put("TONAME", pdName);	
		pd.put("STARTTIME", date);	
		conclusionService.save(pd);
        pd = conclusionService.findById(pd);	//根据ID读取
		PageData pdu = new PageData();
		pdu.put("USER_ID", pd.getString("TONAME"));
		PageData dataUser=usersService.findById(pdu);
		
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取String类型的时间
        String createdate = sdf.format(date);
		try {
			/** 工作流的操作 **/
			Map<String,Object> map = new LinkedHashMap<String, Object>();
			map.put("汇报人员", Jurisdiction.getName());			//当前用户的姓名
//			map.put("工作总结", pd.getString("CONTENT"));
			map.put("开始时间", createdate);
			map.put("USERNAME", Jurisdiction.getUsername());			//指派代理人为当前用户
			String procInstId=startProcessInstanceByKeyHasVariables("key_work_conclusion",map);	//启动流程实例(工作总结流程)通过KEY
			pd.put("PROC_INST_ID_", procInstId);
			conclusionService.edit(pd);							//记录存入数据库
			
			ruTaskController.submit(procInstId, ASSIGNEE);
			model.addAttribute("ASSIGNEE_",ASSIGNEE);	//用于给待办人发送新任务消息
			model.addAttribute("msg","success");
//			model.addAttribute("msg", "edit");
			model.addAttribute("pd", pd);
		} catch (Exception e) {
			model.addAttribute("errer","errer");
			model.addAttribute("msgContent","请联系管理员部署相应业务流程!");
		}
		return "transferPage";
	}
	
	/**删除
	 * @param out
	 * @throws Exception
	 */
	@RequestMapping(value="/delete")
	@ResponseBody
	@RequiresPermissions("conclusion:del")
	public Object delete(){
		Map<String,String> map = new HashMap<String,String>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		try{
			conclusionService.delete(pd);
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
	@RequiresPermissions("conclusion:edit")
	public String edit(Model model) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		conclusionService.edit(pd);
		model.addAttribute("msg","success");
		return "transferPage";
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/list")
	@RequiresPermissions("conclusion:list")
	public String list(Page page, Model model) throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		if(!Jurisdiction.getName().equals("系统管理员")) {
			pd.put("NAME", Jurisdiction.getName());
		}
		String KEYWORDS = pd.getString("KEYWORDS");						//关键词检索条件
		if(Tools.notEmpty(KEYWORDS))pd.put("KEYWORDS", KEYWORDS.trim());
		page.setPd(pd);
		List<PageData>	varList = conclusionService.list(page);	//列出Conclusion列表
		for(int i=0;i<varList.size();i++) {
			PageData pdx = new PageData();
			pdx.put("PROC_INST_ID_", varList.get(i).get("PROC_INST_ID_"));
			PageData pdPageData=ruprocdefService.selectInformation(pdx);
			if (pdPageData.get("ENDTIME")!=null) {
				varList.get(i).put("isEnd", "yes");
			}else {
				varList.get(i).put("isEnd", "no");
			}
		}
		model.addAttribute("varList", varList);
		model.addAttribute("pd", pd);
		return "fhoa/conclusion/conclusion_list";
	}
	
	/**去新增页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goAdd")
	@RequiresPermissions("conclusion:add")
	public String goAdd(Model model)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		model.addAttribute("msg", "save");
		model.addAttribute("pd", pd);
		return "fhoa/conclusion/conclusion_edit";
	}	
	
	 /**去修改页面
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/goEdit")
	@RequiresPermissions("conclusion:edit")
	public String goEdit(Model model)throws Exception{
		PageData pd = new PageData();
		pd = this.getPageData();
		pd = conclusionService.findById(pd);	//根据ID读取
		model.addAttribute("msg", "edit");
		model.addAttribute("pd", pd);
		return "fhoa/conclusion/conclusion_edit";
	}	
	
	 /**批量删除
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value="/deleteAll")
	@ResponseBody
	@RequiresPermissions("conclusion:del")
	public Object deleteAll(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();		
		pd = this.getPageData();
		String errInfo = "success";
		String DATA_IDS = pd.getString("DATA_IDS");
		try{
			if(Tools.notEmpty(DATA_IDS)){
				String ArrayDATA_IDS[] = DATA_IDS.split(",");
				conclusionService.deleteAll(ArrayDATA_IDS);
				errInfo = "success";
			}else{
				errInfo = "error";
			}
		} catch(Exception e){
			errInfo = "error";
		}
		map.put("result", errInfo);				//返回结果
		return map;
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
		titles.add("汇报时间");	//1
		titles.add("汇报人");	//2
		titles.add("接收人");	//3
		titles.add("抄送人");	//4
		titles.add("内容");	//5
		titles.add("接收人评分");	//6
		titles.add("结束时间");	//7
		dataMap.put("titles", titles);
		List<PageData> varOList = conclusionService.listAll(pd);
		List<PageData> varList = new ArrayList<PageData>();
		for(int i=0;i<varOList.size();i++){
			PageData vpd = new PageData();
			vpd.put("var1", varOList.get(i).getString("STARTTIME"));	    //1
			vpd.put("var2", varOList.get(i).getString("NAME"));	    //2
			vpd.put("var3", varOList.get(i).getString("TONAME"));	    //3
			vpd.put("var4", varOList.get(i).getString("COPE_NAME"));	    //4
			vpd.put("var5", varOList.get(i).getString("CONTENT"));	    //5
			vpd.put("var6", varOList.get(i).getString("SCORE"));	    //6
			vpd.put("var7", varOList.get(i).getString("ENDTIME"));	    //7
			varList.add(vpd);
		}
		dataMap.put("varList", varList);
		ObjectExcelView erv = new ObjectExcelView();
		mv = new ModelAndView(erv,dataMap);
		return mv;
	}
	
	
	@RequestMapping(value="/contentDetails")
	public String selectByPIId(Model model)throws Exception{
        PageData pd = new PageData();
		pd = this.getPageData();
		pd.put("key", "conclusion");
		List<PageData> pageDatas=ruprocdefService.selectByPIId(pd);
		model.addAttribute("pd", pd);
		model.addAttribute("contents", pageDatas.get(0).getString("CONTENT"));
		model.addAttribute("TYPE", pageDatas.get(0).getString("TYPE"));
        return "act/rutask/handle_content_details";
	}
	
	
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@RequestMapping(value="/report")
	@RequiresPermissions("conclusion:report")
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
		List<PageData> varList = conclusionService.listReportPage(page);	//列出Workplan列表
		model.addAttribute("varList", varList);
		model.addAttribute("pd", pd);
		return "fhoa/conclusion/conclusion_report";
	}
}
