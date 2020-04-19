package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContent;

public interface TbContentService {
	/**
	 * 分页显示内容信息
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGrid showPageById(long categoryId,int page,int rows);
	/**
	 * 内容新增
	 * @param content
	 * @return
	 */
	EgoResult save(TbContent content);
	
	EgoResult delete(long ids);
	
	EgoResult edit(TbContent content);
}
