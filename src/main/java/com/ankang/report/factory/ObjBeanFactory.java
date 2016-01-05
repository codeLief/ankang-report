/**
 **Copyright (c) 2015, ancher  安康 (676239139@qq.com).
 ** 
 ** This Source Code Form is subject to the terms of the Mozilla Public
 ** License, v. 2.0. If a copy of the MPL was not distributed with this
 ** file, You can obtain one at 
 ** 
 ** 	http://mozilla.org/MPL/2.0/.
 **
 **If it is not possible or desirable to put the notice in a particular
 **file, then You may include the notice in a location (such as a LICENSE
 **file in a relevant directory) where a recipient would be likely to look
 **for such a notice.
 **/
package com.ankang.report.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.ankang.report.exception.ReportException;
import com.ankang.report.main.ReportCabinet;
import com.ankang.report.util.BeanUtil;

public class ObjBeanFactory implements ReportBeanFactory{
	private static final Logger logger = Logger.getLogger(ObjBeanFactory.class);
	
	private void putBean(Object object){
		if(null == object){
			return;
		}
		ReportCabinet.getReportApplicationContext().putBean(object);
	}
	@Override
	public synchronized Object createBean(Class<?> clazz){
		if(null == clazz){
			return null;
		}
		Object object = null;
		object = BeanUtil.getBean(null, clazz);
		if(null != object){
			return object;
		}
		List<Field> fields = new ArrayList<Field>();
		getAllFields(fields, clazz);
		
		try {
			
			object = clazz.newInstance();
			
			for (Field field : fields) {
				if (!Modifier.isFinal(field.getModifiers()) 
						&& !Modifier.isStatic(field.getModifiers()) 
						&&!field.getClass().isPrimitive()
						&& !field.getType().toString().matches("^.+java\\..+$") 
						){
						Class<?> fieldClazz = field.getType();
						Object cBean = null;
						field.setAccessible(true);
						if ((cBean = containsBean(fieldClazz)) != null){
							field.set(object, cBean);
							continue;
						}else{
							cBean = createBean(fieldClazz);
							field.set(object, cBean);
							continue;
						}
				}
			}
		}catch (InstantiationException e1) {
			if(clazz.isInterface()){
				throwException("class is interface", clazz.getName());
			}
			throw new ReportException("Object initialization exception.{}", clazz.getName());
		} catch (IllegalAccessException e1) {
			logger.error("Property is private." + clazz.getName());
			throw new ReportException("Property is private.{}", clazz.getName());
		}
		logger.info("create bean:"+object);
		putBean(object);
		return object;
	}
	private void throwException(String message, Object...args){
		throw new ReportException(message, args);
	}
	private Object containsBean(Class<?> clazz){
		return ReportCabinet.getReportApplicationContext().getBean(clazz);
	}
	private void getAllFields(List<Field> fields, Class<?> clazz) {
		
		if(!clazz.isPrimitive()
				&& clazz.toString().matches("^.+java\\..+$") 
				&& clazz.getSuperclass() != null
				&& !Modifier.isInterface(clazz.getSuperclass().getModifiers()) 
				&& !Modifier.isAbstract(clazz.getSuperclass().getModifiers())){
			getAllFields(fields, clazz.getSuperclass());
		}
		
		Collections.addAll(fields, clazz.getDeclaredFields());
	}
}
