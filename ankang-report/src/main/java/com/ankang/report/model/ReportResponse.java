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
package com.ankang.report.model;

import java.io.Serializable;

import com.ankang.report.enumz.ReportStatus;

/**
 * 
 * @Description: response 
 * @author: ankang
 * @date: 2015-10-16 上午11:34:28
 */
public class ReportResponse implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private int code = ReportStatus.SUCCESS_CODE;
	
	private String message = ReportStatus.SUCCESS_MSG;
	
	private Object response;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}
}
