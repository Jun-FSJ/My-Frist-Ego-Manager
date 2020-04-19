package com.ego.passport.service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ego.commons.pojo.EgoResult;
import com.ego.pojo.TbUser;

public interface TbUserService {
	/**
	 * 用户登陆
	 * @param user
	 * @return
	 */
	EgoResult UserLogin(TbUser user,HttpServletRequest request,HttpServletResponse response);
	/**
	 * 根据token查询用户信息
	 * @param token
	 * @return
	 */
	EgoResult getUserInfoByToken(String token);
	
	/**
	 * 退出
	 * @param token
	 * @param request
	 * @param response
	 * @return
	 */
	EgoResult logout(String token,HttpServletRequest request,HttpServletResponse response);
}
