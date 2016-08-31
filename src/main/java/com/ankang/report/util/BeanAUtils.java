package com.ankang.report.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;


/**
 * 
 * @author: ankang
 * @date: 2015-5-20 下午2:43:28
 */
public class BeanAUtils extends PropertyUtils {

	/**
	 * 
	 * @Description: list对象属性拷贝，对象属性必须一样
	 * @author:ankang
	 * @param <T>
	 * @date: 2015-5-20 下午2:43:28
	 * @param source
	 * @param target
	 *            必须给定一个目标类型
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> copyList(List<?> source, Class<T> targetClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		if (source == null || source.size() < 1) {

			throw new IllegalArgumentException("集合source不能为空");
		}
		if (targetClass == null) {
			
			throw new IllegalArgumentException("必须给定目标类型");
		}

		List<T> result = new ArrayList<T>(source.size());
		
		for (Object sou : source) {
			
			Object tar = null;
			try {
				tar = targetClass.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("创建目标类异常，异常类：" + targetClass
						+ "\n 异常信息：" + e.getStackTrace());
			}
			copyProperties(sou, tar);
			result.add((T)tar);
		}
		return result;
	}
	/**
	 * 
		 * @Description: 层级属性获取
	     * @author: qd-ankang
	     * @date: 2016-7-6 下午4:18:52
	     * @param bean
	     * @param property data.code
	     * @return
	     * @throws IllegalAccessException
	     * @throws InvocationTargetException
	     * @throws NoSuchMethodException
	 */
	public static Object getLayerProperty(Object bean, String property) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		
		if(property.contains(".")){
			
			String tempP = property.substring(0, property.indexOf("."));
			
			return getLayerProperty(getProperty(bean, tempP), property.substring(property.indexOf(".") + 1));
			
		}
		return getProperty(bean, property);
	}
}
