package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale.Category;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.dubbo.service.TbContentCategoryDubboService;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContent;
import com.ego.pojo.TbContentCategory;

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {
	@Reference
	private TbContentCategoryDubboService tbContentCategoryDubboServiceImpl;
	@Override
	public List<EasyUITree> showCategory(long id) {
		List<EasyUITree> listTree = new ArrayList<>();
		
		List<TbContentCategory> listCategory = tbContentCategoryDubboServiceImpl.selByPid(id);
		for (TbContentCategory cate : listCategory) {
			EasyUITree tree = new EasyUITree();
			tree.setId(cate.getId());
			tree.setText(cate.getName());
			tree.setState(cate.getIsParent()?"closed":"open");
			listTree.add(tree);
		}
		return listTree;
	}
	@Override
	public EgoResult create(TbContentCategory cate) {
		EgoResult result = new EgoResult();
		//判断当前节点名称是否已经存在
		List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByPid(cate.getParentId());//找出当前节点的父节点的所有子节点对象
		for (TbContentCategory child : list) {//把所有子节点遍历
			if (child.getName().equals(cate.getName())) {
				result.setData("该分类名已经存在");
				return result;
			}
		}
		Date date = new Date();
		cate.setCreated(date);
		long id = IDUtils.genItemId();
		cate.setId(id);
		cate.setIsParent(false);
		cate.setSortOrder(1);
		cate.setStatus(1);
		cate.setUpdated(date);
		int index = tbContentCategoryDubboServiceImpl.insertCategory(cate);
		if (index > 0) {
			//新增成功后，要改变当前节点的父节点的isparent属性为true
			TbContentCategory parent = new TbContentCategory();
			parent.setId(cate.getParentId());
			parent.setIsParent(true);
			tbContentCategoryDubboServiceImpl.updIsParentById(parent);
		}
		result.setStatus(200);
		Map<String, Long> map = new HashMap<>();
		map.put("id", id);
		result.setData(map);
		return result;
	}
	@Override
	public EgoResult updCategory(TbContentCategory cate) {
		EgoResult result = new EgoResult();
		//找到当前节点的所有信息
		TbContentCategory tbContentCategory = tbContentCategoryDubboServiceImpl.selById(cate.getId());
		//找到该节点的父节点的所有节点。看看重命名的name和其他子节点的name有没有冲突。
		List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByPid(tbContentCategory.getParentId());
		for (TbContentCategory category : list) {
			if (category.getName().equals(cate.getName())) {
				return result;
			}
		}
		int index = tbContentCategoryDubboServiceImpl.updIsParentById(cate);
		if (index > 0 ) {
			result.setStatus(200);
		}
		return result;
	}
	@Override
	public EgoResult delCategory(TbContentCategory cate) {
		EgoResult result = new EgoResult();
		cate.setStatus(0);
		int index = tbContentCategoryDubboServiceImpl.updIsParentById(cate);
		if (index > 0) {
			TbContentCategory curr = tbContentCategoryDubboServiceImpl.selById(cate.getId());
			List<TbContentCategory> tbContentCategories = tbContentCategoryDubboServiceImpl.selByPid(curr.getParentId());
			if (tbContentCategories == null || tbContentCategories.size() == 0) {
				TbContentCategory parent = new TbContentCategory();
				parent.setId(curr.getParentId());
				parent.setIsParent(false);
				int index2 = tbContentCategoryDubboServiceImpl.updIsParentById(parent);
				if (index2 > 0) {
					result.setStatus(200);
				}
			}else {
				result.setStatus(200);
			}
		}
		return result;
	}
}
