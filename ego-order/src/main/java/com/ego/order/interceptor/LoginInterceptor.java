package com.ego.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.JsonUtils;
import com.ego.jedis.dao.JedisDao;

//购物车拦截器
public class LoginInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//判断有没有登录，登录了并且登录成功，就return true，放行
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		if (token != null && !token.equals("")) {
			String json = HttpClientUtil.doPost("http://localhost:8084/user/token/"+token);
			EgoResult egoResult = JsonUtils.jsonToPojo(json, EgoResult.class);
			if (egoResult.getStatus() == 200) {
				return true;
			}
		}
		//如果没有登录，则重定向到登录页面
		String num = request.getParameter("num");
		if (num != null && !num.equals("")) {
			response.sendRedirect("http://localhost:8084/user/showLogin?interurl="+request.getRequestURL()+"%3Fnum=" +num);
		}else {
			response.sendRedirect("http://localhost:8084/user/showLogin");
		}
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
