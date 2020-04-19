package com.ego.manage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EasyUIDataGrid;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbContentDubboService;
import com.ego.jedis.dao.JedisDao;
import com.ego.manage.service.TbContentService;
import com.ego.pojo.TbContent;

@Service
public class TbContentServiceImpl implements TbContentService {
	@Reference
	private TbContentDubboService tbContentDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${redis.bigpic.key}")
	private String key;

	@Override
	public EasyUIDataGrid showPageById(long categoryId, int page, int rows) {
		return tbContentDubboServiceImpl.selContentByPage(categoryId, page, rows);
	}

	@Override
	public EgoResult save(TbContent content) {
		Date date = new Date();
		content.setCreated(date);
		content.setUpdated(date);
		int index = tbContentDubboServiceImpl.insertContent(content);
		EgoResult result = new EgoResult();
		if (index > 0) {
			result.setStatus(200);
		}
		//判断redis中是否有缓存数据
		if (jedisDaoImpl.exists(key)) {
			String value = jedisDaoImpl.get(key);
			if(value != null && !value.equals("")) {
				List<HashMap> list = JsonUtils.jsonToList(value, HashMap.class);
				HashMap<String, Object> map = new HashMap<>();
				map.put("srcB", content.getPic2()); 
				map.put("height", 240); 
				map.put("alt", "对不起,加载图片失败"); 
				map.put("width", 670); 
				map.put("src", content.getPic()); 
				map.put("widthB", 550); 
				map.put("href", content.getUrl() ); 
				map.put("heightB", 240);
				//保证前台大广告显示图片数据为6个
				if (list.size() == 6) {
					list.remove(5);
				}
				list.add(0, map);
				jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
			}
		}
		return result;
	}

	@Override
	public EgoResult delete(long ids) {
		EgoResult result = new EgoResult();
		int index = tbContentDubboServiceImpl.delContentById(ids);
		if (index > 0) {
			result.setStatus(200);
		}
		return result;
	}

	@Override
	public EgoResult edit(TbContent content) {
		EgoResult result = new EgoResult();
		Date date = new Date();
//		long id = IDUtils.genItemId();
//		content.setId(id);
		content.setCreated(date);
		content.setUpdated(date);
		int index = tbContentDubboServiceImpl.updContent(content);
		if (index > 0) {
			result.setStatus(200);
		}
		return result;
	}

}
