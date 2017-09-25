package cn.yyx.research.test_agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.trace.instrument.CmpInstrumenter;

public class CmpTransformer implements ClassFileTransformer {
	
	List<String> flowers = new LinkedList<String>();
	
	public CmpTransformer(List<String> flowers) {
//		for (String fileter : filters) {
//			System.out.println("filter:" + fileter);
//		}
		this.flowers.addAll(flowers);
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//		System.out.println("class_name:" + className);
		if (InFlower(className)) {
			return CmpInstrumenter.InstrumentOneClass(classfileBuffer);
		}
		return classfileBuffer;
	}
	
	protected boolean InFlower(String class_name) {
		for (String flower : flowers) {
			if (class_name.startsWith(flower)) {
				return true;
			}
		}
		return false;
	}
	
}
