package com.ego.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.jedis.dao.JedisDao;
import com.ego.pojo.TbContent;
import com.ego.portal.service.TbContentService;

@Service
public class TbContentServiceImpl implements TbContentService {
	@Reference
	private TbContentDubboService tbcontentDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.bigpic.key}")
	private String key;
	@Override
	public String ShowBigPic() {
		//先判断在redis中是否存在
		if (jedisDaoImpl.exists(key)) {//存在
			//如果存在取出，取出后判断是否诶null或"";
			String value = jedisDaoImpl.get(key);
			if (value != null && !value.equals("")) {
				return value;
			}
		}
		//2.如果不存在，从mysql中取出,取完后放入redis中
		List<TbContent> list = tbcontentDubboServiceImpl.selByCount(6, true);
		
		List<Map<String, Object>> list2 = new ArrayList<>();
		for (TbContent tbContent : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("srcB", tbContent.getPic2());
			map.put("height", 240);
			map.put("alt", "对不起，加载图片失败");
			map.put("width", 670);
			map.put("src", tbContent.getPic());
			map.put("widthB", 550);
			map.put("href", tbContent.getUrl());
			map.put("heightB", 240);
			list2.add(map);
		}
		String listJson = JsonUtils.objectToJson(list2);
		//将数据放入到redis中
		jedisDaoImpl.set(key, listJson);
		return listJson;
	}

}
