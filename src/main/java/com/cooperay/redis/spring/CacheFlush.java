package com.cooperay.redis.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
* @描述: 清除缓存注解、在方法上添加该注解表示在该方法执行完成后清除指定表达式的缓存 
* @时间: 2016年6月12日 上午10:53:19 
* @作者：李阳
* @版本：V1.0.0 
*
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CacheFlush {
	public String pattern() default "";
}
