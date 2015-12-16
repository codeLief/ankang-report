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
package com.ankang.report.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ankang.report.exception.ReportException;
import com.ankang.report.factory.ReportBeanFactory;
import com.ankang.report.pool.AbstractReportAliasPool;


@SuppressWarnings("all")
public final class ReportApplicationContext extends AbstractReportAliasPool implements ReportContext {
	
	private static final Logger logger = Logger.getLogger(ReportApplicationContext.class);
	
	private static final Map<String, Object> reportThreadLocal = new HashMap<String, Object>();
	
	private static final Map<String, Map> aliasPools = new TreeMap<String, Map>();
	
	private static final Map<String, Object> beanPools = Collections.synchronizedMap(new HashMap<String, Object> ());
	
	private ReportBeanFactory beanFactory = null;
	private ReportApplicationContext(ReportBeanFactory beanFactory){
		this.beanFactory = beanFactory;
	};
	@Override
	public Map getPool(String alias) {
		if(StringUtils.isEmpty(alias)){
			return new HashMap<>();
		}
		return aliasPools.get(alias);
	}
	
	@Override
	public boolean putPool(String alias, Map aliasPool) {
		
		if(StringUtils.isEmpty(alias)){
			return false;
		}
		synchronized (this) {
			setMountPool(aliasPools);
			return mount(alias, aliasPool);
		}
	}
	private static ReportContext rc = null;
	public static ReportContext getInstance(ReportBeanFactory factory){
		
		if(null == rc){
			synchronized (ReportApplicationContext.class) {
				if(null == rc){
					rc = new ReportApplicationContext(factory);
				}
			}
		}
		return rc;
	}
	public Map<String, Object> getReportThreadLocal() {
		return reportThreadLocal;
	}
	@Override
	protected void setMountPool(Map waitMap) {
		this.waitMap = waitMap;
	}
	@Override
	public Object getBean(String beanAlias) {
		if(null == beanAlias){
			return null;
		}
		Object bean = beanPools.get(beanAlias);
		try {
			return null == bean?getBean(getClass(beanAlias)):bean;
			
		} catch (ClassNotFoundException e) {
			logger.error("invalid bean.[" + beanAlias + "]");
			throw new ReportException("invalid bean.[%s]",beanAlias);
		}
	}
	@Override
	public Object getBean(Class<?> beanClass) {
		if(null == beanClass){
			return null;
		}
		Object bean = beanPools.get(getBeanAlias(beanClass));
		
		return null == bean?beanFactory.createBean(beanClass):bean;
	}
	@Override
	public boolean putBean(Object bean) {
		if(bean == null){
			return false;
		}
		
		String baneName = getBeanAlias(bean.getClass());
		synchronized (this) {
			setMountPool(beanPools);
			mount(baneName, bean);
			mount(bean.getClass().toString(), bean);
		}
		return true;
	}
	protected String getBeanAlias(Class<?> clazz){
		if(null == clazz){
			return null;
		}
		String baneName = clazz.getSimpleName();
		return baneName.replaceFirst(baneName.substring(0, 1), 
						baneName.substring(0, 1).toLowerCase());
	}
	protected Class<?> getClass(String clazz) throws ClassNotFoundException{
		if(StringUtils.isNotEmpty(clazz) && clazz.contains(" ") || clazz.startsWith("class")){
			return Class.forName(clazz.split("\\s+")[1].trim());
		}
		return Class.forName(clazz);
	}
}
