package com.ego.dubbo.service;

import java.util.List;

import com.ego.pojo.TbContentCategory;

public interface TbContentCategoryDubboService {
	/**
	 * 根据父id查询所有子类目
	 * @param id
	 * @return
	 */
	List<TbContentCategory> selByPid(long id);
	/**
	 * 新增
	 * @param id
	 * @return
	 */
	int insertCategory(TbContentCategory cate);
	/**
	 * 修改isparent通过id
	 * @return
	 */
	int updIsParentById(TbContentCategory cate);
	/**
	 * 根据id查询所有内容
	 * @param id
	 * @return
	 */
	TbContentCategory selById(long id);
}
