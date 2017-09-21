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
			for (String agent : agents) {
				filters.add(agent.replace('.', '/'));
			}
		}
		
		CmpTransformer trans = new CmpTransformer(filters);
		_inst.addTransformer(trans);
	}

}
