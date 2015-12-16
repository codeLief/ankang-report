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
package com.ankang.report.main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;
import com.ankang.report.enumz.ReportStatus;
import com.ankang.report.exception.ReportException;
import com.ankang.report.main.handler.impl.ReportRequestHandler;
import com.ankang.report.model.ExecuteMethod;
import com.ankang.report.model.MonitorView;
import com.ankang.report.model.ReportResponse;
import com.ankang.report.register.impl.MethodRegister;
import com.ankang.report.resolver.ReportResolver;

@Controller
@RequestMapping(value = "/report")
public class Report extends ReportRequestHandler {

	private static final Logger logger = Logger.getLogger(Report.class);

	@RequestMapping(value = "{resolverAlias}/{serviceAlias}/{methodAlias}")
	public void report(HttpServletRequest request,
			HttpServletResponse response,

			@PathVariable(value = "resolverAlias") String resolverAlias,
			@PathVariable(value = "serviceAlias") String serviceAlias,
			@PathVariable(value = "methodAlias") String methodAlias)
			throws IOException {

		ReportResolver resolver = null;
		ReportResponse reportResponse = new ReportResponse();
		try {
			
			resolver = match(resolverAlias);
			
			reportResponse = handler(serviceAlias, methodAlias, resolver, request);
			
		} catch (IllegalAccessException e) {
			logger.error("服务器繁忙", e);
			reportResponse.setCode(ReportStatus.FAIL_CODE);
			reportResponse.setMessage(e.getMessage() != null?e.getMessage():"服务器繁忙");
		} catch (IllegalArgumentException e) {
			logger.error("请求参数异常", e);
			reportResponse.setCode(ReportStatus.FAIL_CODE);
			reportResponse.setMessage(e.getMessage() != null?e.getMessage():"请求参数异常");
		} catch (InvocationTargetException e) {
			logger.error("找不到目标方法", e);
			reportResponse.setCode(ReportStatus.FAIL_CODE);
			reportResponse.setMessage(e.getMessage() != null?e.getMessage():"找不到目标方法");
		} catch (ReportException e) {
			logger.error(e.getMessage(), e);
			reportResponse.setCode(ReportStatus.FAIL_CODE);
			reportResponse.setMessage(e.getMessage());
		} catch (Exception e) {
			logger.error("请求失败", e);
			reportResponse.setCode(ReportStatus.FAIL_CODE);
			reportResponse.setMessage(e.getMessage() != null?e.getMessage():"请求失败");
		}

		response.setCharacterEncoding("utf-8");
		response.setContentType(resolver.getContextType());
		response.getWriter().print(resolver.out(reportResponse));
	}

	@RequestMapping(value = {"/console",""})
	public ModelAndView test(HttpServletResponse response) throws Exception {
		if (!Boolean.parseBoolean(ReportConfig.getValue(
				ReportConfigItem.REPORT_SWITCH.getConfigName()).toString())) {

			return new ModelAndView("report/error.html");
		}

		return new ModelAndView("report/index.html");
	}

	@RequestMapping(value = "get")
	public void get(HttpServletResponse response, String modulName)
			throws IOException {

		if (Boolean.parseBoolean(ReportConfig.getValue(
				ReportConfigItem.REPORT_SWITCH.getConfigName()).toString())) {
			Map<String, Object> methodPools = getReportApplicationContext()
					.getPool(MethodRegister.METHOD_ALIAS_NAME);
			JSONArray json = new JSONArray();

			List<ExecuteMethod> mothods = new ArrayList<ExecuteMethod>();

			// 简介
			if (StringUtils.isEmpty(modulName)) {

				for (Map.Entry<String, Object> pool : methodPools.entrySet()) {
					mothods.add(((ExecuteMethod) pool.getValue()));
				}
				json.addAll(mothods);
				// 详情
			} else if (StringUtils.isNotEmpty(modulName)) {

				mothods.add((ExecuteMethod) methodPools.get(modulName));
				json.addAll(mothods);
			}
			response.setCharacterEncoding("utf-8");
			response.getWriter().print(json);
		}
	}

	@RequestMapping(value = "showReport")
	public void showReport(String modulName, String methodName,
			HttpServletResponse response) throws IOException {

		if (Boolean.parseBoolean(ReportConfig.getValue(
				ReportConfigItem.REPORT_SWITCH.getConfigName()).toString())) {
			MonitorView monitorView = matchMonitorView(modulName, methodName);
			monitorView.setModul(modulName);
			monitorView.setMethod(methodName);
			
			JSONArray json = new JSONArray();

			json.add(monitorView);

			response.setCharacterEncoding("utf-8");
			response.getWriter().print(json);
		}
	}
}
