package com.ego.manage.service;

import java.util.List;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryService {
	/**
	 * 查询所有类目并转换为easyui tree的属性要求
	 * @param id
	 * @return
	 */
	List<EasyUITree> showCategory(long id);
	/**
	 * 类目新增
	 * @param cate
	 * @return
	 */
	EgoResult create(TbContentCategory cate);
	/**
	 * 修改类目(重命名)
	 * @param cate
	 * @return
	 */
	EgoResult updCategory(TbContentCategory cate);
	/**
	 * 删除类目
	 * @param cate
	 * @return
	 */
	EgoResult delCategory(TbContentCategory cate);
}
