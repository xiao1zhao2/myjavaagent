package com.xiao1zhao2.myjavaagent.agent;

import org.apache.log4j.Logger;

import java.text.MessageFormat;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AgentInject {

	private static final Logger logger = Logger.getLogger("agentlog");

	private static final ThreadLocal<String> uuid = new ThreadLocal<String>() {
		protected String initialValue() {
			return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		}
	};

	private static final ThreadLocal<AtomicLong> num = new ThreadLocal<AtomicLong>() {
		protected AtomicLong initialValue() {
			return new AtomicLong();
		}
	};

	private static final ThreadLocal<AtomicLong> step = new ThreadLocal<AtomicLong>() {
		protected AtomicLong initialValue() {
			return new AtomicLong();
		}
	};

	public static void before(String className, String methodName, Object[] args) {
		long nl = num.get().incrementAndGet();
		long sl = step.get().incrementAndGet();
		logger.info(MessageFormat.format("{0}\t{1,number,#}\t{2,number,#}\tB\t{3,number,#}\t{4}\t{5}", uuid.get(), nl, sl, System.nanoTime(), className, methodName));
	}

	public static void after(String className, String methodName, Object[] args) {
		long nl = num.get().incrementAndGet();
		long sl = step.get().getAndDecrement();
		logger.info(MessageFormat.format("{0}\t{1,number,#}\t{2,number,#}\tA\t{3,number,#}\t{4}\t{5}", uuid.get(), nl, sl, System.nanoTime(), className, methodName));
		if (sl == 1L) {
			uuid.remove();
			num.remove();
			step.remove();
		}
	}

	public static void exception(String className, String methodName, Throwable e) {
		long nl = num.get().get();
		long sl = step.get().get();
		logger.info(MessageFormat.format("{0}\t{1,number,#}\t{2,number,#}\tE\t{3,number,#}\t{4}\t{5}", uuid.get(), nl, sl, System.nanoTime(), className, methodName));
	}

}
