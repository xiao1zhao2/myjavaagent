package com.xiao1zhao2.myjavaagent.attach;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AttachTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

		String clazzName = className.replace("/", ".");
		try {
			ClassPool classPool = ClassPool.getDefault();
			CtClass ctClass = classPool.get(clazzName);
			CtMethod[] ctMethods = ctClass.getDeclaredMethods();
			if (ctMethods != null && ctMethods.length > 0) {
				for (CtMethod ctMethod : ctMethods) {
					if (ctMethod.getName().contains("substring")) {
						ctMethod.insertBefore("System.out.println(\"before : \"+System.currentTimeMillis());");
						ctMethod.insertAfter("System.out.println(\"after : \"+System.currentTimeMillis());", true);
					}
				}
			}
			return ctClass.toBytecode();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
