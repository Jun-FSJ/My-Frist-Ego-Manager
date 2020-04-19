package com.ego.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.commons.pojo.EgoResult;
import com.ego.commons.pojo.TbItemChild;
import com.ego.commons.utils.CookieUtils;
import com.ego.commons.utils.HttpClientUtil;
import com.ego.commons.utils.IDUtils;
import com.ego.commons.utils.JsonUtils;
import com.ego.dubbo.service.TbItemDubboService;
import com.ego.dubbo.service.TbOrderDubboService;
import com.ego.jedis.dao.JedisDao;
import com.ego.order.pojo.MyPojo;
import com.ego.order.service.OrderService;
import com.ego.pojo.TbItem;
import com.ego.pojo.TbOrder;
import com.ego.pojo.TbOrderItem;
import com.ego.pojo.TbOrderShipping;

@Service
public class OrderServiceImpl implements OrderService {
	@Resource
	private JedisDao jedisDaoImpl;
	@Value("${passport.url}")
	private String passportUrl;
	@Value("${cart.key}")
	private String cartKey;
	@Reference
	private TbOrderDubboService tbOrderrDubboServiceImpl;
	@Reference
	private TbItemDubboService tbItemDubboServiceImpl;

	@Override
	public List<TbItemChild> showOrderCart(List<Long> ids, HttpServletRequest request) {
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String jsonUser = HttpClientUtil.doPost(passportUrl + token);
		EgoResult result = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		String key = cartKey + ((LinkedHashMap) result.getData()).get("username");

		String value = jedisDaoImpl.get(key);
		List<TbItemChild> list = JsonUtils.jsonToList(value, TbItemChild.class);
		List<TbItemChild> listNew = new ArrayList<>();
		for (TbItemChild child : list) {
			for (Long id : ids) {
				if ((long) child.getId() == (long) id) {
					// 判断购买力是否大于库存
					TbItem item = tbItemDubboServiceImpl.selById(id);
					if (item.getNum() >= child.getNum()) {
						child.setEnough(true);
					} else {
						child.setEnough(false);
					}
					listNew.add(child);
				}
			}
		}
		return listNew;
	}

	@Override
	public EgoResult createOrer(MyPojo param, HttpServletRequest request) {
		// 订单表数据
		TbOrder order = new TbOrder();
		order.setPayment(param.getPayment());
		order.setPaymentType(param.getPaymentType());
		long id = IDUtils.genItemId();
		order.setOrderId(id + "");
		Date date = new Date();
		order.setCreateTime(date);
		order.setUpdateTime(date);
		String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
		String jsonUser = HttpClientUtil.doPost(passportUrl+token);
		EgoResult resul = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
		Map user = (LinkedHashMap)resul.getData();
		order.setUserId(Long.parseLong(user.get("id").toString()));
		order.setBuyerNick(user.get("username").toString());
		order.setBuyerRate(0);
		//订单-商品表
		List<TbOrderItem> list = param.getOrderItems();
		for (TbOrderItem item : list) {
			item.setId(IDUtils.genImageName()+"");
			item.setOrderId(id+"");
		}
		//收货人信息表
		TbOrderShipping shipping = new TbOrderShipping();
		shipping.setOrderId(id+"");
		shipping.setCreated(date);
		shipping.setUpdated(date);
		EgoResult result = new EgoResult();
		try {
			int index = tbOrderrDubboServiceImpl.insOrder(order, list, shipping);
			if (index > 0) {
				resul.setStatus(200);
				//从购物车删除已购买的商品(从redis中删除)
				String json = jedisDaoImpl.get(cartKey+user.get("username"));
				List<TbItemChild> listCart = JsonUtils.jsonToList(json, TbItemChild.class);
				List<TbItemChild> listNew = new ArrayList<>();
				for (TbItemChild child : listCart) {
					for(TbOrderItem item : list) {
						if (Long.parseLong(item.getItemId()) == child.getId().longValue()) {
							listNew.add(child);
						}
					}
				}
				for (TbItemChild tbItemChild : listNew) {
					listCart.remove(tbItemChild);
				}
				jedisDaoImpl.set(cartKey+user.get("username"), JsonUtils.objectToJson(listCart));
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resul;
	}

}
