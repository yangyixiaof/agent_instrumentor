package cn.yyx.research.agent_mode;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.trace_recorder.TraceRecorder;

/**
 * @deprecated
 */
public class PerfAgent {

	public static void premain(String agentArgs, Instrumentation _inst) {
//		System.out.println("Received Filters:" + ((agentArgs == null || agentArgs.equals("")) ? "@no filters/flowers!" : agentArgs));
		System.out.println("RecordDirectoryPath:" + agentArgs);
		TraceRecorder.trace_dir = agentArgs;
		List<String> flowers = new LinkedList<String>();
//		if (agentArgs != null && !agentArgs.equals("")) {
//			String[] agents = agentArgs.split(";|:|#");
//			for (String agent : agents) {
//				flowers.add(agent.replace('.', '/'));
//			}
//		}
		ClassFileTransformer trans = new InverseCmpTransformer(flowers); // CmpTransformer
		_inst.addTransformer(trans);
	}

}
