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


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.ankang.report.annotation.Activate;
import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;
import com.ankang.report.filet.Fileter;
import com.ankang.report.filet.Invocation;
import com.ankang.report.filet.Invoker;
import com.ankang.report.model.ReportResponse;
import com.ankang.report.util.BeanAUtils;

/**
 * 
 * @Description: response转换
 * @author: ankang
 * @date: 2015-12-14 下午4:46:16
 */
@Activate(order = Integer.MAX_VALUE)
public final class AttributeConverFileter implements Fileter {

	@Override
	public ReportResponse invoke(Invoker invoker, HttpServletRequest request,
			Invocation invocation) throws Exception {

		final ReportResponse response = invoker.invoke(request, invocation);

		final Object IS_DEFINED_RESPONSE = ReportConfig
				.getValue(ReportConfigItem.IS_DEFINED_RESPONSE.getConfigName());
		if (null != IS_DEFINED_RESPONSE
				&& Boolean.valueOf(IS_DEFINED_RESPONSE.toString())) {

			Object object;

			String code = null == (object = ReportConfig
					.getValue(ReportConfigItem.RESPONSE_CODE.getConfigName())) ? ""
					: object.toString();
			String message = null == (object = ReportConfig
					.getValue(ReportConfigItem.RESPONSE_MESSAGE.getConfigName())) ? ""
					: object.toString();
			String result = null == (object = ReportConfig
					.getValue(ReportConfigItem.RESPONSE_RESULT.getConfigName())) ? ""
					: object.toString();
			if (null != response.getResponse()) {
				
				Object temp;
				
				if (StringUtils.isNotEmpty(code) 
						&& (temp = BeanAUtils.getLayerProperty(response.getResponse(), code)) != null) {
					
					response.setCode((Integer)(temp));
				}
				if (StringUtils.isNotEmpty(message) 
						&& (temp = BeanAUtils.getLayerProperty(response.getResponse(), message)) != null) {
					
					response.setMessage((String)temp);
				}
				if (StringUtils.isNotEmpty(result) 
						&& (temp = BeanAUtils.getLayerProperty(response.getResponse(), result)) != null ) {
					
					response.setResponse(temp);
				}
			}
		}
		return response;
	}
}
