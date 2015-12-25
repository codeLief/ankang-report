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
package com.ankang.report.start;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;
import com.ankang.report.example.Example;
import com.ankang.report.register.impl.MethodRegister;
import com.ankang.report.register.impl.MonitorRegister;
import com.ankang.report.register.impl.ResolverRegister;
import com.ankang.report.resolver.ReportResolver;

public abstract class ReportStart implements
		ApplicationListener<ContextRefreshedEvent> {

	private static final Logger logger = Logger.getLogger(ReportStart.class);

	private static final ResolverRegister rr = new ResolverRegister();

	private static final MethodRegister mr = new MethodRegister();

	private static final MonitorRegister or = new MonitorRegister();

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			logger.info("report initialization start");
			logger.info("=========================== report start ==============================");
			Long start = System.currentTimeMillis();
			initConfig();
			initPage();
			initRegisters();
			reginsterReport();
			reginsterResolver();
			supplement();
			logger.info("report initialization end. time consuming：" + (System.currentTimeMillis() - start));
			logger.info("=========================== report end ==============================");
		}
	}

	private void initConfig() {
		ReportConfig.loadReportConfig(null);
		ReportConfig.loadReportConfig(ReportConfig.REPORT_CONFIG);
		ReportConfig.buildInvokerChain();
	}

	private void supplement() {
		ReportConfig.dynamicLoadConfig(
				ReportConfigItem.REPORT_ISLOAD.getConfigName(), Boolean.TRUE);
		or.makeMonitor();
	}

	private void initRegisters() {
		rr.reginster();
		mr.reginster();
		or.reginster();
		reginster(Example.class);
	}

	/**
	 * 
	 * @Description: 注册需要执行的api
	 * @author: ankang
	 * @date: 2015-10-26 上午11:16:42
	 */
	protected abstract void reginsterReport();

	/**
	 * 
	 * @Description: 注册自定义解析器
	 * @author: ankang
	 * @date: 2015-10-26 上午11:17:07
	 */
	protected abstract void reginsterResolver();

	protected final void reginster(Class<?> clazz) {
		if (null == clazz) {
			logger.warn("reginster object is null");
			return;
		}
		if (ReportResolver.class.isAssignableFrom(clazz)) {
			logger.info("reginster resolver.[" + clazz.getSimpleName() + "]");
			rr.configer(clazz);
		} else {
			logger.info("reginster api.[" + clazz.getSimpleName() + "]");
			mr.configer(clazz);
			or.configer(clazz);
		}
	}

	private void initPage() {

		URL url = this.getClass().getClassLoader().getResource("/");

		String path = url.toString().split("file:/")[1];

		String sourcePath = path + "report";

		String targetPath = "";

		if (path.contains("WEB-INF")) {
			targetPath = path.split("WEB-INF")[0];
		}

		try {
			if (ReportConfig.isTrue(ReportConfigItem.REPORT_SWITCH.getConfigName())) {
				copyFile(new File(sourcePath), new File(targetPath));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void copyFile(File fileSource, File fileTarget) {

		if (fileSource.isDirectory()) {

			String pathSource = fileSource.getPath().toString();
			String newPath = pathSource.substring(pathSource.lastIndexOf("\\"));

			String pathTarget = fileTarget.getPath().toString();
			File nextFile = new File(pathTarget + "//" + newPath);

			if (!nextFile.exists()) {
				fileTarget.mkdir();
			}
			File[] listFiles = fileSource.listFiles();
			for (File file : listFiles) {
				copyFile(file, nextFile);
			}
		} else {

			FileOutputStream out = null;
			FileInputStream input = null;
			BufferedInputStream inbuff = null;
			BufferedOutputStream outbuff = null;
			try {
				File temp = new File(fileTarget.getPath().toString() + "//"
						+ fileSource.getName());

				if (!temp.getParentFile().exists()) {
					temp.getParentFile().mkdir();
				}

				out = new FileOutputStream(temp);

				input = new FileInputStream(fileSource);
				inbuff = new BufferedInputStream(input);

				outbuff = new BufferedOutputStream(out);

				byte[] b = new byte[1024 * 5];
				int len = 0;
				while ((len = inbuff.read(b)) != -1) {
					outbuff.write(b, 0, len);
				}
				outbuff.flush();

			} catch (IOException e) {

				logger.error("File upload fail.[" + fileSource.getName() + "]",
						e);
				e.printStackTrace();
			} finally {
				if (null != inbuff) {
					try {
						inbuff.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != outbuff) {
					try {
						inbuff.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != out) {
					try {
						inbuff.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != input) {
					try {
						inbuff.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
