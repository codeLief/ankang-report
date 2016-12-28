package com.ankang.report.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;

public class ReportInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = Logger
			.getLogger(ReportInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if (!ReportConfig
				.isTrue(ReportConfigItem.REPORT_ISLOAD.getConfigName())) {
			return printErrorMsg(ReportConfigItem.REPORT_ISLOAD.getErrorMsg(), response);
		} else if (!ReportConfig.isTrue(ReportConfigItem.REPORT_SWITCH
				.getConfigName()) && request.getRequestURI().endsWith("/report/console")) {
			return printErrorMsg(ReportConfigItem.REPORT_SWITCH.getErrorMsg(), response);
		}
		return Boolean.TRUE;
	}

	private boolean printErrorMsg(String errorMsg, HttpServletResponse response) throws IOException {

		logger.error(errorMsg);
		String page = "<html lang=\"zh\">"
				+ "<head>"
				+ "<title>report error</title>"
				+ "</head>"
				+ "<style type=\"text/css\"> body{background-color:#EEEEEE;} </style>"
				+ "<body>" + "<h1 align=\"center\"> "+ errorMsg +" </h1>"
				+ "<h3 align=\"center\"> "
				+ "  <a href=\"https://github.com/codeLief/ankang-report\" target=\"view_window\"> 查看手册 </a> </h3>"
				+ "</body>" + "</html>";

		response.setContentType("text/html;charset=utf-8");
		response.getWriter().print(page);
		return Boolean.FALSE;
	}
}
