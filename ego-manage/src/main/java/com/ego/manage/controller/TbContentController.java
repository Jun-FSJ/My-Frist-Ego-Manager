package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbContentService;
import com.ego.pojo.TbContent;

@Controller
public class TbContentController {
	@Resource
	private TbContentService tbContentServiceImpl;
	/**
	 * 显示内容信息
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("content/query/list")
	@ResponseBody
	public EasyUIDataGrid showPage(long categoryId,int page,int rows) {
		return tbContentServiceImpl.showPageById(categoryId, page, rows);
	}
	/**
	 * 新增内容
	 * @param content
	 * @return
	 */
	@RequestMapping("content/save")
	@ResponseBody
	public EgoResult save(TbContent content) {
		return tbContentServiceImpl.save(content);
	}
	@RequestMapping("content/delete")
	@ResponseBody
	public EgoResult delete(long ids) {
		return tbContentServiceImpl.delete(ids);
	}
	@RequestMapping("rest/content/edit")
	@ResponseBody
	public EgoResult edit(TbContent content) {
		return tbContentServiceImpl.edit(content);
	}
}
