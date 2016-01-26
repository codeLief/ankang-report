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
package com.ankang.report.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMethod;



/**
 * 
 * @Description: 只可用在public方法上，表明此方法可被外部接口调用
 * @author: ankang
 * @date: 2015-9-29 上午10:51:10
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HTTP {
	
	/**
	 * 调用方法别名
	 * @return
	 */
	String value();

	/**
	 * 
		 * @Description: 方法描述
	 */
	String desc() default "";
	
	/**
	 * 支持的http请求方式
	 * @return
	 */
	RequestMethod[] supportMethod() default {RequestMethod.GET, RequestMethod.POST};
	
}
