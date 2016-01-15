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
package com.ankang.report.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * @Description: 获取springBean 需要在spring容易注入该实现类
 * @author: ankang
 * @date: 2015-8-3 下午6:06:48
 */
@Component
public class BeanUtil implements ApplicationContextAware{

	 private static ApplicationContext context = null;
	 public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
		 context = applicationContext;
	 }

	 public synchronized static Object getBean(String beanName) {
		 try {
			if(StringUtils.isEmpty(beanName)){
				return null;
			}
			return context.getBean(beanName);
		} catch (BeansException e) {
			 return null;
		}
		
	 }
	 public synchronized static Object getBean(Class<?> beanCalzz){
		 return context.getBeansOfType(beanCalzz);
	 }
}
