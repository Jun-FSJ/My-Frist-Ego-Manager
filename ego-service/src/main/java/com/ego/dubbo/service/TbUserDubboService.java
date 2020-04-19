package com.ego.dubbo.service;

import com.ego.pojo.TbUser;

public interface TbUserDubboService {
	/**
	 * 根据用户名和密码进行查询
	 * @param user
	 * @return
	 */
	TbUser selByUser(TbUser user);
}
