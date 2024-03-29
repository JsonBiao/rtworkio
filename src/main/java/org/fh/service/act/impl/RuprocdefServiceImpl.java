package org.fh.service.act.impl;

import java.util.List;

import org.fh.entity.Page;
import org.fh.entity.PageData;
import org.fh.mapper.dsno1.act.RuprocdefMapper;
import org.fh.service.act.RuprocdefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 
 * 说明： 正在运行的流程接口实现类
 * 创建人：FH Q313596790
 * 官网：www.fhadmin.org
 */
@Service(value="ruprocdefServiceImpl")
@Transactional //开启事物
public class RuprocdefServiceImpl implements RuprocdefService {
	
	@Autowired
	private RuprocdefMapper ruprocdefMapper;
	

	/**待办任务 or正在运行任务列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> list(Page page)throws Exception{
		return ruprocdefMapper.datalistPage(page);
	}
	
	/**流程变量列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> varList(PageData pd)throws Exception{
		return ruprocdefMapper.varList(pd);
	}
	
	/**历史任务节点列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hiTaskList(PageData pd)throws Exception{
		return ruprocdefMapper.hiTaskList(pd);
	}
	
	/**已办任务列表列表
	 * @param page
	 * @throws Exception
	 */
	public List<PageData> hitasklist(Page page)throws Exception{
		return ruprocdefMapper.hitaskdatalistPage(page);
	}
	
	/**激活or挂起任务(指定某个任务)
	 * @param pd
	 * @throws Exception
	 */
	public void onoffTask(PageData pd)throws Exception{
		ruprocdefMapper.onoffTask(pd);;
	}
	
	/**激活or挂起任务(指定某个流程的所有任务)
	 * @param pd
	 * @throws Exception
	 */
	public void onoffAllTask(PageData pd)throws Exception{
		ruprocdefMapper.onoffAllTask(pd);;
	}

	public List<PageData> selectByPIId(PageData pd)throws Exception{
		return ruprocdefMapper.selectByPIId(pd);
	}
	
	public PageData selectTaskID(PageData pd)throws Exception{
		return ruprocdefMapper.selectTaskID(pd);
	}

	
	public PageData selectInformation(PageData pd) throws Exception {
		PageData pd1=ruprocdefMapper.selectToName(pd);
		PageData pd2=ruprocdefMapper.selectEndTime(pd);
		PageData pd3=ruprocdefMapper.selectState(pd);
		PageData pd4=ruprocdefMapper.selectOpinpons(pd);
		PageData pdData=new PageData();
		if (pd1!=null) {
			pdData.put("TONAME", pd1.get("NAME"));
		}
		if (pd2!=null) {
			pdData.put("ENDTIME", pd2.get("END_TIME_"));
		}
		if (pd3!=null) {
			pdData.put("STATE", pd3.get("ACT_NAME_"));
		}
		if (pd4!=null) {
			pdData.put("OPINIONS", pd4.get("content"));
		}
		return pdData;
	}
}
