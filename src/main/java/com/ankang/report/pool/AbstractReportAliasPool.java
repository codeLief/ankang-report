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
package com.ankang.report.pool;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ankang.report.annotation.HTTP;
import com.ankang.report.annotation.ReportParam;
import com.ankang.report.exception.ReportException;
import com.ankang.report.model.ExecuteMethod;
import com.ankang.report.model.ReportRequest;

/**
 * 
 * @Description: 装配器
 * @author: ankang
 * @date: 2015-9-29 上午10:34:05
 */
@SuppressWarnings("all")
public abstract class AbstractReportAliasPool {
	private static final Logger logger = Logger
			.getLogger(AbstractReportAliasPool.class);
	protected Map waitMap;

	protected final <T> boolean mount(String alias, T aliasClass) {
		if (StringUtils.isEmpty(alias)) {
			logger.error("alias and aliasClass can't be empty");
			return Boolean.FALSE;
		}
		synchronized (waitMap) {
			if (null != waitMap && waitMap.containsKey(alias)) {
				logger.error("duplicate alias : " + alias);
				return Boolean.FALSE;
			}
			waitMap.put(alias, aliasClass);
			return Boolean.TRUE;
		}
	}

	protected abstract void setMountPool(Map waitMap);

	protected final void mountMethod(Method method, ExecuteMethod em,
			Class<? extends Annotation> annotation) {

		Map<String, Method> tempMethod = null;
		Map<String, RequestMethod[]> tempType = null;
		LinkedHashMap<String, Class<?>> paramsType = null;
		Map<String, String> executeDesc = null;
		Map<String, LinkedHashMap<String, Class<?>>> parameterType = null;

		Annotation anno = method.getAnnotation(annotation);
		if (null != anno) {
			tempMethod = new HashMap<String, Method>();
			tempType = new HashMap<String, RequestMethod[]>();
			paramsType = new LinkedHashMap<String, Class<?>>();
			parameterType = new HashMap<String, LinkedHashMap<String, Class<?>>>();
			executeDesc = new HashMap<String, String>();
			if (anno instanceof HTTP) {
				HTTP http = (HTTP) anno;
				if (null != http && !"".equals(http.value())) {
					tempMethod.put(http.value(), method);
					tempType.put(http.value(),
							http.supportMethod());
					executeDesc.put(http.value(), 
							http.desc());
					mountPrams(paramsType, method);
					parameterType.put(http.value(), paramsType);
				}
			} else if (anno instanceof RequestMapping) {
				RequestMapping requestMapping = (RequestMapping) anno;
				if (requestMapping != null && "".equals(requestMapping.value())) {
					String methodName = requestMapping.value()[0];
					if (methodName.startsWith("/")) {
						methodName = methodName.substring(1);
					}
					tempMethod.put(methodName, method);
					tempType.put(methodName, requestMapping.method());
					executeDesc.put(methodName, requestMapping.name());
					mountPrams(paramsType, method);
					parameterType.put(methodName, paramsType);
				}
			}
		}
		em.getExecuteDesc().add(executeDesc);
		em.getExecuteMethod().add(tempMethod);
		em.getExecuteType().add(tempType);
		em.getParameterType().add(parameterType);
	}

	private String matchPrams(Annotation annotation) {

		if (annotation instanceof ReportParam) {
			return ((ReportParam) annotation).value();
		} else if (annotation instanceof RequestParam) {
			return ((RequestParam) annotation).value();
		}
		return "";
	}

	private void mountPrams(LinkedHashMap<String, Class<?>> paramsType,
			Method method) {

		Class<?>[] parameterTypes = method.getParameterTypes();

		Annotation[][] annotations = method.getParameterAnnotations();

		for (int i = 0; i < parameterTypes.length; i++) {

			if (annotations.length < i) {
				throw new ReportException(
						"Please add an effective note for the argument, such as: HTTPParam, RequestParam");
			}
			if ((ReportRequest.class.isAssignableFrom(parameterTypes[i]))
					|| (!parameterTypes[i].isPrimitive()
					&& !parameterTypes[i].toString().matches("^.+java\\..+$")
					&& parameterTypes[i].toString().matches("^class.+$"))) {

				Field[] fields = parameterTypes[i].getDeclaredFields();
				for (Field field : fields) {
					if(!Modifier.isFinal(field.getModifiers()) || !Modifier.isStatic(field.getModifiers())){
						
						paramsType.put(field.getName(), field.getType());
					}
				}
			}else{
				if(annotations.length >= i && annotations[i].length > 0){
					
					paramsType.put(matchPrams(annotations[i][0]), parameterTypes[i]);
				}
			}
		}
	}
	protected final String getAlias(String name){
		
		if(!StringUtils.isEmpty(name) && !name.contains(".")){
			return name.replaceFirst(name.substring(0, 1),
					name.substring(0, 1).toLowerCase());
		}
		return name;
	}
}
