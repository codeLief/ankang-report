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
package com.ankang.report.resolver;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;


import com.alibaba.fastjson.JSONObject;
import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;
import com.ankang.report.exception.ReportException;

public abstract class AbstractReportResolver implements ReportResolver {
	
	private static final Logger logger = Logger.getLogger(AbstractReportResolver.class);
	
	protected JSONObject params = new JSONObject();
	
	private static final String JSON_DUFALUE_BODY = "body";
	
	private static final String XML_DUFALUE_BODY = "root";

	protected final void JsonResolverParamter(HttpServletRequest request){
		
		if(null == request){
			return;
		}
		
		String jsonBody = (String)ReportConfig.getValue(ReportConfigItem.JSON_BOAY.getConfigName());
		
		String key = null == jsonBody? JSON_DUFALUE_BODY : jsonBody;
		
		String parameter = getParameter(request, key);
		
		params = JSONObject.parseObject(parameter);
		
		
	}
	protected final void xmlResolverParamter(HttpServletRequest request){
		if(null == request){
			return;
		}
		
		String xmlBody = (String)ReportConfig.getValue(ReportConfigItem.XML_BODY.getConfigName());
		
		String key = null == xmlBody?XML_DUFALUE_BODY : xmlBody;
		
		String parameter = getParameter(request, key);
		if(null == parameter){
			return;
		}
		XMLSerializer xs = new XMLSerializer();
		JSON json = xs.read(parameter);
		
		params = JSONObject.parseObject(json.toString());
		
	}
	private final String getParameter(HttpServletRequest request, String key){
		if(null == key){
			logger.error("Is not an effective parameter key");
			throw new ReportException("Is not an effective parameter key");
		}
		try {
			String parameter = request.getParameter(key);
			if(StringUtils.isEmpty(parameter)){
				Object object = request.getAttribute(key);
				if(null == object){
					logger.error("Not get the parameters subject");
					throw new ReportException("Not get the parameters subject");
				}
				parameter = object.toString();
			}
			return new String(parameter.getBytes("iso-8859-1"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("request encode fial", e);
			throw new ReportException("request encode fial");
		}
	}
	public final Object getValue(String param, Class<?> clazz){
		
		return this.params.getObject(param, clazz);
	}
}
