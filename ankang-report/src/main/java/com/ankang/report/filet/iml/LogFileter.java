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
package com.ankang.report.filet.iml;

import java.util.Arrays;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ankang.report.annotation.Activate;
import com.ankang.report.filet.Fileter;
import com.ankang.report.filet.Invocation;
import com.ankang.report.filet.Invoker;
import com.ankang.report.model.ReportResponse;

@SuppressWarnings("all")
@Activate(order = Integer.MAX_VALUE - 2)
public class LogFileter implements Fileter{
	
	private static final Logger logger = Logger.getLogger(LogFileter.class);
	
	@Override
	public ReportResponse invoke(Invoker invoker, HttpServletRequest request,
			Invocation invocation) throws Exception {
		
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<String> keySet = parameterMap.keySet();
		String uri = request.getRequestURI();
		StringBuffer toString = new StringBuffer(400);
		for (String key : keySet) {
			toString.append(key).append("=")
					.append(Arrays.toString(parameterMap.get(key))).append(",");
		}

		logger.info("request [" + uri + " param : "
				+ toString.toString() + "]");

		ReportResponse response = invoker.invoke(request, invocation);
		
		logger.info("response [" + uri + " param : "
				+ invocation.getResolver().out(response) + "]");
		
		return response;
	}
}
