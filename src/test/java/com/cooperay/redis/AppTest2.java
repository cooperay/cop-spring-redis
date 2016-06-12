package com.cooperay.redis;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest2 
    extends TestCase
{
	
    public static void main(String[] args)
    {
    	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-redis.xml");
    	
    	UserServeice userService = context.getBean(UserServeice.class);
    	System.out.println(userService.sayHello("liyang2222"));;
    	userService.add();
    	//Properties p = new classpath;
    	
    }

}
