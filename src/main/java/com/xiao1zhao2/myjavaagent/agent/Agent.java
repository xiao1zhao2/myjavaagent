package com.xiao1zhao2.myjavaagent.agent;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarFile;

public class Agent {

	public static Properties props = new Properties();
	public static String userDir = System.getProperty("user.dir");
	public static Set<String> includePatterns = new HashSet<String>(16);

	private static void loadProps() throws Exception {

		String file = userDir+File.separator+"config"+File.separator+"agent.properties";
		InputStream is = new FileInputStream(file);
		props.load(is);
		is.close();

		String bootstrapJar = props.getProperty("bootstrapJar");
		if (bootstrapJar == null || bootstrapJar.trim().length() == 0) {
			String javassistJar = userDir + File.separator + "lib" + File.separator + "javassist-3.12.1.GA.jar";
			String log4jJar = userDir + File.separator + "lib" + File.separator + "log4j-1.2.17.jar";
			bootstrapJar = javassistJar + "," + log4jJar;
		}
		props.setProperty("bootstrapJar", bootstrapJar);

		String logPath = props.getProperty("logPath");
		if (logPath == null || logPath.trim().length() == 0) {
			logPath = userDir + File.separator + "output";
		}
		props.setProperty("logPath", logPath);

		String includeClasses = props.getProperty("includePatterns");
		if (includeClasses != null && !includeClasses.isEmpty()) {
			Collections.addAll(includePatterns, includeClasses.split(","));
		}
	}

	private static void appendBootstrapJar(Instrumentation inst) throws Exception {
		String[] bootstrapJars = props.getProperty("bootstrapJar", "").split(",");
		for (String jar : bootstrapJars) {
			JarFile jarFile = new JarFile(jar);
			inst.appendToBootstrapClassLoaderSearch(jarFile);
		}
	}

	private static void initLogConfig() throws Exception {

		String logPath = props.getProperty("logPath");
		Properties logProps = new Properties();
		logProps.setProperty("log4j.logger.agentlog", "INFO,agentlog");
		logProps.setProperty("log4j.additivity.agentlog", "false");
		logProps.setProperty("log4j.appender.agentlog", "org.apache.log4j.DailyRollingFileAppender");
		logProps.setProperty("log4j.appender.agentlog.DatePattern", "'.'yyyy-MM-dd'.log'");
		logProps.setProperty("log4j.appender.agentlog.layout", "org.apache.log4j.PatternLayout");
		logProps.setProperty("log4j.appender.agentlog.layout.ConversionPattern", "%m%n");
		logProps.setProperty("log4j.appender.agentlog.file", logPath + File.separator + "agent.log");
		logProps.setProperty("log4j.appender.agentlog.encoding", "UTF-8");
//		log4j_properties.setProperty("log4j.appender.agentlog.BufferedIO", "true");
//		log4j_properties.setProperty("log4j.appender.agentlog.BufferSize", "8192");
		PropertyConfigurator.configure(logProps);
	}

	public static void premain(String args, Instrumentation inst) {

		try {
			System.out.println("========== call agent start ==========");
			loadProps();
			appendBootstrapJar(inst);
			initLogConfig();
			inst.addTransformer(new AgentTransformer());
			System.out.println("========== call agent end ==========");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}

