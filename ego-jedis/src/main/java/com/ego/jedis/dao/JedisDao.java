package com.ego.jedis.dao;

public interface JedisDao {
	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	Boolean exists(String key);

	String get(String key);

	String set(String key, String value);
	
	Long del(String key);
	/**
	 * 设置key的过期时间
	 * @param key
	 * @param seconds
	 * @return
	 */
	Long expire(String key,int seconds);
}
