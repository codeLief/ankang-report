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
package com.ankang.report.example;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

import com.ankang.report.annotation.Alias;
import com.ankang.report.annotation.HTTP;
import com.ankang.report.annotation.ReportParam;

@Alias(alias = "example")
public class Example {
	
	private static final Logger logger = Logger.getLogger(Example.class);
	
	/**
	 * 
		 * @Description: 系统测试 
	     * @author: qd-ankang
	     * @date: 2015-12-1 下午2:54:46
	     * @param testRequest 请求参数 自定义类型
	     * @param key 请求参数 常用类型
	     * @param request 请求参数，系统可封装类型 可以不加参数注解
	     * @return 返回参数
	 */
	@HTTP(value = "check", desc = "例子")
	public TestResponse check(@ReportParam(value = "testRequest") TestRequest testRequest, 
				@RequestParam(value = "key") Integer key, HttpServletRequest request){
		
		logger.info("key:"+ key);
		logger.info("testRequest:"+testRequest);
		logger.info("request:"+request);
		
		TestResponse response = new TestResponse();
		
		response.setAge(testRequest.getAge());
		response.setKey(key);
		response.setName(testRequest.getName());
		
		return response;
	}
}
