package cn.yyx.research.test_agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cn.yyx.research.trace.instrument.CmpInstrumenter;

public class CmpTransformer implements ClassFileTransformer {

	private List<String> flowers = new LinkedList<String>();
	private Set<String> forbids = new HashSet<String>();

	public CmpTransformer() {
		// set up flowers
//		flowers.add("java/lang/String");
		flowers.add("randoop/generation/date/test/");
		flowers.add("randoop/generation/date/test/resource/");
		// set up forbids
		forbids.add("cern/colt/");
		forbids.add("cn/yyx/");
		forbids.add("com/github/");
		forbids.add("com/google/");
		forbids.add("com/sun/");
		forbids.add("java/io/");
		forbids.add("java/lang/");
		forbids.add("java/math/");
		forbids.add("java/net/");
		forbids.add("java/nio/");
		forbids.add("java/security/");
		forbids.add("java/sql/");
		forbids.add("java/text/");
		forbids.add("java/text/DecimalFormat");
		forbids.add("java/text/DecimalFormatSymbols");
		forbids.add("java/text/spi/");
		forbids.add("java/util/");
		forbids.add("java/util/Currency");
		forbids.add("java/util/concurrent/");
		forbids.add("java/util/Date");
		forbids.add("java/util/Formattable");
		forbids.add("java/util/Formatter");
		forbids.add("java/util/function/");
		forbids.add("java/util/jar/");
		forbids.add("java/util/ListResourceBundle");
		forbids.add("java/util/Locale");
		forbids.add("java/util/logging/");
		forbids.add("java/util/Properties");
		forbids.add("java/util/Random");
		forbids.add("java/util/ResourceBundle");
		forbids.add("java/util/ServiceLoader");
		forbids.add("java/util/spi/");
		forbids.add("java/util/stream/");
		forbids.add("java/util/TimeZone");
		forbids.add("java/util/zip/");
		forbids.add("javax/");
		forbids.add("jdk/");
		forbids.add("org/apache/commons/");
		forbids.add("org/eclipse/");
		forbids.add("org/plumelib/");
		forbids.add("randoop/");
		forbids.add("sun/");
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		boolean must_flow = false;
		for (String flow : flowers) {
			if (className.startsWith(flow)) {
				must_flow = true;
				break;
			}
		}
		if (!must_flow) {
			for (String forbid : forbids) {
				if (className.startsWith(forbid) || (className.startsWith("java/")
						&& (className.endsWith("Exception") || className.endsWith("Error")))) {
					return classfileBuffer;
				}
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
