package com.xiao1zhao2.myjavaagent.trace;

import java.util.ArrayList;
import java.util.List;

public class JsTreeData {

	private String text;
	private List<JsTreeData> children;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<JsTreeData> getChildren() {
		return children;
	}

	public void setChildren(List<JsTreeData> children) {
		this.children = children;
	}

	public static JsTreeData parseFromNode(TraceNode node) {

		JsTreeData vo = new JsTreeData();
		if (node != null) {
			vo.setText(String.format("%s [%s] %s#%s", Format.getTimeCost(node), Format.getPercent(node), node.getClazz(), node.getMethod()));
			if (node.getSubNodes() != null && node.getSubNodes().size() > 0) {
				List<JsTreeData> children = new ArrayList<JsTreeData>();
				for (TraceNode sub : node.getSubNodes()) {
					children.add(parseFromNode(sub));
				}
				vo.setChildren(children);
			}
		}
		return vo;
	}

	public static List<JsTreeData> parseFromNodeList(List<TraceNode> nodeList) {

		List<JsTreeData> voList = new ArrayList<JsTreeData>();
		for (TraceNode node : nodeList) {
			voList.add(JsTreeData.parseFromNode(node));
		}
		return voList;
	}

}
