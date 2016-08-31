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
package com.ankang.report.filet.iml;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.ankang.report.annotation.Activate;
import com.ankang.report.enumz.ReportStatus;
import com.ankang.report.filet.Fileter;
import com.ankang.report.filet.Invocation;
import com.ankang.report.filet.Invoker;
import com.ankang.report.model.ReportResponse;
import com.ankang.report.register.impl.MonitorRegister;

@Activate(order = Integer.MAX_VALUE - 1)
public final class MonitorFileter implements Fileter {

	private static final Logger logger = Logger.getLogger(MonitorFileter.class);
	private static final MonitorRegister mr = new MonitorRegister();
	private static final String TT = "\t\t";
	@Override
	public ReportResponse invoke(Invoker invoker, HttpServletRequest request,
			Invocation invocation) throws Exception {

		String url = request.getRequestURI();
		String[] split = url.split("/");
		Long startTime = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer(100);
		String modul = split[split.length - 2];
		sb.append(split[split.length - 2]).append(TT)
				.append(split[split.length - 1]).append(TT);

		ReportResponse response = null;
		try {
			response = invoker.invoke(request, invocation);

			byte num = 0;
			if (ReportStatus.SUCCESS_CODE == response.getCode()) {
				num = 1;
			} else {
				num = 0;
			}

			sb.append(0 ^ num).append(TT).append(1 ^ num).append(TT);
			appendTread(sb, startTime, modul);

		} catch (Exception e) {
			sb.append("0").append(TT).append("1").append(TT);
			appendTread(sb, startTime, modul);
			throw e;
		}

		return response;
	}

	private void appendTread(final StringBuffer sb, final Long startTime,
			final String modul) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				Long timeMillis = System.currentTimeMillis();

				sb.append(startTime).append(TT).append(timeMillis)
						.append(TT).append(timeMillis - startTime);

				logger.debug(sb.toString());

				mr.append(Arrays.asList(sb.toString()), Arrays.asList(modul), true);
			}
		}).start();
	}
}
