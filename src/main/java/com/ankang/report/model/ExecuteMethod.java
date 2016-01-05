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
package com.ankang.report.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;


@SuppressWarnings("all")
public class ExecuteMethod implements Serializable{


	private static final long serialVersionUID = 1L;
	private String moduleName;
	private Class clazz;
	
	private final List<Map<String, Method>> executeMethod = new ArrayList<Map<String,Method>>();//方法别名和方法类型 重复方法的处理办法
	private final List<Map<String, RequestMethod[]>> executeType = new ArrayList<Map<String,RequestMethod[]>>();//方法别名和请求类型
	private final List<Map<String, LinkedHashMap<String, Class<?>>>> parameterType = new ArrayList<Map<String,LinkedHashMap<String,Class<?>>>>(); //方法名称和参数名称类型
	private final List<Map<String, String>> executeDesc = new ArrayList<Map<String, String>>(); //方法名称和描述
	public ExecuteMethod() {}
	public ExecuteMethod(String moduleName, Class clazz) {
		super();
		this.moduleName = moduleName;
		this.clazz = clazz;
	}
	public Class getClazz() {
		return clazz;
	}
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public List<Map<String, Method>> getExecuteMethod() {
		return executeMethod;
	}
	public List<Map<String, RequestMethod[]>> getExecuteType() {
		return executeType;
	}
	public List<Map<String, LinkedHashMap<String, Class<?>>>> getParameterType() {
		return parameterType;
	}
	public List<Map<String, String>> getExecuteDesc() {
		return executeDesc;
	}
	
}
