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
		forbids.add("java/io/");
		forbids.add("java/lang/");
		forbids.add("com/google/");
		forbids.add("com/github/");
		forbids.add("com/sun/");
		forbids.add("java/net/");
		forbids.add("java/nio/");
		forbids.add("java/security/");
		forbids.add("java/util/concurrent/atomic/");
		forbids.add("java/util/jar/");
		forbids.add("java/util/zip/");
		forbids.add("javax/");
		forbids.add("jdk/");
		forbids.add("org/plumelib/");
		forbids.add("randoop/");
		forbids.add("sun/");
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		for (String forbid : forbids) {
			if (className.startsWith(forbid) || (className.startsWith("java/") && (className.endsWith("Exception") || className.endsWith("Error")))) {
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
