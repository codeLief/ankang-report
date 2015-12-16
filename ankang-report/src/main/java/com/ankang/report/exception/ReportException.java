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
package com.ankang.report.exception;


/**
 * 
 * @Description: report异常
 * @author: ankang
 * @date: 2015-9-28 下午3:15:02
 */
public class ReportException extends BaseException {

	private static final long serialVersionUID = 7122658111260716065L;
	
	public ReportException(String message){
		super(message);
	}
	public ReportException(String message, Object... args){
		super(message, args);
	}
	public ReportException(int code, String msgFormat, Object... args) {
		super(code, msgFormat, args);
	}
	
	public ReportException(int code, Throwable cause, String msgFormat, Object... args) {
		super(cause);
		this.code = code;
		this.msg = String.format(msgFormat, args);
	}
	public ReportException newInstance(Throwable cause, String msgFormat, Object... args) {
		return new ReportException(code, cause, msgFormat, args);
	}
	
}
