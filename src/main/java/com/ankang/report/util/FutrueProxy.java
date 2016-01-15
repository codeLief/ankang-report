package com.ankang.report.util;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


/**
 * @author ankang
 * @time 2016-1-10
 */
public class FutrueProxy{
	
	private static final Logger logger = Logger.getLogger(FutrueProxy.class);
	
	private ExecutorService service;
	
	public FutrueProxy(int nThreads){
		
		if(nThreads > 0){
			
			service = new ThreadPoolExecutor(nThreads, nThreads, 60L, TimeUnit.SECONDS,
		            new SynchronousQueue<Runnable>());
		}else{
			service = Executors.newCachedThreadPool();
		}
	}
	
	/**
	 * 
	 * @author ankang
	 * @time 2016-1-10
	 * @param targerService 目标类
	 * @param targerMethod 目标方法名称 注：不要有方法名称一样，参数个数一样的本类认为重复的方法。
	 * @param resultType 返回值类型
	 * @param args 参数，与实际方法参数类型，顺序一致。
	 * @return 返回Future 通过get()获取真实返回值，此方法为阻塞方法
	 */
	public <T> Future<T> excute(final Object targerService, final String targerMethod, final Class<T> resultType, final Object...args) {
		
		Future<T> real = service.submit(new Callable<T>() {

			@Override
			public T call() throws Exception {
				
				try {
					
					Method method = getMethod(targerService, targerMethod, args);
					
					return (T)method.invoke(targerService, args);
				} catch (Exception e) {
					
					logger.error(String.format("并行调用失败  targetService:[%s], targetMethod:[%s], args:[%s]",
							targerService, targerMethod, args), e);
					throw e;
				} 
			}
		});
		
		return real;
	}
	
	public void excute(final Object targerService, final String targerMethod, final Object...args){
		
		service.execute(new Runnable() {
			
			@Override
			public void run(){
				try {
					getMethod(targerService, targerMethod, args).invoke(targerService, args);
					
				} catch (Exception e) {
					logger.error(String.format("并行调用失败  targetService:[%s], targetMethod:[%s], args:[%s]",targerService, 
								targerMethod, args), e);
				}
				
			}
		});
		
	}
	private Method getMethod(Object targer, String targerMethod, Object...args){
		
		if(args == null){
			args = new Object[0];
		}
		int i = 0;
		Method[] methods = targer.getClass().getMethods();
		for (; i < methods.length; i++) {
			if(targerMethod.equals(methods[i].getName()) 
					&& args.length == methods[i].getParameterTypes().length){
				break;
			}
		}
		return methods[i];
	}
}
