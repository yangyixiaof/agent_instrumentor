package cn.yyx.research.test_agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.yyx.research.trace.instrument.CmpInstrumenter;

public class CmpTransformer implements ClassFileTransformer {

//	private List<String> flowers = new LinkedList<>();
	private Set<String> forbids = new HashSet<String>();

	public CmpTransformer(List<String> flowers) {
		// for (String fileter : filters) {
		// System.out.println("filter:" + fileter);
		// }
//		this.flowers.addAll(flowers);
		forbids.add("sun/");
		forbids.add("randoop/");
		forbids.add("java/io/");
		forbids.add("java/util/concurrent/atomic/");
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		for (String forbid : forbids) {
			if (className.startsWith(forbid)) {
				return classfileBuffer;
			}
		}
		return CmpInstrumenter.InstrumentOneClass(className, classfileBuffer);
	}

	// protected boolean InFlower(String class_name) {
	// for (String flower : flowers) {
	// if (class_name.startsWith(flower)) {
	// return true;
	// }
	// }
	// return false;
	// }

}
