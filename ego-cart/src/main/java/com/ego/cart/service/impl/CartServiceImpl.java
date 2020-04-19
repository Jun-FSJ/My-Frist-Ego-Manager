package com.ego.cart.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.cart.service.CartService;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.jedis.dao.JedisDao;
import com.ego.jedis.dao.impl.JedisDaoImpl;
import com.ego.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {
	@Resource
	private JedisDao jedisDaoImpl;
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;
	@Value("${passport.url}")
	private String passportUrl;
	@Value("${cart.key}")
	private String cartKey;
	@Override
	public void addCart(long id, int num, HttpServletRequest request) {
		//集合中存放所有购物车商品
		List<TbItemChild> list = new ArrayList<>();
		//resid中的key
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String jsonUser = HttpClientUtil.doPost(passportUrl+token);
		EgoResult result = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		String key = cartKey+((LinkedHashMap)result.getData()).get("username");
		
		//如果redis中存在key
		if (jedisDaoImpl.exists(key)) {
			String json = jedisDaoImpl.get(key);
			if (json != null && !json.equals("")) {
				list = JsonUtils.jsonToList(json, TbItemChild.class);
				for (TbItemChild tbItemChild : list) {
					if ((long)tbItemChild.getId() == id) {
						//购物车中存在该商品
						//商品个数加
						tbItemChild.setNum(tbItemChild.getNum()+num);
						//重新添加到redis中
						jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
						return ;
					}
				}
			}
		}
		TbItem item = tbItemDubboServiceImpl.selById(id);
		TbItemChild child = new TbItemChild();
		child.setId(item.getId());
		child.setTitle(item.getTitle());
		child.setImages(item.getImage()==null||item.getImage().equals("")?new String[1]:item.getImage().split(","));
		child.setNum(num);
		child.setPrice(item.getPrice());
		list.add(child);
		jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
	}
	@Override
	public List<TbItemChild> showCat(HttpServletRequest request) {
		//取出redis中的key
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String jsonUser = HttpClientUtil.doPost(passportUrl+token);
		EgoResult result = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		String key = cartKey+((LinkedHashMap)result.getData()).get("username");
		
		String value = jedisDaoImpl.get(key);
		return JsonUtils.jsonToList(value, TbItemChild.class);
	}
	@Override
	public EgoResult update(long id, int num, HttpServletRequest request) {
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String jsonUser = HttpClientUtil.doPost(passportUrl+token);
		EgoResult result = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		String key = cartKey+((LinkedHashMap)result.getData()).get("username");
		
		String json = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
		for (TbItemChild tbItemChild : list) {
			if ((long)tbItemChild.getId() == id) {
				tbItemChild.setNum(num);
			}
		}
		String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		EgoResult result2 = new EgoResult();
		if (ok.equals("OK")) {
			result2.setStatus(200);
		}
		return result2;
	}
	@Override
	public EgoResult delete(long id, HttpServletRequest request) {
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String jsonUser = HttpClientUtil.doPost(passportUrl+token);
		EgoResult result = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		String key = cartKey+((LinkedHashMap)result.getData()).get("username");
		
		String json = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
		TbItemChild child = null;
		for (TbItemChild tbItemChild : list) {
			if (tbItemChild.getId() == id) {
				child = tbItemChild;
			}
		}
		list.remove(child);
		String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
		EgoResult result2 = new EgoResult();
		if (ok.equals("OK")) {
			result2.setStatus(200);
		}
		return result2;
	}

}
