package com.xiao1zhao2.myjavaagent.agent;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;

public class AgentTransformer implements ClassFileTransformer {

	private static ClassPool classPool = ClassPool.getDefault();
	private static AtomicBoolean hasAppendWebAppClassLoader = new AtomicBoolean();

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		String clazzName = className.replace("/", ".");
		System.out.println("clazzName : "+clazzName);
		try {
			appendWebappCLassLoader();
			if (isClazzNeedModify(clazzName)) {
				System.out.println("modify clazzName : "+clazzName);
				CtClass ctClass = classPool.get(clazzName);
				if (ctClass != null && !ctClass.isInterface() && !ctClass.isAnnotation()) {
//					CtClass e = classPool.get("java.lang.Exception");
					CtMethod[] ctMethods = ctClass.getDeclaredMethods();
					if (ctMethods != null && ctMethods.length > 0) {
						for (CtMethod ctMethod : ctMethods) {
							if (!ctMethod.isEmpty() && !Modifier.isAbstract(ctMethod.getModifiers()) && !Modifier.isNative(ctMethod.getModifiers())) {
								ctMethod.insertBefore(MessageFormat.format("com.xiao1zhao2.myjavaagent.agent.AgentInject.before(\"{0}\", \"{1}\", $args);", ctClass.getName(), ctMethod.getName()));
//								ctMethod.addCatch(MessageFormat.format("com.xiao1zhao2.myjavaagent.agent.AgentInject.exception(\"{0}\", \"{1}\", $e); throw $e;", ctClass.getName(), ctMethod.getName()), e);
								ctMethod.insertAfter(MessageFormat.format("com.xiao1zhao2.myjavaagent.agent.AgentInject.after(\"{0}\", \"{1}\", $args);", ctClass.getName(), ctMethod.getName()), true);
							}
						}
					}
					return ctClass.toBytecode();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean isClazzNeedModify(String clazzName) throws Exception {

		for (String include : Agent.includePatterns) {
			if (clazzName.matches(include)) {
				return true;
			}
		}
		return false;
	}

	private void appendWebappCLassLoader() throws Exception {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (!hasAppendWebAppClassLoader.get() && classLoader != null && classLoader.getClass().getSimpleName().equalsIgnoreCase("webappclassloader")) {
			LoaderClassPath loaderClassPath = new LoaderClassPath(classLoader);
			classPool.insertClassPath(loaderClassPath);
			hasAppendWebAppClassLoader.set(true);
		}
	}

}
