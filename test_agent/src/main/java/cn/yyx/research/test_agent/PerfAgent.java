package cn.yyx.research.test_agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class PerfAgent {

	public static void premain(String agentArgs, Instrumentation _inst) {
		System.out.println("Received Filters:" + ((agentArgs == null || agentArgs.equals("")) ? "@no filters!" : agentArgs));
		List<String> flowers = new LinkedList<String>();
		if (agentArgs != null && !agentArgs.equals("")) {
			String[] agents = agentArgs.split(";|:|#");
			for (String agent : agents) {
				flowers.add(agent.replace('.', '/'));
			}
		}
		
		ClassFileTransformer trans = new InverseCmpTransformer(flowers); // CmpTransformer
		_inst.addTransformer(trans);
	}

}
