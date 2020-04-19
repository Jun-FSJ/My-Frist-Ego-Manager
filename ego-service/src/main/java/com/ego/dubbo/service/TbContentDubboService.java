package com.ego.dubbo.service;

import java.util.List;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.pojo.TbContent;

public interface TbContentDubboService {
	/**
	 * 分页查询
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGrid selContentByPage(long categoryId,int page,int rows);
	/**
	 * 新增
	 * @param tbContent
	 * @return
	 */
	int insertContent(TbContent tbContent);
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	int delContentById(long ids);
	/**
	 * 修改
	 * @param content
	 * @return
	 */
	int updContent(TbContent content);
	/**
	 * 查询出最近的前n个
	 * @param count
	 * @param isSort
	 * @return
	 */
	List<TbContent> selByCount(int count,boolean isSort);
}
