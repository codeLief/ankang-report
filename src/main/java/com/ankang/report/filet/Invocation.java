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
package com.ankang.report.filet;

import java.lang.reflect.Method;

import com.ankang.report.resolver.ReportResolver;

public class Invocation {
	
	private Class<?> modul;
	private Method method;
	private Object[] arguments;
	private Class<?>[] parameterTypes;
	private ReportResolver resolver;
	public Class<?> getModul() {
		return modul;
	}
	public void setModul(Class<?> modul) {
		this.modul = modul;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Object[] getArguments() {
		return arguments;
	}
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}
	public void setParameterTypes(Class<?>[] parameterTypes) {
		this.parameterTypes = parameterTypes;
	}
	public ReportResolver getResolver() {
		return resolver;
	}
	public void setResolver(ReportResolver resolver) {
		this.resolver = resolver;
	}
}
