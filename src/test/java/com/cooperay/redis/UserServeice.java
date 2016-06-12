package com.cooperay.redis;

import org.springframework.stereotype.Service;

import com.cooperay.redis.spring.CacheFlush;
import com.cooperay.redis.spring.CacheKey;
import com.cooperay.redis.spring.Cacheable;

@Service("userService")
public class UserServeice {
	
	@Cacheable
	public String sayHello(@CacheKey String id){
		System.out.println(id+" say hello" );
		return id + " say hello!";
	}
	
	@CacheFlush(pattern="com.cooperay.redis.UserServeice.sayHello*")
	public void add(){
		System.out.println("add method exec");
	}
}
