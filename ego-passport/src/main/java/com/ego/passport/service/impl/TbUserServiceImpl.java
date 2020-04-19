package com.ego.passport.service.impl;

import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbUserDubboService;
import com.ego.jedis.dao.JedisDao;
import com.ego.passport.service.TbUserService;
import com.ego.pojo.TbUser;

import javassist.expr.NewArray;

@Service
public class TbUserServiceImpl implements TbUserService {
	@Reference
	private TbUserDubboService tbUserDubboServiceImpl;
	@Resource
	private JedisDao jedisDaoImpl;

	@Override
	public EgoResult UserLogin(TbUser user, HttpServletRequest request, HttpServletResponse response) {
		EgoResult result = new EgoResult();
		TbUser user2 = tbUserDubboServiceImpl.selByUser(user);
		if (user2 != null) {
			result.setStatus(200);
			// 当用户登陆成功后把用户信息放入到redis中
			String key = UUID.randomUUID().toString();
			jedisDaoImpl.set(key, JsonUtils.objectToJson(user2));
			jedisDaoImpl.expire(key, 60 * 60 * 24 * 7);
			// 产生Cookie
			CookieUtils.setCookie(request, response, "TT_TOKEN", key, 60 * 60 * 24 * 7);
		} else {
			result.setMsg("用户名或密码错误");
		}
		return result;
	}

	@Override
	public EgoResult getUserInfoByToken(String token) {
		EgoResult result = new EgoResult();
		String json = jedisDaoImpl.get(token);
		if (json != null && !json.equals("")) {
			TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
			//可以把密码清空
			user.setPassword(null);
			result.setStatus(200);
			result.setMsg("OK");
			result.setData(user);
		}else {
			result.setMsg("获取失败");
		}
		return result;
	}

	@Override
	public EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response) {
		jedisDaoImpl.del(token);
		CookieUtils.deleteCookie(request, response, token);
		EgoResult result = new EgoResult();
		result.setStatus(200);
		result.setMsg("OK");
		return result;
	}

}
