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


public class BaseException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	protected String msg;
	
	protected int code;
	public BaseException(String message, Object...argers){
		super(String.format(message, argers));
		this.msg = String.format(message, argers);
	}
	
	public BaseException(int code, String message, Object...argers){
		super(String.format(message, argers));
		this.code = code;
		this.msg = String.format(message, argers);
	}
	public BaseException(String message){
		super(message);
		this.msg = message;
	}
	
	public BaseException(Throwable cause){
		super(cause);
	}
	
	public BaseException(String message, Throwable cause){
		super(message, cause);
		this.msg = message;
	}
	
	public BaseException newInstance(String msgFormat, Object... args) {
		return new BaseException(this.code, msgFormat, args);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
