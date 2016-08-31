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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ankang.report.exception.ReportException;
import com.ankang.report.factory.ReportBeanFactory;
import com.ankang.report.pool.AbstractReportAliasPool;


@SuppressWarnings("all")
public final class ReportApplicationContext extends AbstractReportAliasPool implements ReportContext {
	
	private static final Logger logger = Logger.getLogger(ReportApplicationContext.class);
	
	private static final Map<String, Map> ALIASPOOLS = Collections.synchronizedMap(new TreeMap<String, Map>());
	
	private static final Map<String, Object> BEANPOOLS = Collections.synchronizedMap(new HashMap<String, Object> ());
	
	private HttpServletResponse httpServletResponse;
	
	private HttpServletRequest httpServletRequest;
	
	private ReportBeanFactory beanFactory = null;
	private ReportApplicationContext(ReportBeanFactory beanFactory){
		this.beanFactory = beanFactory;
	};
	@Override
	public Map getPool(String alias) {
		if(StringUtils.isEmpty(alias)){
			return new HashMap<>();
		}
		return ALIASPOOLS.get(alias);
	}
	
	@Override
	public boolean putPool(String alias, Map aliasPool) {
		setMountPool(ALIASPOOLS);
		return mount(alias, aliasPool);
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

	@Override
	protected void setMountPool(Map waitMap) {
		this.waitMap = waitMap;
	}
	@Override
	public Object getBean(String beanAlias) {
		if(null == beanAlias){
			throw new ReportException("beanAlias can't be empty");
		}
		Object bean = BEANPOOLS.get(beanAlias);
		try {
			return null == bean?getBean(getClass(beanAlias)):bean;
			
		} catch (ClassNotFoundException e) {
			logger.error("invalid bean.[" + beanAlias + "]", e);
			throw new ReportException("invalid bean.[%s]", beanAlias);
		}
	}
	@Override
	public Object getBean(Class<?> beanClass) {
		if(null == beanClass){
			throw new ReportException("beanClass can't be empty");
		}
		Object bean = BEANPOOLS.get(getBeanAlias(beanClass));
		
		return null == bean?beanFactory.createBean(beanClass):bean;
	}
	@Override
	public boolean putBean(Object bean) {
		if(bean == null){
			return Boolean.FALSE;
		}
		String baneName = getBeanAlias(bean.getClass());
		
		setMountPool(BEANPOOLS);
		mount(baneName, bean);
		mount(bean.getClass().toString(), bean);
		return Boolean.TRUE;
	}
	protected String getBeanAlias(Class<?> clazz){
		if(null == clazz){
			return "";
		}
		String baneName = clazz.getSimpleName();
		return baneName.replace(String.valueOf(baneName.charAt(0)), 
				String.valueOf(baneName.charAt(0)).toLowerCase());
	}
	protected Class<?> getClass(String clazz) throws ClassNotFoundException{
		if(StringUtils.isNotEmpty(clazz) && clazz.trim().startsWith("class ")){
			return Class.forName(clazz.split("\\s+")[1].trim());
		}
		return Class.forName(clazz);
	}
	public HttpServletResponse getHttpServletResponse() {
		return httpServletResponse;
	}
	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}
}
