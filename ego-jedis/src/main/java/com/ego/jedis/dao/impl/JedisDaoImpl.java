package com.ego.jedis.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.ego.jedis.dao.JedisDao;

import redis.clients.jedis.JedisCluster;

@Repository
public class JedisDaoImpl implements JedisDao {
	@Resource
	private JedisCluster jedisClients;
	@Override
	public Boolean exists(String key) {
		// TODO Auto-generated method stub
		return jedisClients.exists(key);
	}

	@Override
	public String get(String key) {
		// TODO Auto-generated method stub
		return jedisClients.get(key);
	}

	@Override
	public String set(String key, String value) {
		// TODO Auto-generated method stub
		return jedisClients.set(key, value);
	}

	@Override
	public Long del(String key) {
		// TODO Auto-generated method stub
		return jedisClients.del(key);
	}
	
	@Override
	public Long expire(String key, int seconds) {
		return jedisClients.expire(key, seconds);
	}

}
