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
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @Description: 报表显示对象
 * @author: ankang
 * @date: 2015-12-9 下午4:39:32
 */
public class MonitorView implements Serializable {


	private static final long serialVersionUID = 1L;
	private String modul;
	private String method;
	
	private Integer success = 0;
	private Integer fail = 0;
	
	public static final Integer QUEUE_SIZE = 20;
	private Integer flag;; //y轴最大数持续时间
	
	private Long nextKeyCu = 0l;
	private Long nextKeyAvg = 0l;
	
	private Map<Long, Long> currents = new TreeMap<Long, Long>();
	private Map<Long, Long> avgs = new TreeMap<Long, Long>();
	
	private Long startX = 0l; //轴线开始值
	private Long endX = 20l; //节点数 向上取合理值 避免节点产生小数
	private Integer numberX = 5;//轴线显示个数
	
	private Long startY = 0l;
	private Long endY = 500l;//毫秒值 向上取合理值 避免节点产生小数
	private Integer numberY = 5;
	
	
	public String getModul() {
		return modul;
	}
	public void setModul(String modul) {
		this.modul = modul;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public Integer getFail() {
		return fail;
	}
	public void setFail(Integer fail) {
		this.fail = fail;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Long getNextKeyCu() {
		return nextKeyCu;
	}
	public void setNextKeyCu(Long nextKeyCu) {
		this.nextKeyCu = nextKeyCu;
	}
	public Long getNextKeyAvg() {
		return nextKeyAvg;
	}
	public void setNextKeyAvg(Long nextKeyAvg) {
		this.nextKeyAvg = nextKeyAvg;
	}
	public Map<Long, Long> getCurrents() {
		return currents;
	}
	public void setCurrents(Map<Long, Long> currents) {
		this.currents = currents;
	}
	public Map<Long, Long> getAvgs() {
		return avgs;
	}
	public void setAvgs(Map<Long, Long> avgs) {
		this.avgs = avgs;
	}
	public Long getStartX() {
		return startX;
	}
	public void setStartX(Long startX) {
		this.startX = startX;
	}
	public Long getEndX() {
		return endX;
	}
	public void setEndX(Long endX) {
		this.endX = endX;
	}
	public Integer getNumberX() {
		return numberX;
	}
	public void setNumberX(Integer numberX) {
		this.numberX = numberX;
	}
	public Long getStartY() {
		return startY;
	}
	public void setStartY(Long startY) {
		this.startY = startY;
	}
	public Long getEndY() {
		return endY;
	}
	public void setEndY(Long endY) {
		this.endY = endY;
	}
	public Integer getNumberY() {
		return numberY;
	}
	public void setNumberY(Integer numberY) {
		this.numberY = numberY;
	}
}
