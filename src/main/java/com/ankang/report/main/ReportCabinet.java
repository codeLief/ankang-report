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
package com.ankang.report.main;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.ankang.report.context.ReportApplicationContext;
import com.ankang.report.context.ReportContext;
import com.ankang.report.exception.ReportException;
import com.ankang.report.factory.ObjProxy;
import com.ankang.report.model.ExecuteMethod;
import com.ankang.report.model.Monitor;
import com.ankang.report.model.MonitorView;
import com.ankang.report.register.impl.MethodRegister;
import com.ankang.report.register.impl.MonitorRegister;
import com.ankang.report.register.impl.ResolverRegister;
import com.ankang.report.resolver.ReportResolver;
import com.google.common.collect.Collections2;



@SuppressWarnings("all")
public class ReportCabinet{
	
	private static ReportContext RA;
	static{
		RA = ReportApplicationContext.getInstance(new ObjProxy());
	}
	public static ReportResolver match(String resolverAlias){
		if(StringUtils.isEmpty(resolverAlias)){
			throw new ReportException("解析类型不能为空");
		}
		Map<String, Object> resolvePool = RA.getPool(ResolverRegister.POOL_ALIAS_NAME);
		return (ReportResolver)RA.getBean(resolvePool.get(resolverAlias).toString());
	}
	public static Method match(String serviceAlias, String methodAlias){
		ExecuteMethod em = matchExecuteMethod(serviceAlias);
		List<Map<String, Method>> executeMethod = em.getExecuteMethod();
		for (Map<String, Method> map : executeMethod) {
			if(null != map.get(methodAlias)){
				return map.get(methodAlias);
			}
		}
		return null;
	}
	public static Class matchModule(String serviceAlias){
		if(StringUtils.isEmpty(serviceAlias)){
			throw new ReportException("模块名称不能为空");
		}
		ExecuteMethod em = matchExecuteMethod(serviceAlias);
		if(null == em){
			throw new ReportException("未找到有效的模块名称");
		}
		return em.getClazz();
	}
	
	public static String matchDesc(String serviceAlias, String methodAlias){
		ExecuteMethod em = matchExecuteMethod(serviceAlias);
		
		for (Map<String, String> map : em.getExecuteDesc()) {
			if(null != map.get(methodAlias)){
				return map.get(methodAlias);
			}
		}
		return "";
	}
	public static ExecuteMethod matchExecuteMethod(String serviceAlias){
		Map servicePool = RA.getPool(MethodRegister.METHOD_ALIAS_NAME);
		return (ExecuteMethod) servicePool.get(serviceAlias);
	}
	
	public static ReportContext getReportApplicationContext(){
		return RA;
	}
	public static Object getBean(Class clazz){
		return RA.getBean(clazz);
	}
	public static Object getBean(String clazz){
		return RA.getBean(clazz);
	}
	public static Monitor matchMonitor(String modul, String method){
		
		if(StringUtils.isEmpty(modul) || StringUtils.isEmpty(method)){
			return null;
		}
		Map moduls = RA.getPool(MonitorRegister.MONITOR_ALIAS_NAME);
		
		List<Monitor>  monitors = (List<Monitor>) moduls.get(modul);
		if(!CollectionUtils.isEmpty(monitors)){
			for (Monitor monitor : monitors) {
				if(method.equalsIgnoreCase(monitor.getMethod())){
					return monitor;
				}
			}
		}
		return null;
	}
	
	public static MonitorView matchMonitorView(String modul, String method){
		
		Monitor monitor = matchMonitor(modul, method);
		if(monitor != null){
			return monitor.getMonitorView();
		}
		return new MonitorView();
	}
}
