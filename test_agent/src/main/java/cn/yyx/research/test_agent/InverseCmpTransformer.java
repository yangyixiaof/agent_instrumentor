package cn.yyx.research.test_agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.trace.instrument.SimpleInstrumenter;
import util.Trie;

public class InverseCmpTransformer implements ClassFileTransformer {
	
	List<String> flowers = new LinkedList<String>();
	Trie forbid = new Trie();
	{
		// forbid.insert("java/lang/invoke/MethodHandleImpl");
		forbid.insert("cn.yyx.research");
	}
	SimpleInstrumenter simple_inst = new SimpleInstrumenter();
	
	public InverseCmpTransformer(List<String> flowers) {
		for (String flower : flowers) {
			System.out.println("flower:" + flower);
		}
		this.flowers.addAll(flowers);
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if (forbid.prefixSearch(className)) {
			System.out.println("Skipping ...:" + className);
			return classfileBuffer;
		}
		String classname = className.replace('/', '.');
		if (InFlowers(classname)) {
			byte[] bt = null;
			try {
				bt = simple_inst.InstrumentOneClass(className, classfileBuffer);
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error er) {
				er.printStackTrace();
			}
			if (bt == null) {
				bt = classfileBuffer;
			}
			return bt;
		}
		return classfileBuffer;
	}
	
	protected boolean InFlowers(String class_name) {
		if (flowers.size() == 0) {
			return true;
		}
		for (String flower : flowers) {
			if (class_name.startsWith(flower)) {
				return true;
			}
		}
		return false;
	}
	
}
