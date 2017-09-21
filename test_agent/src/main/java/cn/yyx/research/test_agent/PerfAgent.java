package cn.yyx.research.test_agent;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class PerfAgent {

	public static void premain(String agentArgs, Instrumentation _inst) {
		System.out.println("agentArgs:" + agentArgs);
		List<String> filters = new LinkedList<String>();
		if (agentArgs != null && !agentArgs.equals("")) {
			String[] agents = agentArgs.split(";|:|#");
			
		}
		
		CmpTransformer trans = new CmpTransformer();
		_inst.addTransformer(trans);
	}

}
