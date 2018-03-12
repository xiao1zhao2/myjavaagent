package com.xiao1zhao2.myjavaagent.trace;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TraceTest {

	public String userDir = System.getProperty("user.dir");

	private List<TraceLog> loadTraceLog() throws Exception {

		List<TraceLog> traceLogList = new ArrayList<TraceLog>();
		File file = new File(userDir + File.separator + "log", "agent.log");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] arr = line.split("\t");
			TraceLog traceLog = new TraceLog();
			traceLog.setUuid(arr[0]);
			traceLog.setNum(Integer.valueOf(arr[1]));
			traceLog.setStep(Integer.valueOf(arr[2]));
			traceLog.setStep(Integer.valueOf(arr[2]));
			traceLog.setPosition(arr[3]);
			traceLog.setTime(Long.valueOf(arr[4]));
			traceLog.setClazz(arr[5]);
			traceLog.setMethod(arr[6]);
			traceLogList.add(traceLog);
		}
		return traceLogList;
	}

	private List<TraceNode> getTraceNodeByTraceLog(List<TraceLog> traceLogList) throws Exception {

		List<TraceNode> traceNodeList = new ArrayList<TraceNode>();

		String uuid = null;
		TraceNode top = null;
		TraceNode now = null;

		for (TraceLog traceLog : traceLogList) {

			//get first
			if (traceLog.isFirstTraceLog()) {
				uuid = traceLog.getUuid();

				top = new TraceNode();
				top.setUuid(traceLog.getUuid());
				top.setClazz(traceLog.getClazz());
				top.setMethod(traceLog.getMethod());
				top.setStart(traceLog.getTime());
				top.setStep(1);
				continue;
			}

			//get last
			if (top != null && traceLog.isLastTraceLog() && traceLog.getUuid().equals(uuid)) {
				top.setEnd(traceLog.getTime());
				top.setTime(top.getEnd() - top.getStart());
				traceNodeList.add(top);

				uuid = null;
				top = null;
				now = null;
				continue;
			}

			//get middle
			int subSize = 0;
			TraceNode current = null;
			if (top != null && uuid != null && !uuid.isEmpty() && traceLog.getUuid().equals(uuid)) {

				int step = traceLog.getStep();
				String position = traceLog.getPosition();

				now = top;
				if (now.getSubNodes() == null) {
					now.setSubNodes(new ArrayList<TraceNode>());
				}

				while (--step > 1 && (subSize = now.getSubNodes().size()) > 0) {
					now = now.getSubNodes().get(subSize - 1);
					if (now.getSubNodes() == null) {
						now.setSubNodes(new ArrayList<TraceNode>());
					}
				}

				if (position.equalsIgnoreCase("B")) {
					current = new TraceNode();
					current.setUuid(traceLog.getUuid());
					current.setClazz(traceLog.getClazz());
					current.setMethod(traceLog.getMethod());
					current.setStart(traceLog.getTime());
					current.setStep(traceLog.getStep());
					current.setParent(now);
					now.getSubNodes().add(current);
				} else if (position.equalsIgnoreCase("A")) {
					current = now.getSubNodes().get(now.getSubNodes().size() - 1);
					current.setEnd(traceLog.getTime());
					current.setTime(current.getEnd() - current.getStart());
				}
			}
		}
		return traceNodeList;
	}

	private void writeToFile(List<JsTreeData> voList) throws Exception {

		File out = new File(userDir + File.separator + "output", "out.json");
		BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		writer.write(JSON.toJSONString(voList, true));
		writer.flush();
		writer.close();
	}

	@Test
	public void testTrace() throws Exception {
		List<TraceLog> traceLogList = loadTraceLog();
		List<TraceNode> traceNodeList = getTraceNodeByTraceLog(traceLogList);
		List<JsTreeData> dataList = JsTreeData.parseFromNodeList(traceNodeList);
		writeToFile(dataList);
	}

}
