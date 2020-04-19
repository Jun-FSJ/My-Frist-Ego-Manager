package com.ego.dubbo.service.impl;

import javax.annotation.Resource;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.mapper.TbItemDescMapper;
import com.ego.pojo.TbItemDesc;

public class TbItemDescDubboServiceImpl implements TbItemDescDubboService {
	@Resource
	private TbItemDescMapper tbItemDescMapper;
	@Override
	public int insertDesc(TbItemDesc desc) {
		return tbItemDescMapper.insert(desc);
	}
	@Override
	public TbItemDesc selByItemid(long itemid) {
		return tbItemDescMapper.selectByPrimaryKey(itemid);
	}

}
