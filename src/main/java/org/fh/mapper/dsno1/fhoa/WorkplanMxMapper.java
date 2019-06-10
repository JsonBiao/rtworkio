package org.fh.mapper.dsno1.fhoa;

import java.util.List;
import org.fh.entity.Page;
import org.fh.entity.PageData;



/** 
 * 说明： 工作计划流程(明细)Mapper
 * 作者：FH Admin 
 * 时间：2019-05-05
 * 官网：www.fhadmin.org
 * @version
 */
public interface WorkplanMxMapper{

	/**新增
	 * @param pd
	 * @throws Exception
	 */
	void save(PageData pd);
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	void delete(PageData pd);
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	void edit(PageData pd);
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistPage(Page page);
	
	/**未完成列表
	 * @param page
	 * @throws Exception
	 */
	List<PageData> datalistNoEndPage(Page page);
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	List<PageData> listAll(PageData pd);
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	PageData findById(PageData pd);
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	void deleteAll(String[] ArrayDATA_IDS);
	
	/**查询明细总数
	 * @param pd
	 * @throws Exception
	 */
	PageData findCount(PageData pd);
	
	PageData findNoEndCount(PageData pd);
	
	void batchUpdate(List<PageData> pageDatas);
	
	
	List<PageData> selectIds(String[] ids);
	
	int insertBatch(List<PageData> pageDatas);
}

