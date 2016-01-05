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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;




import com.ankang.report.annotation.Alias;
import com.ankang.report.exception.ReportException;
import com.ankang.report.main.ReportCabinet;
import com.ankang.report.pool.AbstractReportAliasPool;
import com.ankang.report.register.ReportRegister;
import com.ankang.report.resolver.ReportResolver;
import com.ankang.report.util.ReportUtil;

@SuppressWarnings("all")
public final class ResolverRegister extends AbstractReportAliasPool implements ReportRegister{
	
	private static final Logger logger = Logger.getLogger(ResolverRegister.class);
	
	public static final String POOL_ALIAS_NAME = "resolverPool";
	private static final Map<String, Class<?>> RESOLVERPOOL = new HashMap<String, Class<?>>();
	
	@Override
	public boolean reginster() {
		List<Class<?>> resolverAll = ReportUtil.getAllClassByInterface(ReportResolver.class);
		for (Class<?> clazz : resolverAll) {
			configer(clazz);
		}
		ReportCabinet.getReportApplicationContext().putPool(POOL_ALIAS_NAME, RESOLVERPOOL);
		return Boolean.TRUE;
	}
	@Override
	public void configer(Class<?> clazz){
		if(null == clazz){
			return;
		}
		if(!ReportResolver.class.isAssignableFrom(clazz)){
			throw new ReportException("Is not a registered subclass.[%s]", clazz);
		}
		Alias annoName = clazz.getAnnotation(Alias.class);
		if(null == annoName || "".equals(annoName.alias())){
			logger.warn("Not annotated alias");
			return;
		}
		synchronized (RESOLVERPOOL) {
			setMountPool(RESOLVERPOOL);
			mount(annoName.alias(), clazz);
		}
	}
	@Override
	protected void setMountPool(Map waitMap){
		this.waitMap = waitMap;
	}
	
}
