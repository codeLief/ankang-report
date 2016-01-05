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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * @Description: 参数解析器 
 * @author: ankang
 * @date: 2015-9-28 上午11:32:42
 */
public abstract interface ReportResolver {
	
	public void resolverParamter(HttpServletRequest request);
	
	public <IN> IN in(String input, Class<IN> targerCalss);
	
	public <OUT> String out(OUT output);
	
	public String error(int code, String reportMessage);
	
	public String getContextType();
	
	public Object getValue(String param, Class<?> clazz);
	
	public Map<String, Object> getParamsMap(HttpServletRequest request);
}
