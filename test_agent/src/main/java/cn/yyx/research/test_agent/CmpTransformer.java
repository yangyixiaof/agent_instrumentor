package cn.yyx.research.test_agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.trace.instrument.CmpInstrumenter;

public class CmpTransformer implements ClassFileTransformer {
	
	List<String> filters = new LinkedList<String>();
	
	public CmpTransformer(List<String> filters) {
//		for (String fileter : filters) {
//			System.out.println("filter:" + fileter);
//		}
		this.filters.addAll(filters);
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
//		System.out.println("class_name:" + className);
		if (InFilter(className)) {
			return CmpInstrumenter.InstrumentOneClass(classfileBuffer);
		}
		return classfileBuffer;
	}
	
	protected boolean InFilter(String class_name) {
		for (String filter : filters) {
			if (class_name.startsWith(filter)) {
				return true;
			}
		}
		return false;
	}
	
}
