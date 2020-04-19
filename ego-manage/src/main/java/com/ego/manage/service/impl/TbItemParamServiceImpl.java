package com.ego.manage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.manage.pojo.TbItemParamChild;
import com.ego.manage.service.TbItemParamService;
import com.ego.pojo.TbItemParam;

@Service
public class TbItemParamServiceImpl implements TbItemParamService {
	@Reference
	private TbItemParamDubboService tbItemParamDubboServiceImpl;
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;

	@Override
	public EasyUIDataGrid show(int page, int rows) {
		EasyUIDataGrid dataGrid = tbItemParamDubboServiceImpl.show(page, rows);
		List<TbItemParam> list = (List<TbItemParam>) dataGrid.getRows();
		List<TbItemParamChild> listchild = new ArrayList<>();
		for (TbItemParam tbItemParam : list) {
			TbItemParamChild child = new TbItemParamChild();
			child.setCreated(tbItemParam.getCreated());
			child.setId(tbItemParam.getId());
			child.setItemCatId(tbItemParam.getItemCatId());
			child.setParamData(tbItemParam.getParamData());
			child.setUpdated(tbItemParam.getUpdated());
			child.setItemCatName(tbItemCatDubboServiceImpl.selById(tbItemParam.getItemCatId()).getName());
			listchild.add(child);
		}
		dataGrid.setRows(listchild);
		return dataGrid;
	}
	
	@Override
	public int delByIds(String ids) throws Exception {
		return tbItemParamDubboServiceImpl.delByIds(ids);
	}

	@Override
	public EgoResult showParam(long catId) {
		EgoResult result = new EgoResult();
		TbItemParam param = tbItemParamDubboServiceImpl.selByCatid(catId);
		if (param != null) {
			result.setStatus(200);
			result.setData(param);
		}
		return result;
	}

	@Override
	public EgoResult insParam(TbItemParam param) {
		Date date = new Date();
		param.setCreated(date);
		param.setUpdated(date);
		EgoResult result = new EgoResult();
		int index = 0;
		index = tbItemParamDubboServiceImpl.insParam(param);
		if (index == 1) {
			result.setStatus(200);
			result.setData(param);
		}
		return result;
	}
}
