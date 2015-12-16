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
package com.ankang.report.register.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.collections.list.TreeList;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ankang.report.config.ReportConfig;
import com.ankang.report.config.ReportConfigItem;
import com.ankang.report.exception.ReportException;
import com.ankang.report.main.ReportCabinet;
import com.ankang.report.model.Monitor;
import com.ankang.report.model.MonitorView;
import com.ankang.report.pool.AbstractReportAliasPool;
import com.ankang.report.register.ReportRegister;

@SuppressWarnings("all")
public final class MonitorRegister extends AbstractReportAliasPool implements
		ReportRegister {

	private static final Logger logger = Logger
			.getLogger(MonitorRegister.class);
	public static final String MONITOR_ALIAS_NAME = "monitorPool";
	private static final String FILE_PATH = "/report/report.cc";
	private static final Map monitorPool = new HashMap<String, List<Monitor>>();
	private static final List<String> appendText = new LinkedList();
	private PrintWriter pw;
	private File monitorFile;
	
	@Override
	public boolean reginster() {
		ReportCabinet.getReportApplicationContext().putPool(MONITOR_ALIAS_NAME,
				monitorPool);
		loadOldData();
		return true;
	}

	@Override
	public void configer(Class<?> clazz) {
		
		List<String> temp = new ArrayList<String>();
		temp.add(getAlias(clazz.getSimpleName()));
		append(appendText, temp, false);

	}

	@Override
	protected void setMountPool(Map waitMap) {
		this.waitMap = waitMap;
	}

	private void loadOldData() {

		loadFile();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(this.monitorFile));
			String text = null;
			int count = 0;
			while ((text = br.readLine()) != null) {
				if(count++ > 9 && text.trim().length() > 0){
					appendText.add(text.intern());
				}
			}
		} catch (IOException e) {
			logger.error("File read exception", e);
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					logger.error("IO flow off exception", e);
				}
			}
		}
	}

	private List<String> group(List<String> texts, String simpName) {

		List<String> result = new ArrayList<String>();
		for (int i = 0; i < texts.size(); i++) {

			if (texts.get(i).startsWith(simpName)) {
				result.add(texts.get(i));
			}
		}
		for (String value : result) {
			texts.remove(value);
		}
		return result;
	}

	public void append(List<String> text, List<String> groupName, boolean flag) {

		for (int i = 0; i < groupName.size(); i++) {
			List<String> group = group(text, groupName.get(i));
			if (group.size() < 1) {
				continue;
			}
			List<Monitor> monitors = (List<Monitor>) monitorPool
					.get(groupName.get(i));
			if (null == monitors) {
				monitors = new ArrayList<Monitor>();
			}
			f: for (String name : group) {
				String[] lineText = name.split("\\s+");

				if (monitors.size() > 0) {
					for (Monitor monitor : monitors) {
						if (StringUtils.isNotEmpty(lineText[1])
								&& monitor != null
								&& lineText[1].equals(monitor.getMethod())) {
							if (StringUtils.isNotEmpty(lineText[2])
									&& !lineText[2].equals("0")) {
								monitor.setSuccess(monitor.getSuccess() + Integer.valueOf(lineText[2]));
							}
							if (StringUtils.isNotEmpty(lineText[3])
									&& !lineText[3].equals("0")) {
								monitor.setFail(monitor.getFail()
										+ Integer.valueOf(lineText[3]));
							}
							if (StringUtils.isNotEmpty(lineText[4])) {
								monitor.getStart()
										.add(Long.valueOf(lineText[4]));
							}
							if (StringUtils.isNotEmpty(lineText[5])) {
								monitor.getEnd().add(Long.valueOf(lineText[5]));
							}
							if (StringUtils.isNotEmpty(lineText[6])) {
								Long currentTime = Long.valueOf(lineText[6]);
								monitor.getCurrent().add(currentTime);
								monitor.setTotalCount(monitor.getTotalCount()
										+ currentTime);
							}

							Long avg = monitor.getTotalCount()/ (monitor.getSuccess() == 0?1:monitor.getSuccess());
							monitor.getAvg().add(avg);// 不算失败次数
							if(flag){
								StringBuffer sb = new StringBuffer(100);
								for (int j = 0; j < lineText.length; j++) {
									sb.append(lineText[j]).append("\t\t");
								}
								sb.append(avg);
								appendText.add(sb.toString());
							}
							processMonitor(monitor, Long.valueOf(lineText[6]), avg);
							continue f;
						}
					}
				}
				Monitor monitor = new Monitor();
				monitor.setModul(lineText[0]);
				monitor.setMethod(lineText[1]);
				monitor.setSuccess(Integer.valueOf(lineText[2]));
				monitor.setFail(Integer.valueOf(lineText[3]));

				List<Long> start = new ArrayList<Long>();
				start.add(Long.valueOf(lineText[4]));
				monitor.setStart(start);

				List<Long> end = new ArrayList<Long>();
				end.add(Long.valueOf(lineText[5]));
				monitor.setEnd(end);

				List<Long> current = new ArrayList<Long>();
				Long currentTime = Long.valueOf(lineText[6]);
				current.add(currentTime);
				monitor.setCurrent(current);

				monitor.setTotalCount(currentTime);

				List<Long> avg = new ArrayList<Long>();
				avg.add(currentTime);
				monitor.setAvg(avg);
				
				if(flag){
					
					appendText.add(name+"\t\t" + currentTime);
				}
				processMonitor(monitor, currentTime, currentTime);//第一次创建对象，平均请求时间和当前请求时间
				monitors.add(monitor);
			}
			synchronized (monitorPool) {
				setMountPool(monitorPool);
				mount(groupName.get(i), monitors);
				if(flag){appendText(null);};
			}
		}
	}
	public void appendText(String text){
		
		if (null == pw) {
			if(this.monitorFile == null){
				loadFile();
			}
			try {
				pw = new PrintWriter(new FileWriter(this.monitorFile, true), true);
			} catch (IOException e) {
				logger.error("PrintWriter initialization fail", e);
			}
		}
		List<Object> removes = new ArrayList<Object>();
		if(appendText.size() > 0){
			for (int i = 0; i < appendText.size(); i++) {
				pw.println();
				pw.write(appendText.get(i));
				logger.info("记录请求日志.["+ appendText.get(i) +"]");
				pw.flush();
				removes.add(appendText.get(i));
			}
			for (Object value : removes) {
				appendText.remove(value);
			}
		}
		if(StringUtils.isNotEmpty(text)){
			pw.println();
			pw.write(text);
			pw.flush();
		}
		
	}
	private void loadFile() {
		
		File file = new File(getFilePath());
		
		if(!file.exists()){
			BufferedReader br = null;
			PrintWriter pw = null;
			try {
				file.getParentFile().mkdir();
				file.createNewFile();
				URL url = this.getClass().getClassLoader().getResource("/");
				String path = url.toString().split("file:/")[1];
				br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path+"//config//report.cc")),"UTF-8"));
				
				pw = new PrintWriter(new FileWriter(file,true));
				
				String line = null;
				
				while ((line = br.readLine()) != null) {
					pw.write(line);
					pw.println();
					pw.flush();
				}
			} catch (IOException e) {
				logger.error("report.cc create fail", e);
				throw new ReportException("report.cc create fail",e);
			}finally{
				if(pw != null){
					pw.close();
				}
				if(br != null){
					try {
						br.close();
					} catch (IOException e) {
						logger.error("IO flow off exception", e);
					}
				}
			}
		}
		this.monitorFile = file;
	}
	private String getFilePath(){
		
		Object path;
		
		return null == (path = ReportConfig.getValue(ReportConfigItem.MONITOR_FILE_PATH.getConfigName()))?this.FILE_PATH:
			path.toString()+this.FILE_PATH;
	}
	
	/**
	 * 
		 * @Description: 报表模型计算 
	     * @author: ankang
	     * @date: 2015-12-9 下午5:37:10
	     * @param monitor
	     * @param current 当前的请求时间
	     * @param avg
	 */
	private void processMonitor(Monitor monitor, Long current, Long avg){
		
		Long numY = current > avg?current:avg;
		MonitorView monitorView = monitor.getMonitorView();
		if(null == monitorView){
			monitorView = new MonitorView();
			monitor.setMonitorView(monitorView);
		}
		
		monitorView.setModul(monitor.getModul());
		monitorView.setMethod(monitor.getMethod());
		
		monitorView.setSuccess(monitor.getSuccess());
		monitorView.setFail(monitor.getFail());
		
		Long size = monitorView.getNextKeyCu() > monitorView.getNextKeyAvg()?
				monitorView.getNextKeyCu():monitorView.getNextKeyAvg();
		if(size >= monitorView.QUEUE_SIZE){
			
			while ((size % (monitorView.getNumberX()-1)) != 0) {
				size++;
			}
			monitorView.setEndX(size);
			
			monitorView.getAvgs().remove(monitorView.getStartX());
			monitorView.getCurrents().remove(monitorView.getStartX());
			
			monitorView.setStartX(monitorView.getStartX() + 1);
			
			mountAvgAndCu(monitorView, current, avg);
			
		}else{
			mountAvgAndCu(monitorView, current, avg);
		}
		
		Long maxY1 = electMaxY(monitorView.getCurrents(), monitorView.getNumberY() - 1);
		Long maxY2 = electMaxY(monitorView.getAvgs(), monitorView.getNumberY() - 1);
		monitorView.setEndY(maxY1 > maxY2?maxY1:maxY2);
		//计算y轴值
		if(monitorView.getEndY() < numY){
//			monitorView.setFlag(monitorView.QUEUE_SIZE);//第一次出现极限值，理论上20次之后会弹出
			while ((numY % (monitorView.getNumberY()-1)) != 0) {
				numY++;
			}
			monitorView.setEndY(numY);
		}
	}
	private void mountAvgAndCu(MonitorView monitorView, Long current, Long avg){
		
		monitorView.getAvgs().put(monitorView.getNextKeyAvg(), avg);
		monitorView.getCurrents().put(monitorView.getNextKeyCu(), current);
		monitorView.setNextKeyAvg(monitorView.getNextKeyAvg() + 1);
		monitorView.setNextKeyCu(monitorView.getNextKeyCu() + 1);
	}
	private Long electMaxY(Map<Long, Long> elects, Integer numberY){
		
		Long result = 0l;
		for (Long num : elects.values()) {
			if(num > result){
				result = num;
			}
		}
		while ((result % numberY) != 0) {
			result++;
		}
		return result;
	}
}
