package com.ego.order.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.order.pojo.MyPojo;

public interface OrderService {
	/**
	 * 确认订单信息包含的商品
	 * @param id
	 * @param request
	 * @return
	 */
	List<TbItemChild> showOrderCart(List<Long> ids,HttpServletRequest request);
	/**
	 * 创建订单
	 * @param myPojo
	 * @param request
	 * @return
	 */
	EgoResult createOrer(MyPojo myPojo,HttpServletRequest request);
}
