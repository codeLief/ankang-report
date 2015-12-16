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
package com.ankang.report.enumz;

public enum HttpMethod {

	POST("post"),
	GET("get");
	
	private final String method;
	
	HttpMethod(String method) {
		this.method = method;
	}
	
	public boolean is(String method) {
		return this.method.equalsIgnoreCase(method);
	}
}
