package com.xiao1zhao2.myjavaagent.trace;

import java.util.List;

public class TraceNode {

	private String uuid;
	private String clazz;
	private String method;
	private long start;
	private long end;
	private long time;
	private int step;
	private TraceNode parent;
	private List<TraceNode> subNodes;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public TraceNode getParent() {
		return parent;
	}

	public void setParent(TraceNode parent) {
		this.parent = parent;
	}

	public List<TraceNode> getSubNodes() {
		return subNodes;
	}

	public void setSubNodes(List<TraceNode> subNodes) {
		this.subNodes = subNodes;
	}
}
