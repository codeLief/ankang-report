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
package com.ankang.report.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.ankang.report.annotation.Activate;
import com.ankang.report.exception.ReportException;
import com.ankang.report.filet.Fileter;
import com.ankang.report.filet.Invocation;
import com.ankang.report.filet.Invoker;
import com.ankang.report.main.ReportCabinet;
import com.ankang.report.model.ReportResponse;

/**
 * 
 * @Description: report配置类，
 *               report.properties为默认配置文件，report-config.properties使用配置文件，后者会覆盖前者
 * @author: ankang
 * @date: 2015-10-1 下午10:02:24
 */
@SuppressWarnings("all")
public final class ReportConfig {

	private static final Logger logger = Logger.getLogger(ReportConfig.class);

	private static final String REPORT = "config/report.properties";// 主配置文件
	public static final String REPORT_CONFIG = "report/report-config.properties";// 用户配置文件
	private static final Map<String, Object> REPORTCONFIG = Collections
			.synchronizedMap(new HashMap<String, Object>());
	private static final Map<Integer, Fileter> FILETERS = new TreeMap<Integer, Fileter>();
	private static final Properties ps = new Properties();
	private static boolean isLoad = false;

	public static void loadReportConfig(String classPath) {

		if ((null == classPath || REPORT.equals(classPath)) && !isLoad) {

			classPath = REPORT;
			isLoad = Boolean.TRUE;
		}
		logger.info("load properties " + classPath);

		ClassPathResource cp = new ClassPathResource(classPath);

		try {
			if (cp.exists()) {
				ps.load(cp.getInputStream());
				convertMap((Map) ps);
				ps.clear();
			}
		} catch (IOException e) {
			throw new ReportException("File read exception file.[%s]",
					classPath);
		}

	}

	public static Object getValue(String key) {
		if (null == key) {
			throwException("key is null");
		}
		key = key.toLowerCase();
		if (!REPORTCONFIG.containsKey(key)) {
			return null;
		}
		return REPORTCONFIG.get(key);
	}

	public static boolean containsKey(String key, Object value, boolean isCover) {

		boolean flag = false;
		if (isCover) {
			if ((flag = REPORTCONFIG.containsKey(key))) {
				logger.info(String.format(
						"[%s] is covered, the original [%s] cover for [%s]",
						key, REPORTCONFIG.get(key), value));
			}
			REPORTCONFIG.put(key, value);
		}
		return flag;
	}

	public static boolean isTrue(String key) {

		Object value = getValue(key);
		if (null == value) {
			return Boolean.FALSE;
		}
		return Boolean.parseBoolean(value.toString());
	}

	public static boolean dynamicLoadConfig(String key, Object value) {
		if (null == key || null == value) {
			return Boolean.FALSE;
		}
		logger.info(String.format(
				"Dynamic loading config key: [%s] value: [%s]", key, value));
		return containsKey(key, value, true);
	}

	public static boolean containsKey(String key) {
		if (null == key) {
			logger.error("key is null");
			return Boolean.FALSE;
		}
		return REPORTCONFIG.containsKey(key);
	}

	private static <K, V> void convertMap(Map<? extends K, ? extends V> m) {
		if (null == m) {
			logger.error("map is null");
			return;
		}

		synchronized (FILETERS) {
			for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {

				String key = e.getKey().toString().toLowerCase();

				Object value = e.getValue();

				if (ReportConfigItem.FILETER.getConfigName().equalsIgnoreCase(
						key)) {

					Class<?> filterzz = null;
					Fileter fileter = null;
					String[] clazzs = null;
					int i = 0;
					try {
						
						if (value != null && value.toString().contains(",")) {
							clazzs = value.toString().trim().split(",");
						}
						for (; i < clazzs.length; i++) {
							filterzz = Class.forName(clazzs[i]);
							Activate activate = filterzz
									.getAnnotation(Activate.class);
							if (!FILETERS
									.containsValue((fileter = (Fileter) ReportCabinet
											.getBean(filterzz)))) {
								Integer order = activate != null
										&& activate.order() >= Integer.MIN_VALUE
										&& activate.order() <= Integer.MAX_VALUE ? activate
										.order() : Integer.MIN_VALUE;
								if (FILETERS.containsKey(order)) { // 以下两次判断是否存在key，避免临界值死循环,避免临界值越界
									if (order < (Integer.MAX_VALUE >> 1) + 1) {
										while (FILETERS.containsKey(order)) {
											order++;
										}
									} else if (order > (Integer.MAX_VALUE >> 1)) {
										while (FILETERS.containsKey(order)) {
											order--;
										}
									}
								}
								FILETERS.put(order, fileter);
								logger.debug("load fileter.["+ filterzz +"]");
								continue;
							}
						}
					} catch (ClassNotFoundException c) {

						logger.error("fileter load fial:" + clazzs[i], c);
						throw new ReportException("fileter load fial.[%s]",
								clazzs[i]);
					}
				} else {
					containsKey(key, value, true);
				}
			}
		}
	}

	public static void throwException(String message, Object... args) {
		throw new ReportException(message, args);
	}

	public static void buildInvokerChain() {
		Object endInvokeClass = REPORTCONFIG.get(ReportConfigItem.END_INVOKE
				.getConfigName());
		if (null == endInvokeClass
				|| StringUtils.isEmpty(endInvokeClass.toString())) {
			throw new ReportException(ReportConfigItem.END_INVOKE.getErrorMsg());
		}
		List<Fileter> fileterz = new ArrayList<Fileter>();
		for (Map.Entry<Integer, Fileter> fileter : FILETERS.entrySet()) {
			fileterz.add(fileter.getValue());
		}
		Invoker startInvoke = buildInvokerChain(
				(Invoker) ReportCabinet.getBean(endInvokeClass.toString()),
				fileterz);
		REPORTCONFIG.put(ReportConfigItem.START_INVOKE.getConfigName(),
				startInvoke);
	}

	private static Invoker buildInvokerChain(Invoker invoker,
			List<Fileter> fileters) {

		Invoker last = invoker;

		for (int i = fileters.size() - 1; i >= 0; i--) {
			final Fileter fileter = fileters.get(i);
			final Invoker next = last;
			last = new Invoker() {

				@Override
				public ReportResponse invoke(HttpServletRequest request,
						Invocation invocation) throws Exception {

					return fileter.invoke(next, request, invocation);
				}
			};

		}
		return last;
	}
}
