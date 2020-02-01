package com.leiwuwang.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDbUtil {
	protected final Logger log = LoggerFactory.getLogger(getClass());
    
	public static JedisPool jedisDbPool = null;

	private static JedisPool getPool(){
		if(jedisDbPool == null){
			JedisPoolConfig config = new JedisPoolConfig(); 
	        config.setMaxIdle(RedisUtil.MAX_IDLE);
			config.setMaxTotal(RedisUtil.MAX_TOTAL);
			config.setMaxWaitMillis(RedisUtil.MAX_WAIT);
			config.setTestOnBorrow(true);  
            config.setTestOnReturn(true);
			jedisDbPool = new JedisPool(config, RedisUtil.ADDR, RedisUtil.PORT, 180000, RedisUtil.AUTH);
        }
        return jedisDbPool;
	}

	public static Jedis getJedis(int index){
		Jedis jedis  = null;
		try{
			jedis = getPool().getResource();
			//选择指定数据库
			jedis.select(index);
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
	
	public static Boolean exists(String key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		Boolean val = jedis.exists(key);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static Boolean hexists(String key,String field,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		Boolean val = jedis.hexists(key, field);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static Boolean exists(byte[] key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		Boolean val = jedis.exists(key);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static String get(String key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		String val = jedis.get(key);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static byte[] get(byte[] key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		byte[] val = jedis.get(key);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static long incr(String key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		long val = jedis.incr(key);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static String set(String key,String value,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		String val = jedis.set(key, value);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static String set(byte[] key,byte[] value,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		String val = jedis.set(key, value);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static String setex(String key,int seconds,String value,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		String val = jedis.setex(key, seconds, value);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}
	
	public static String setex(byte[] key,int seconds,byte[] value,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		String val = jedis.setex(key, seconds, value);
		RedisDbUtil.closeJedis(jedis);
		return val;
	}

	public static Long del(String key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		Long l = jedis.del(key);
		RedisDbUtil.closeJedis(jedis);
		return l;
	}

	public static Long del(byte[] key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		Long l = jedis.del(key);
		RedisDbUtil.closeJedis(jedis);
		return l;
	}

	public static Long ttl(byte[] key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		long l = jedis.ttl(key);
		RedisDbUtil.closeJedis(jedis);
		return l;
	}

	public static Long ttl(String key,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		long l = jedis.ttl(key);
		RedisDbUtil.closeJedis(jedis);
		return l;
	}

	public static long hset(String key,String field,String value,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		long s = jedis.hset(key, field,value);
		RedisDbUtil.closeJedis(jedis);
		return s;
	}

	public static String hget(String key,String filed,int index){
		Jedis jedis = RedisDbUtil.getJedis(index);
		String s  = jedis.hget(key,filed);
		RedisDbUtil.closeJedis(jedis);
		return s;
	}
	
	public static void main(String[] args) {
		//适合连续调用方式
		Jedis jedis = RedisDbUtil.getJedis(1);
		String str = jedis.set("lw", "hello world");
		System.out.println(jedis.get("lw")+"  "+str.equals("OK"));
		RedisDbUtil.closeJedis(jedis);
		
		//适合快速调用方式
		int testIndex = 8;
		RedisDbUtil.set("lw", "lw", testIndex);
		System.out.println(RedisDbUtil.get("lw", testIndex));

	}
}
