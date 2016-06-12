package com.cooperay.redis.spring;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.cooperay.redis.spring.Cacheable.KeyMode;

@Aspect
@EnableAspectJAutoProxy
public class CacheableAop {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	private boolean manualEnable=true;
	
	/**
	 * 
	 * @作者：李阳
	 * @时间：2016年6月12日 上午9:03:11
	 * @描述：切点执行方法、拦截@Cacheable注解、如果存在以目标方法名作为key的缓存就返回缓存中的数据
	 *       如果不存在就执行目标方法并将结果放入缓存
	 */
	@Around("@annotation(cacheable)")
	public Object cached(final ProceedingJoinPoint pjp,Cacheable cacheable) throws Throwable{
		String key = getKey(pjp,cacheable);
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		Object value = valueOperations.get(key);
		if(value!=null){
			return value;
		}
		value =pjp.proceed();
		if(cacheable.expire()<=0){
			valueOperations.set(key, value);
		}else{
			valueOperations.set(key, value, cacheable.expire(), TimeUnit.SECONDS);
		}
		return value;
	}
	
	/**
	 * 在执行完目标方法后清除表达式指定的缓存
	 * @作者：李阳
	 * @时间：2016年6月12日 上午10:57:19
	 * @描述：
	 */
	@After("@annotation(cacheFlush)")
	public void flushCache(CacheFlush cacheFlush) throws Throwable{
		String pattern = cacheFlush.pattern();
		if("".equals(pattern) || !manualEnable){
			return;
		}
		redisTemplate.delete(redisTemplate.keys(pattern));
	}
	
	
	/**
	 * 
	 * @作者：李阳
	 * @时间：2016年6月12日 上午8:55:19
	 * @描述：得到缓存的key 目标方法全类名加上方法名
	 */
	private String getKey(ProceedingJoinPoint pjp,Cacheable cacheable){
		//如果指定key直接使用指定的key
		if(cacheable.key()!=null && !"".equals(cacheable.key())){
			return cacheable.key();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(pjp.getSignature().getDeclaringTypeName());
		sb.append(".");
		sb.append(pjp.getSignature().getName());
		Object[] args = pjp.getArgs();
		
		if(cacheable.keyMode() == KeyMode.DEFAULT){
			Annotation[][] pas = ((MethodSignature)pjp.getSignature()).getMethod().getParameterAnnotations();
			for(int i=0;i<pas.length;i++){
				for(Annotation an:pas[i]){
					if(an instanceof CacheKey){
						sb.append(".").append(args[i].toString());
						break;
					}
				}
			}
		
		}else if(cacheable.keyMode()==KeyMode.ALL){
			for(Object arg : args ){
				sb.append(".").append(arg.toString());
			}
		}
		
		return sb.toString();
	}

	public boolean isManualEnable() {
		return manualEnable;
	}

	public void setManualEnable(boolean manualEnable) {
		this.manualEnable = manualEnable;
	}

	
	
	
}
