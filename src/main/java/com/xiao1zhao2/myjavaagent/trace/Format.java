package com.xiao1zhao2.myjavaagent.trace;

import com.xiao1zhao2.myjavaagent.trace.TraceNode;

import java.text.NumberFormat;

public class Format {

	public static String getPercent(TraceNode node) {

		TraceNode parent = node.getParent();
		if (parent == null) {
			return "100%";
		}

		while (parent.getParent() != null) {
			parent = parent.getParent();
		}

		NumberFormat format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		return format.format((float) node.getTime() / (float) parent.getTime() * 100) + "%";
	}

	public static String getTimeCost(TraceNode node) {
		NumberFormat format = NumberFormat.getInstance();
		return format.format(node.getTime());
	}
}
