package com.ego.manage.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.manage.service.TableService;
import com.ego.pojo.TbItem;

@Controller
public class TableController {
	@Resource
	private TableService tableServiceImpl;
	/**
	 * 分页显示商品
	 */
	@RequestMapping("item/list")
	@ResponseBody
	public EasyUIDataGrid show(int page,int rows) {
		return tableServiceImpl.show(page, rows);
	}
	/**
	 * 显示商品修改
	 * @return
	 */
	@RequestMapping("rest/page/item-edit")
	public String showItemEdit() {
		return "item-edit";
	}
	/**
	 * 商品删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/delete")
	@ResponseBody
	public EgoResult delete(String ids) {
		EgoResult result = new EgoResult();
		int index = tableServiceImpl.update(ids, (byte)3);//status为3表示删除
		if (index == 1) {
			result.setStatus(200);//状态码为200，表示操作成功
		}
		return result;
	}
	/**
	 * 商品下架
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/instock")
	@ResponseBody
	public EgoResult instock(String ids) {
		EgoResult result = new EgoResult();
		int index = tableServiceImpl.update(ids, (byte)2);//status为2表示下架
		if (index == 1) {
			result.setStatus(200);//状态码为200，表示操作成功
		}
		return result;
	}
	/**
	 * 商品上架
	 * @param ids
	 * @return
	 */
	@RequestMapping("rest/item/reshelf")
	@ResponseBody
	public EgoResult reshelf(String ids) {
		EgoResult result = new EgoResult();
		int index = tableServiceImpl.update(ids, (byte)1);//status为1表示上架
		if (index == 1) {
			result.setStatus(200);//状态码为200，表示操作成功
		}
		return result;
	}
//	不考虑事务回滚的商品新增
//	@RequestMapping("item/save")
//	@ResponseBody
//	public EgoResult insert1(TbItem item,String desc) {
//		EgoResult er = new EgoResult();
//		int index = tableServiceImpl.insert(item, desc);
//		if (index == 1) {
//			er.setStatus(200);
//		}
//		return er;
//	}
	/**
	 * 商品新增，考虑事务回滚
	 * @param item
	 * @param desc
	 * @return
	 */
	@RequestMapping("item/save") 
	@ResponseBody
	public EgoResult insert(TbItem item,String desc,String itemParams) {
		EgoResult er = new EgoResult();
		int index;
		try {
			index = tableServiceImpl.save(item, desc,itemParams);
			if (index == 1) {
				er.setStatus(200);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			er.setData(e.getMessage());
		}
		return er;
	}
}
