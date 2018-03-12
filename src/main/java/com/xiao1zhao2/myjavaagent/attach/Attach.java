package com.xiao1zhao2.myjavaagent.attach;

import java.lang.instrument.Instrumentation;

public class Attach {

	public static void agentmain(String args, Instrumentation inst) {

		try {
			inst.addTransformer(new AttachTransformer(), true);
			inst.retransformClasses(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
