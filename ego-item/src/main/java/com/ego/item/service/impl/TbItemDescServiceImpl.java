package com.ego.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemDescDubboService;
import com.ego.item.service.TbItemDescService;
import com.ego.jedis.dao.JedisDao;

@Service
public class TbItemDescServiceImpl implements TbItemDescService {
	@Reference
	private TbItemDescDubboService tbItemDescDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.desc.key}")
	private String descKey;
	@Override
	public String showDesc(long itemid) {
		String key = descKey+itemid;
		if (jedisDaoImpl.exists(key)) {
			String val= jedisDaoImpl.get(key);
			if (val != null && !val.equals("")) {
				return val;
			}
		}
		String result = tbItemDescDubboServiceImpl.selByItemid(itemid).getItemDesc();
		jedisDaoImpl.set(key, result);
		return result;
	}

}
