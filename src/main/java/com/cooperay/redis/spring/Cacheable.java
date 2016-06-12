package com.cooperay.redis.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
* @描述: 启用缓存注解、在方法上添加该注解表示将该方法纳入到缓存管理中 
* @时间: 2016年6月12日 上午10:53:19 
* @作者：李阳
* @版本：V1.0.0 
*
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cacheable {
	
	public enum KeyMode{
		DEFAULT, //添加@cachekey的参数才加入key后缀中
		ALL;     //所有参数都加入key后缀
	}
	
	public String key() default "";
	
	public KeyMode keyMode() default KeyMode.DEFAULT;
	
	public int expire() default 0;
	
	
}
