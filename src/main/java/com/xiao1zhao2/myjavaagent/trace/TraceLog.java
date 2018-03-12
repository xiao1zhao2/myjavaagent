package com.xiao1zhao2.myjavaagent.trace;

public class TraceLog {

	private String uuid;
	private int num;
	private int step;
	private String position;
	private long time;
	private String clazz;
	private String method;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
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

	public boolean isFirstTraceLog() {
		return getNum() == 1 && getStep() == 1 && getPosition().equalsIgnoreCase("B");
	}

	public boolean isLastTraceLog() {
		return ((getNum() & 1) == 0) && getStep() == 1 && getPosition().equalsIgnoreCase("A");
	}

}
