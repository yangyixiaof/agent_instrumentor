package cn.yyx.research.test_agent;

import java.lang.instrument.Instrumentation;

/**
 * Hello world!
 *
 */
public class PerfAgent {

	public static void premain(String agentArgs, Instrumentation _inst) {
		System.out.println("agentArgs:" + agentArgs);
		CmpTransformer trans = new CmpTransformer();
		_inst.addTransformer(trans);
	}

}
