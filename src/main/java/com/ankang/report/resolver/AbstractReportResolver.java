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
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import net.sf.json.xml.XMLSerializer;


import com.alibaba.fastjson.JSONObject;
import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;
import com.ankang.report.exception.ReportException;
import com.ankang.report.resolver.impl.XmlProtocolResolver;
import com.ankang.report.util.GsonUtil;

public abstract class AbstractReportResolver implements ReportResolver {
	
	private static final Logger logger = Logger.getLogger(AbstractReportResolver.class);
	
	protected JSONObject params = null;
	
	protected static String JSON_DUFALUE_BODY = "body";
	
	protected static String XML_DUFALUE_BODY = "root";
	
	private static final Map<String, JSONObject> sessionCache = new WeakHashMap<String, JSONObject>(16, 1);
	
	protected void resolverParamter(HttpServletRequest request, String key){
	
		if(null == request){
			return;
		}
		
		if(null != (params = sessionCache.get(request.getServletPath() + request.getQueryString() + key))){
			return;
		}
		
		String parameter = getParameter(request, key);
		
		if(null == parameter){
			return;
		}
		if(this.getClass().isAssignableFrom(XmlProtocolResolver.class)){
			XMLSerializer xs = new XMLSerializer();
			parameter = xs.read(parameter).toString();
		}
		params = JSONObject.parseObject(parameter);
		sessionCache.put(request.getServletPath() + request.getQueryString() + key, params);
		
	}
	
	
	private String getParameter(HttpServletRequest request, String key){
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
	@Override
	public final Object getValue(String param, Class<?> clazz){
		
		return this.params.getObject(param, clazz);
	}
	
	@Override
	public final Map<String, Object> getParamsMap(HttpServletRequest request) {
		
		resolverParamter(request);
		
		return GsonUtil.jsonToMap(params.toJSONString());
		
	}
	static{
		
		String jsonBody = (String)ReportConfig.getValue(ReportConfigItem.JSON_BOAY.getConfigName());
		
		if(StringUtils.isNotEmpty(jsonBody)){
			JSON_DUFALUE_BODY = jsonBody;
		}
		String xmlBody = (String)ReportConfig.getValue(ReportConfigItem.XML_BODY.getConfigName());
		
		if(StringUtils.isNotEmpty(xmlBody)){
			XML_DUFALUE_BODY = xmlBody;
		}
	}
}
