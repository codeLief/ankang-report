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
package com.ankang.report.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.ankang.report.annotation.Activate;
import com.ankang.report.exception.ReportException;
import com.ankang.report.filet.Fileter;
import com.ankang.report.filet.Invocation;
import com.ankang.report.filet.Invoker;
import com.ankang.report.main.ReportCabinet;
import com.ankang.report.model.ReportResponse;

/**
 * 
 * @Description: report配置类， report.properties为默认配置文件，report-config.properties使用配置文件，后者会覆盖前者
 * @author: ankang
 * @date: 2015-10-1 下午10:02:24
 */
@SuppressWarnings("all")
public final class ReportConfig {
	
	private static final Logger logger = Logger.getLogger(ReportConfig.class);
	
	protected static final String REPORT = "config/report.properties";//主配置文件
	public static final String REPORT_CONFIG = "report/report-config.properties";//用户配置文件
	private static final Map<String, Object> reportConfig = Collections.synchronizedMap(new HashMap<String, Object>());
	private static final Map<Integer, Fileter> fileters = new TreeMap<Integer, Fileter>();
	private static Properties ps = new Properties();
	
	public static void loadReportConfig(String classPath){
		
		if(null == classPath || REPORT.equals(classPath)){
			if(null != reportConfig.get(ReportConfigItem.LOAD_REPORT_PROPERTIES.getConfigName()) &&
					Boolean.parseBoolean(reportConfig.get(ReportConfigItem.LOAD_REPORT_PROPERTIES.getConfigName()).toString())){
				
				logger.warn("default configuration file is already loaded");
			}
			classPath = REPORT;
			reportConfig.put(ReportConfigItem.LOAD_REPORT_PROPERTIES.getConfigName(), Boolean.TRUE);
		}
		logger.info("load properties " + classPath);
		
		ClassPathResource cp = new ClassPathResource(classPath);
		
		try {
			if(cp.exists()){
				ps.load(cp.getInputStream());
				convertMap((Map)ps);
			}
		} catch (IOException e) {
			throw new ReportException("File read exception file.[%s]", classPath);
		}
		
	}
	
	public static Object getValue(String key){
		if(null == key){
			throwException("key is null");
		}
		key = key.toLowerCase();
		if(!reportConfig.containsKey(key)){
			return null;
		}
		return reportConfig.get(key);
	}
	
	public static boolean containsKey(String key){
		if(null == key){
			logger.warn("key is null");
			return Boolean.FALSE;
		}
		return reportConfig.containsKey(key);
	}
	protected static <K, V> void convertMap(Map<? extends K, ? extends V> m){
		if(null == m){
			logger.error("map is null");
			return;
		}
			
		synchronized (fileters) {
				for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
					
					String key = e.getKey().toString().toLowerCase();
					
					Object value = e.getValue();
					
					if(ReportConfigItem.FILETER.getConfigName().equalsIgnoreCase(key)){
						
						Class<?> filterzz = null;
						Fileter fileter = null;
						try {
							String[] clazzs = null;
							if(value.toString().contains(",")){
								clazzs = value.toString().trim().split(",");
							}
							for (String clazz : clazzs) {
								filterzz = Class.forName(clazz);
								Activate activate = filterzz.getAnnotation(Activate.class);
								if(!fileters.containsValue((fileter = (Fileter) ReportCabinet.getBean(filterzz)))){
									Integer order = order = activate != null?activate.order():Integer.MIN_VALUE;
									if(fileters.containsKey(order)){ //以下两次判断是否存在key，避免临界值死循环,避免临界值越界
										if(order < Integer.MAX_VALUE/2){
											while (fileters.containsKey(order)) {
												order++;
											}
										}else if(order > Integer.MAX_VALUE/2){
											while (fileters.containsKey(order)) {
												order--;
											}
										}
									}
									fileters.put(order,null == fileter?(Fileter)filterzz.newInstance():fileter);
									continue;
								}
							}
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException c) {
							
							logger.error("fileter load fial:" + filterzz,c);
							throw new ReportException("fileter load fial.[%s]",filterzz);
						}
					}else if(reportConfig.containsKey(key)){
						logger.info(key + " is covered, the original " + reportConfig.get(e.getKey()) + " cover for " + e.getValue() + " ");
					}
					reportConfig.put(key, value);
		    }
		}
	}
	public static void throwException(String message, Object...args){
		throw new ReportException(message, args);
	}
	public static void buildInvokerChain(){
		Object endInvokeClass = reportConfig.get(ReportConfigItem.END_INVOKE.getConfigName());
		if(null == endInvokeClass){
			throw new ReportException(ReportConfigItem.END_INVOKE.getErrorMsg());
		}
		List<Fileter> fileterz = new ArrayList<Fileter>();
		for(Map.Entry<Integer, Fileter>  fileter : fileters.entrySet()){
			fileterz.add(fileter.getValue());
		}
		Invoker startInvoke = buildInvokerChain((Invoker)ReportCabinet.getBean(endInvokeClass.toString()), fileterz);
		reportConfig.put(ReportConfigItem.START_INVOKE.getConfigName(), startInvoke);
	}
	private static Invoker buildInvokerChain(Invoker invoker, List<Fileter> fileters){
		
		Invoker last = invoker;
		
		for (int i = fileters.size() - 1; i >= 0; i--) {
			final Fileter fileter = fileters.get(i);
			final Invoker next = last;
			last = new Invoker() {
				
				@Override
				public ReportResponse invoke(HttpServletRequest request,
						Invocation invocation) throws Exception {
					
					return fileter.invoke(next, request, invocation);
				}
			};
			
		}
		return last;
	}
}
