package com.ego.passport.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.taglibs.standard.tag.common.xml.IfTag;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ego.commons.pojo.EgoResult;
import com.ego.passport.service.TbUserService;
import com.ego.passport.service.impl.TbUserServiceImpl;
import com.ego.pojo.TbUser;

@Controller
public class TbUserController {
	@Resource
	private TbUserService tbUserServiceImpl;

	/**
	 * 显示登陆界面
	 * 
	 * @param url
	 * @param model
	 * @return
	 */
	@RequestMapping("/user/showLogin")
	public String showLogin(@RequestHeader("Referer") String url, Model model,String interurl) {
		if (interurl != null && !interurl.equals("")) {
			model.addAttribute("redirect", interurl);
		}else if (url != null && !url.equals("")) {
			model.addAttribute("redirect", url);
		}
		return "login";
	}

	/**
	 * 用户登陆
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("user/login")
	@ResponseBody
	public EgoResult UserLogin(TbUser user, HttpServletRequest request, HttpServletResponse response) {
		return tbUserServiceImpl.UserLogin(user, request, response);
	}

	/**
	 * 通过token获取用户信息
	 * 
	 * @param token
	 * @param callback
	 * @return
	 */
	@RequestMapping("user/token/{token}")
	@ResponseBody
	public Object getUserInfo(@PathVariable String token, String callback) {
		EgoResult result = tbUserServiceImpl.getUserInfoByToken(token);
		// 可选参数callback：如果有此参数表示此方法为jsonp请求，需要支持jsonp。
		//参考前后端接口手册
		if (callback != null && !callback.equals("")) {
			MappingJacksonValue mjv = new MappingJacksonValue(result);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
		return result;
	}

	/**
	 * 退出
	 * 
	 * @return
	 */
	@RequestMapping("user/logout/{token}")
	@ResponseBody
	public Object logout(@PathVariable String token, String callback, HttpServletRequest request,
			HttpServletResponse response) {
		EgoResult result = tbUserServiceImpl.logout(token, request, response);
		// 可选参数callback：如果有此参数表示此方法为jsonp请求，需要支持jsonp。
		if (callback != null && !callback.equals("")) {
			MappingJacksonValue mjv = new MappingJacksonValue(result);
			mjv.setJsonpFunction(callback);
			return mjv;
		}
		return result;
	}
}
