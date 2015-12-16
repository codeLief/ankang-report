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
import java.util.List;

/**
 * 
 * @Description: 统计模型 
 * @author: ankang
 * @date: 2015-12-7 下午4:19:36
 */
public class Monitor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String modul;
	private String method;
	private Integer success = 0;
	private Integer fail = 0;
	private List<Long> start;//如果没有为0
	private List<Long> end; //如果没有为0
	private List<Long> avg;//如果没有为0
	private List<Long> current;//如果没有为0
	private Long totalCount = 0l;
	private MonitorView monitorView;
	
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
	public List<Long> getStart() {
		return start;
	}
	public void setStart(List<Long> start) {
		this.start = start;
	}
	public List<Long> getEnd() {
		return end;
	}
	public void setEnd(List<Long> end) {
		this.end = end;
	}
	public List<Long> getAvg() {
		return avg;
	}
	public void setAvg(List<Long> avg) {
		this.avg = avg;
	}
	public List<Long> getCurrent() {
		return current;
	}
	public void setCurrent(List<Long> current) {
		this.current = current;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public MonitorView getMonitorView() {
		return monitorView;
	}
	public void setMonitorView(MonitorView monitorView) {
		this.monitorView = monitorView;
	}
	public Monitor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Monitor(String modul, String method, Integer success, Integer fail,
			List<Long> start, List<Long> end, List<Long> avg,
			List<Long> current, Long totalCount, MonitorView monitorView) {
		super();
		this.modul = modul;
		this.method = method;
		this.success = success;
		this.fail = fail;
		this.start = start;
		this.end = end;
		this.avg = avg;
		this.current = current;
		this.totalCount = totalCount;
		this.monitorView = monitorView;
	}
	@Override
	public String toString() {
		return "Monitor [modul=" + modul + ", method=" + method + ", success="
				+ success + ", fail=" + fail + ", start=" + start + ", end="
				+ end + ", avg=" + avg + ", current=" + current
				+ ", totalCount=" + totalCount + ", monitorView=" + monitorView
				+ "]";
	}
}
