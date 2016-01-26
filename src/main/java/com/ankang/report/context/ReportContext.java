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

import java.util.Map;

public abstract interface ReportContext{
	

	Map<String, Object> getPool(String alias);
	
	boolean putPool(String alias, Map<String, Class<?>> aliasPool);
	
	Object getBean(String bean);
	
	Object getBean(Class<?> beanClass);
	
	boolean putBean(Object bean);
}
