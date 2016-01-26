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

public enum ReportConfigItem {
	
	REPORT_SWITCH("report开关,true/false", "report_switch", "report为关闭状态"),
	REPORT_ISLOAD("report配置文件是否加载 true/false", "report_isload", "配置文件未加载，请阅读report使用手册"),
	FILETER("fileter,class", "fileter", "过滤器"),
	
	JSON_BOAY("json参数传递主体","json_body", "没配置默认json消息主体，将使用默认主体"),
	XML_BODY("xml参数传递主体","xml_body", "没配置默认xml消息主体，将使用默认主体"),
	
	MONITOR_FILE_PATH("统计数据保存地址", "monitor_file_path", "没有找到report.cc"),
	
	END_INVOKE("最终invoke对象", "end_invoke","没有最终执行对象"),
	
	START_INVOKE("第一个invoke对象", "start_invoke","没有找到有效的链开始"),
	
	IS_DEFINED_RESPONSE("是否自定义返回体,true/false", "is_defined_response", "不使用自定义返回体"),
	RESPONSE_CODE("自定义返回值code", "response_code", "未找到该属性"),
	RESPONSE_MESSAGE("自定义返回值消息", "response_message", "未找到返回值消息"),
	RESPONSE_RESULT("自定义返回值实体", "response_result", "未找到返回值实体");
	
	private String desc;
	private String configName;
	private String errorMsg;
	
	private ReportConfigItem(String desc, String configName, String errorMsg) {
		this.desc = desc;
		this.configName = configName;
		this.errorMsg = errorMsg;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
