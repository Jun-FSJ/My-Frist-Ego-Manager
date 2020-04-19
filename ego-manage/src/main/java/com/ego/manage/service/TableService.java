package com.ego.manage.service;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbItem;

public interface TableService {
	/**
	 * 显示商品
	 * @param page
	 * @param rows
	 * @return
	 */
	EasyUIDataGrid show(int page, int rows);
	/**
	 * 批量修改商品状态
	 *
	 * @param ids
	 * @param status
	 * @return
	 */
	int update(String ids, byte status);
//	/**
//	 * 商品新增,未事务回滚
//	 * @param item
//	 * @param desc
//	 * @return
//	 */
//	int insert(TbItem item,String desc);
	/**
	 * 商品新增,事务回滚
	 * @param item
	 * @param desc
	 * @return
	 */
	int save(TbItem item,String desc,String itemParams) throws Exception;
}
