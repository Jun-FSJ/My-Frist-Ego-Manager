package com.ego.order.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ego.commons.pojo.EgoResult;
import com.ego.order.pojo.MyPojo;
import com.ego.order.service.OrderService;

@Controller
public class OrderController {
	@Resource
	private OrderService orderServiceImpl;
	/**
	 * 显示订单确认页面
	 * @param model
	 * @param ids
	 * @param request
	 * @return
	 */
	@RequestMapping("order/order-cart.html")
	public String showCartOrder(Model model,@RequestParam("id") List<Long> ids,HttpServletRequest request) {
		model.addAttribute("cartList",orderServiceImpl.showOrderCart(ids, request));
		return "order-cart";
	}
	/**
	 * 创建订单
	 * @param param
	 * @param request
	 * @return
	 */
	@RequestMapping("order/create.html")
	public String createOrder(MyPojo param,HttpServletRequest request) {
		EgoResult result = orderServiceImpl.createOrer(param, request);
		if (result.getStatus() == 200) {
			return "my-orders";
		}else {
			request.setAttribute("message", "订单创建失败");
		}
		return "error/exception";
	}
}
