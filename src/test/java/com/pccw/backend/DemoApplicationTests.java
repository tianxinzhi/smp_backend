package com.pccw.backend;

import com.alibaba.fastjson.JSONObject;
import com.pccw.backend.util.Session;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.redis.AutoConfigureDataRedis;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	Session session;

	@Test
	public void contextLoads() {
		JSONObject obj = new JSONObject();
		obj.put("name", "cjl");
		// redisTemplate.opsForValue().set("key", obj);
		session.set("kkk", obj);
	}


}
