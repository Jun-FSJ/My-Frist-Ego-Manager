package com.ego.dubbo.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.dubbo.service.TbItemParamDubboService;
import com.ego.mapper.TbItemParamMapper;
import com.ego.pojo.TbItemParam;
import com.ego.pojo.TbItemParamExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService {
	@Resource
	private TbItemParamMapper tbItemParamMapper;

	@Override
	public EasyUIDataGrid show(int page, int total) {
		PageHelper.startPage(page, total);

		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
		EasyUIDataGrid er = new EasyUIDataGrid();
		PageInfo<TbItemParam> pi = new PageInfo<>(list);
		er.setRows(pi.getList());
		er.setTotal(pi.getTotal());
		return er;
	}

	@Override
	public int delByIds(String ids) throws Exception {
		String[] idStrings = ids.split(",");
		int index = 0;
		for (String id : idStrings) {
			index += tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
		}
		if (index == idStrings.length) {
			return 1;
		} else {
			throw new Exception("删除失败.可能原因:数据已经不 存在");
		}
	}

	@Override
	public TbItemParam selByCatid(long catId) {
		TbItemParamExample example = new TbItemParamExample();
		example.createCriteria().andItemCatIdEqualTo(catId);
		List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
		if (list != null && list.size() > 0) {
			//要求每个类目只能有一个模板
			return list.get(0);
		}
		return null;
	}

	@Override
	public int insParam(TbItemParam param) {
		return tbItemParamMapper.insertSelective(param);
	}
}
