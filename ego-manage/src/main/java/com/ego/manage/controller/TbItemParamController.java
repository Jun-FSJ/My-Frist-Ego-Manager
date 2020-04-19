package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;

@Controller
public class TbItemParamController {
	@Resource
	private TbItemParamService tbItemParamServiceImpl;

	@RequestMapping("item/param/list")
	@ResponseBody
	public EasyUIDataGrid showPage(int page, int rows) {
		return tbItemParamServiceImpl.show(page, rows);
	}

	/*** 批量删除规格参数 * @param ids * @return */
	@RequestMapping("item/param/delete")
	@ResponseBody
	public EgoResult delByIds(String ids) {
		EgoResult result = new EgoResult();
		try {
			int index = tbItemParamServiceImpl.delByIds(ids);
			if (index == 1) {
				result.setStatus(200);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setData(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("item/param/query/itemcatid/{catId}")
	@ResponseBody
	public EgoResult show(@PathVariable long catId) {
		return tbItemParamServiceImpl.showParam(catId);
	}
	@RequestMapping("item/param/save/{catId}") 
	@ResponseBody
	public EgoResult insParam(TbItemParam param,@PathVariable long catId) {
		param.setItemCatId(catId);
		return tbItemParamServiceImpl.insParam(param);
	}
}
