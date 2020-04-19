package com.ego.manage.service;

import java.util.List;

import com.ego.commons.pojo.EasyUITree;

public interface TbItemCatService {
	/**
	 * 根据父菜单id显示所有子菜单
	 * @param pid
	 * @return
	 */
	List<EasyUITree> show(long pid);
}
