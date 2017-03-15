package com.linjia.tools;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.linjia.core.feature.cache.CachePool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Redis常用工具类(里面没有的方法可以自行编辑添加)
 * 
 * @author lixinling:
 * @date 2016年7月1日 下午1:05:34
 * @version 1.0
 */
public class RedisUtil {

	private static JedisPool jedisPool = CachePool.getInstance().getJedisPool();

	/**
	 * 根据key取得redis中存储的值
	 * 
	 * lixinling 2016年7月1日 下午1:37:51
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			value = jedis.get(key);
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;

	}
	
	/**
	 * 根据key取得redis中存储的JSON对象
	 * 
	 * lixinling 2016年7月1日 下午1:37:51
	 * 
	 * @param key
	 * @return
	 */
	public static JSONObject getJSONObj(String key) {
		Jedis jedis = null;
		String value = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			value = jedis.get(key);
			if(Tools.isEmpty(value)){
				return null;
			}else{
				return new JSONObject(value);
			}
		} catch (Exception e) {
			jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return null;
		
	}

	/**
	 * 根据传入的key和value的值，在redis中进行存储
	 * 
	 * lixinling 2016年7月1日 下午2:04:21
	 * 
	 * @param key
	 * @param value
	 */
	public static void put(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			jedis.set(key, value);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 根据传入的key和value的值，在redis中进行存储 并设置超时时间
	 * 
	 * lixinling 2016年7月1日 下午2:06:17
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时时间
	 */
	public static void put(String key, String value, int seconds) {
		Jedis jedis = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			jedis.set(key, value);
			jedis.expire(key, seconds);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 根据key来删除redis中的值
	 * 
	 * lixinling 2016年7月1日 下午2:08:18
	 * 
	 * @param key
	 * @return
	 */
	public static Object remove(String key) {
		Jedis jedis = null;
		Object value = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			value = jedis.del(key);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
		} finally {
			jedisPool.returnResource(jedis);
		}
		return value;
	}

	/**
	 * 根据传入的key和Map,在redis中进行存储
	 * 
	 * lixinling 2016年7月1日 下午2:06:17
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时时间
	 */
	public static void hmset(String key, Map<String,String> hash) {
		Jedis jedis = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			jedis.hmset(key, hash);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 根据传入的key和field,在redis中进行存储
	 * 
	 * lixinling 2016年7月1日 下午2:06:17
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时时间
	 */
	public static void hset(String key, String field, String value) {
		Jedis jedis = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			jedis.hset(key, field, value);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
	}

	/**
	 * 根据传入的key和field,获取在哈希表中指定字段的值
	 * 
	 * lixinling 2016年7月1日 下午2:06:17
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时时间
	 */
	public static String hget(String key, String field) {
		Jedis jedis = null;
		String result = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			result = jedis.hget(key, field);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}

	/**
	 * 根据传入的key和fields,获取所有给定字段的值
	 * 
	 * lixinling 2016年7月1日 下午2:06:17
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时时间
	 */
	public static List<String> hmget(String key, String[] fields) {
		Jedis jedis = null;
		List<String> list = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			list = jedis.hmget(key, fields);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return list;
	}

	/**
	 * 获取在哈希表中指定 key 的所有字段和值
	 * 
	 * lixinling 2016年7月1日 下午2:06:17
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时时间
	 */
	public static Map<String,String> hgetAll(String key) {
		Jedis jedis = null;
		Map<String,String> map = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			map = jedis.hgetAll(key);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return map;
	}
	
	/**
	 * 根据传入的key和fields,删除一个或多个哈希表字段
	 * 
	 * lixinling 2016年7月1日 下午2:06:17
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 *            超时时间
	 */
	public static Long hdel(String key, String[] fields) {
		Jedis jedis = null;
		Long result = null;
		try {
			jedis = CachePool.getInstance().getJedis();
			result = jedis.hdel(key, fields);
		} catch (JedisConnectionException e) {
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
		} finally {
			jedisPool.returnResource(jedis);
		}
		return result;
	}
}
