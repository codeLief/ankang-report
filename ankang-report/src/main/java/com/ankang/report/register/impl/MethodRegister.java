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
package com.ankang.report.register.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ankang.report.annotation.Alias;
import com.ankang.report.annotation.HTTP;
import com.ankang.report.main.ReportCabinet;
import com.ankang.report.model.ExecuteMethod;
import com.ankang.report.pool.AbstractReportAliasPool;
import com.ankang.report.register.ReportRegister;

/**
 * 
 * @Description: 方法注册器
 * @author: ankang
 * @date: 2015-10-2 下午1:09:54
 */
@SuppressWarnings("all")
public final class MethodRegister extends AbstractReportAliasPool implements
		ReportRegister {

	private static final Logger logger = Logger.getLogger(MethodRegister.class);

	public static final String METHOD_ALIAS_NAME = "methodPool";
	private static final Map methodPool = new HashMap();

	@Override
	public boolean reginster() {
		ReportCabinet.getReportApplicationContext().putPool(METHOD_ALIAS_NAME,
				methodPool);
		return Boolean.TRUE;
	}

	@Override
	public void configer(Class<?> clazz) {
		if (null == clazz) {
			return;
		}
		Alias alias = clazz.getAnnotation(Alias.class);
		String methodName = null;
		if (null == alias || "".equals(alias.alias())) {
			methodName = getAlias(clazz.getSimpleName());
			logger.info("Module name.[" + methodName + "] access time using this module name！");
		}else{
			methodName = alias.alias();
		}
		Method[] methods = clazz.getMethods();
		ExecuteMethod em = new ExecuteMethod(methodName, clazz);
		for (Method method : methods) {
			if (Modifier.isPublic(method.getModifiers()) 
					&& method.getAnnotation(Override.class) == null
					&& !Modifier.isNative(method.getModifiers())
					&& !Modifier.isStatic(method.getModifiers())
					&& !Modifier.isFinal(method.getModifiers())) {
				if (method.getAnnotation(HTTP.class) != null ) {
					mountMethod(method, em, HTTP.class);
					continue;
				}
				if (method.getAnnotation(RequestMapping.class) != null) {
					mountMethod(method, em, RequestMapping.class);
					continue;
				}
				
				logger.debug("No registered object or no identification alias method:"
						+ method);
			}
		}
		synchronized (methodPool) {
			setMountPool(methodPool);
			mount(em.getModuleName(), em);
		}
	}

	@Override
	protected void setMountPool(Map waitMap) {
		this.waitMap = waitMap;
	}
}
