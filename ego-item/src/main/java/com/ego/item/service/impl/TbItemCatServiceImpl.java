package com.ego.item.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.ego.dubbo.service.TbItemCatDubboService;
import com.ego.item.pojo.PortalMenu;
import com.ego.item.pojo.PortalMenuNode;
import com.ego.item.service.TbItemCatService;
import com.ego.pojo.TbItemCat;

@Service
public class TbItemCatServiceImpl implements TbItemCatService {
	@Reference
	private TbItemCatDubboService tbItemCatDubboServiceImpl;

	@Override
	public PortalMenu showCatMenu() {
		//查询出所有一级菜单
		List<TbItemCat> listCat = tbItemCatDubboServiceImpl.show(0);
		PortalMenu portalMenu = new PortalMenu();
		portalMenu.setData(selAllMenu(listCat));
		return portalMenu;
	}

	/**
	 * 最终返回结果所有查询到的结果
	 */
	public List<Object> selAllMenu(List<TbItemCat> list) {
		List<Object> listNode = new ArrayList<>();
		for (TbItemCat tbItemCat : list) {
			if (tbItemCat.getIsParent()) {
				PortalMenuNode node = new PortalMenuNode();
				node.setU("/products/" + tbItemCat.getId() + ".html");
				node.setN("<a href='/products/" + tbItemCat.getId()+ ".html'>" + tbItemCat.getName() + "</a>");
				node.setI(selAllMenu(tbItemCatDubboServiceImpl.show(tbItemCat.getId())));// 如果是父菜单，就一直递归，直到不是父菜单为止
				listNode.add(node);
			}else {
				listNode.add("/products/"+tbItemCat.getId()+".html|"+tbItemCat.getName());//到这里就是子菜单，就没有I这个属性了。
			}
		}
		return listNode;
	}
}
