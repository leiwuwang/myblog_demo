package com.leiwuwang.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	protected final Logger log = LoggerFactory.getLogger(getClass());

	//Redis服务器IP
	public static String ADDR = getProperty("redisAddr");
    
    //Redis的端口号
	public static int PORT = Integer.parseInt(getProperty("redisPort"));
    
    //访问密码
	public static String AUTH = getProperty("password");
    
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	public static int MAX_IDLE = Integer.parseInt(getProperty("maxIdle"));
    
    //最大连接数
	public static int MAX_TOTAL = Integer.parseInt(getProperty("maxTotal"));
    
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	public static int MAX_WAIT = Integer.parseInt(getProperty("maxWaitMillis"));
    
	public static JedisPool jedisPool = null;

	private static JedisPool getPool(){
		if(jedisPool == null){
			JedisPoolConfig config = new JedisPoolConfig(); 
	        config.setMaxIdle(MAX_IDLE);
			config.setMaxTotal(MAX_TOTAL);
			config.setMaxWaitMillis(MAX_WAIT);
			config.setTestOnBorrow(true);  
            config.setTestOnReturn(true);
			jedisPool = new JedisPool(config, ADDR, PORT, 180000, AUTH);
        }
        return jedisPool;
	}

	public static Jedis getJedis(){
		Jedis jedis  = null;
		try{
			jedis = getPool().getResource();
		} catch (Exception e) {
            getPool().returnBrokenResource(jedis);
            e.printStackTrace();
        }
		return jedis;
	}

	public static void closeJedis(Jedis jedis){
		if(jedis != null){
			getPool().returnResource(jedis);
		}
	}

	public static Boolean exists(String key){
		Jedis jedis = RedisUtil.getJedis();
		Boolean val = jedis.exists(key);
		RedisUtil.closeJedis(jedis);
		return val;
	}
	
	public static Boolean exists(byte[] key){
		Jedis jedis = RedisUtil.getJedis();
		Boolean val = jedis.exists(key);
		RedisUtil.closeJedis(jedis);
		return val;
	}
	
	public static Boolean hexists(byte[] key,byte[] field){
		Jedis jedis = RedisUtil.getJedis();
		Boolean val = jedis.hexists(key, field);
		RedisUtil.closeJedis(jedis);
		return val;
	}

	public static Boolean hexists(String key,String field){
		Jedis jedis = RedisUtil.getJedis();
		Boolean val = jedis.hexists(key, field);
		RedisUtil.closeJedis(jedis);
		return val;
	}

	public static String get(String key){
		Jedis jedis = RedisUtil.getJedis();
		String val = jedis.get(key);
		RedisUtil.closeJedis(jedis);
		return val;
	}

	public static byte[] get(byte[] key){
		Jedis jedis = RedisUtil.getJedis();
		byte[] val = jedis.get(key);
		RedisUtil.closeJedis(jedis);
		return val;
	}

	public static String set(String key,String value){
		Jedis jedis = RedisUtil.getJedis();
		String val = jedis.set(key, value);
		RedisUtil.closeJedis(jedis);
		return val;
	}
	
	public static String set(byte[] key,byte[] value){
		Jedis jedis = RedisUtil.getJedis();
		String val = jedis.set(key, value);
		RedisUtil.closeJedis(jedis);
		return val;
	}
	
	public static String setex(String key,int seconds,String value){
		Jedis jedis = RedisUtil.getJedis();
		String val = jedis.setex(key, seconds, value);
		RedisUtil.closeJedis(jedis);
		return val;
	}

	public static String setex(byte[] key,int seconds,byte[] value){
		Jedis jedis = RedisUtil.getJedis();
		String val = jedis.setex(key, seconds, value);
		RedisUtil.closeJedis(jedis);
		return val;
	}

	public static Long del(String key){
		Jedis jedis = RedisUtil.getJedis();
		Long l = jedis.del(key);
		RedisUtil.closeJedis(jedis);
		return l;
	}

	public static Long del(byte[] key){
		Jedis jedis = RedisUtil.getJedis();
		Long l = jedis.del(key);
		RedisUtil.closeJedis(jedis);
		return l;
	}

	public static Long ttl(byte[] key){
		Jedis jedis = RedisUtil.getJedis();
		long l = jedis.ttl(key);
		RedisUtil.closeJedis(jedis);
		return l;
	}
	
	public static Long ttl(String key){
		Jedis jedis = RedisUtil.getJedis();
		long l = jedis.ttl(key);
		RedisUtil.closeJedis(jedis);
		return l;
	}
	
	public static String hmset(byte[] key, Map<byte[], byte[]> hash){
		Jedis jedis = RedisUtil.getJedis();
		String s = jedis.hmset(key, hash);
		RedisUtil.closeJedis(jedis);
		return s;
	}
	
	public static long hset(String key,String field,String value){
		Jedis jedis = RedisUtil.getJedis();
		long s = jedis.hset(key, field,value);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static long sadd(byte[] key,byte[][] value){
		Jedis jedis = RedisUtil.getJedis();
		long s = jedis.sadd(key, value);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static long sadd(byte[] key,byte[] value){
		Jedis jedis = RedisUtil.getJedis();
		long s = jedis.sadd(key, value);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static long sadd(String key,String[] value){
		Jedis jedis = RedisUtil.getJedis();
		long s = jedis.sadd(key, value);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static long sadd(String key,String value){
		Jedis jedis = RedisUtil.getJedis();
		long s = jedis.sadd(key, value);
		RedisUtil.closeJedis(jedis);
		return s;
	}
	
	public static Set<byte[]> sdiff(byte[] key1,byte[] key2){
		Jedis jedis = RedisUtil.getJedis();
		Set<byte[]> s = jedis.sdiff(key1, key2);
		RedisUtil.closeJedis(jedis);
		return s;
	}
	
	public static Set<String> sdiff(String key1,String key2){
		Jedis jedis = RedisUtil.getJedis();
		Set<String> s = jedis.sdiff(key1, key2);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static Set<String> smembers(String key){
		Jedis jedis = RedisUtil.getJedis();
		Set<String> s = jedis.smembers(key);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static List<byte[]> hmget(byte[] key1,byte[][] key2){
		Jedis jedis = RedisUtil.getJedis();
		List<byte[]> s = jedis.hmget(key1,key2);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static byte[] hget(byte[] key1,byte[] key2){
		Jedis jedis = RedisUtil.getJedis();
		byte[] s = jedis.hget(key1,key2);
		RedisUtil.closeJedis(jedis);
		return s;
	}

	public static long srem(byte[] key,byte[] value){
		Jedis jedis = RedisUtil.getJedis();
		long s = jedis.srem(key, value);
		RedisUtil.closeJedis(jedis);
		return s;
	}
	
	public static long srem(String key,String value){
		Jedis jedis = RedisUtil.getJedis();
		long s = jedis.srem(key, value);
		RedisUtil.closeJedis(jedis);
		return s;
	}
	
	public static void expire(byte[] key,int seconds){
		Jedis jedis = RedisUtil.getJedis();
		jedis.expire(key, seconds);
		RedisUtil.closeJedis(jedis);
	}

	public static void hdel(byte[] key1,byte[] key2){
		Jedis jedis = RedisUtil.getJedis();
		jedis.hdel(key1,key2);
		RedisUtil.closeJedis(jedis);
	}
	
	public static boolean sismember(String key,String value){
		Jedis jedis = RedisUtil.getJedis();
		boolean a  =  jedis.sismember(key, value);
		RedisUtil.closeJedis(jedis);
		return a;
	}
	
	public static String getProperty(String key){
		return PropertiesUtil.getPropertyValue("/redis.properties", key);
	}
	
	public static void main(String[] args) {
		List list = new ArrayList();
		for(int i=0;i<1000;i++){
			list.add(i+1);
		}
		RedisUtil.set("test_test".getBytes(), SerializationUtil.serialize(list));
		List list2 = new ArrayList();
		for(int j=200;j<400;j++){
			list2.add(j+1);
		}
		byte[][] friends = new byte[list2.size()][];
		for(int m =0;m<list2.size();m++ ){
			friends[m] = SerializationUtil.serialize(list2.get(m));
		}
		RedisUtil.sadd(("test_friends").getBytes(), friends);
		long date1 = new Date().getTime();
		byte[][] result = new byte[list.size()][];
		for(int k=0;k<list.size();k++){
			result[k] = SerializationUtil.serialize(list.get(k));
		}
		long date2 = new Date().getTime();
		System.out.println("循环序列化用时:"+(date2-date1));
		long date3 = new Date().getTime();
		RedisUtil.sadd(("test_recommendFriend").getBytes(), result);
		long date4 = new Date().getTime();
		System.out.println("存入缓存用时:"+(date4-date3));
		long date5 = new Date().getTime();
		boolean a = RedisUtil.exists(("test_recommendFriend").getBytes());
		long date6 = new Date().getTime();
		System.out.println("判断是否存在key用时:"+(date6-date5));
		long date7 = new Date().getTime();
		Set<byte[]> ok = RedisUtil.sdiff(("test_recommendFriend").getBytes(),("test_friends").getBytes());
		long date8 = new Date().getTime();
		System.out.println("取出差集用时:"+(date8-date7));
	}
}
