package com.ego.manage.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUITree;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentCategoryService;
import com.ego.pojo.TbContentCategory;

@Controller
public class TbContentCategoryController {
	@Resource
	private TbContentCategoryService tbContentCategoryServiceImpl;
	@RequestMapping("content/category/list")
	@ResponseBody
	public List<EasyUITree> showCategory(@RequestParam(defaultValue="0") long id){//默认id为0，显示最大的父菜单，再一级一级的调用这个控制器，得到一级一级的子类目
		return tbContentCategoryServiceImpl.showCategory(id);
	}
	/**
	 * 新增内容类目
	 * @param cate
	 * @return
	 */
	@RequestMapping("content/category/create") 
	@ResponseBody
	public EgoResult create(TbContentCategory cate) {
		return tbContentCategoryServiceImpl.create(cate);
	}
	/**
	 * 重命名
	 * @param cate
	 * @return
	 */
	@RequestMapping("content/category/update")
	@ResponseBody
	public EgoResult update(TbContentCategory cate) {
		return tbContentCategoryServiceImpl.updCategory(cate);
	}
	/**
	 * 删除
	 * @return
	 */
	@RequestMapping("content/category/delete") 
	@ResponseBody
	public EgoResult delete(TbContentCategory cate) {
		return tbContentCategoryServiceImpl.delCategory(cate);
	}
}
