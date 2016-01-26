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
package com.ankang.report.main.handler.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

import com.ankang.report.annotation.ReportParam;
import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;
import com.ankang.report.exception.ReportException;
import com.ankang.report.filet.Invocation;
import com.ankang.report.filet.Invoker;
import com.ankang.report.main.ReportCabinet;
import com.ankang.report.main.handler.ReportHandler;
import com.ankang.report.model.ReportRequest;
import com.ankang.report.model.ReportResponse;
import com.ankang.report.resolver.ReportResolver;

public class ReportRequestHandler extends ReportCabinet implements
		ReportHandler, Invoker {

	private static final Logger logger = Logger
			.getLogger(ReportRequestHandler.class);

	@Override
	public ReportResponse handler(String serviceAlias, String methodAlias,
			ReportResolver resolver, HttpServletRequest request)
			throws Exception {

		Invocation invocation = new Invocation();

		Class<?> module = matchModule(serviceAlias);
		if (null == module) {
			logger.error("找不到请求的服务类." + serviceAlias);
			throw new ReportException("找不到请求的服务类.[%s]", serviceAlias);
		}
		invocation.setModul(module);

		Method method = match(serviceAlias, methodAlias);
		if (null == method) {
			logger.error("没有找到可执行的有效方法." + methodAlias);
			throw new ReportException("没有找到可执行的有效方法.[%s]", methodAlias);
		}
		invocation.setMethod(method);
		invocation.setResolver(resolver);
		invocation.setParameterTypes(method.getParameterTypes());

		Invoker startInvoke = (Invoker) ReportConfig
				.getValue(ReportConfigItem.START_INVOKE.getConfigName());

		return startInvoke.invoke(request, invocation);
	}

	@Override
	public ReportResponse invoke(HttpServletRequest request,
			Invocation invocation) throws Exception {

		Class<?>[] parameterTypes = invocation.getParameterTypes();
		Object[] invokerParam = invocation.getArguments();
		if (parameterTypes.length > 0) {
			
			invocation.getResolver().resolverParamter(request);

			for (int i = 0; i < parameterTypes.length && invokerParam[i] == null; i++) {

				Annotation[][] parameters = invocation.getMethod()
						.getParameterAnnotations();
				Annotation annotation = null;

				if (parameters.length > i && parameters[i].length > 0) {

					annotation = parameters[i][0];
				}

				Object para = null;

				if (annotation instanceof ReportParam) {

					ReportParam reportPara = (ReportParam) annotation;

					para = reportPara.value();

				}
				if (annotation instanceof RequestParam) {

					RequestParam reportPara = (RequestParam) annotation;

					para = reportPara.value();

				}

				if (null == parameterTypes[i]) {
					throw new IllegalArgumentException("获取参数失败，没有目标参数类型");
				}
				if ((ReportRequest.class
						.isAssignableFrom(parameterTypes[i]))
						|| (!parameterTypes[i].isPrimitive()
								&& !parameterTypes[i].toString().matches(
										"^.+java\\..+$") && parameterTypes[i]
								.toString().matches("^class.+$"))) {

					invokerParam[i] = invocation.getResolver().in(null,
							parameterTypes[i]);
					continue;
				} else if (null == para) {

					throw new IllegalArgumentException("获取参数失败，未找到有效的参数名");
				} else {

					invokerParam[i] = invocation.getResolver().getValue(
							para.toString(), parameterTypes[i]);
				}
			}
		}

		Object result = invocation.getMethod().invoke(
				getBean(invocation.getModul()), invocation.getArguments());

		ReportResponse response = new ReportResponse();
		response.setResponse(result);

		return response;
	}

}
