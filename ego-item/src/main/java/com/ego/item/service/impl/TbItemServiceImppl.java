package com.ego.item.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.item.service.TbItemService;
import com.ego.jedis.dao.JedisDao;
import com.ego.pojo.TbItem;

@Service
public class TbItemServiceImppl implements TbItemService {
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.item.key}")
	private String itemKey;
	@Override
	public TbItemChild show(long id) {
		String key = itemKey+id;
		if (jedisDaoImpl.exists(key)) {
			String val = jedisDaoImpl.get(key);
			if (val != null && !val.equals("")) {
				return JsonUtils.jsonToPojo(val, TbItemChild.class);
			}
		}
		TbItem item = tbItemDubboServiceImpl.selById(id);
		TbItemChild child = new TbItemChild();
		child.setId(item.getId());
		child.setTitle(item.getTitle());
		child.setPrice(item.getPrice());
		child.setSellPoint(item.getSellPoint());
		child.setImages(item.getImage() != null && !item.equals("") ? item.getImage().split(",") : new String[1]);
		//存到redis数据库中
		jedisDaoImpl.set(key, JsonUtils.objectToJson(child));
		return child;
	}

}
