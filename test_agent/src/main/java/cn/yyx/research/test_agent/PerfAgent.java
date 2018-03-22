package cn.yyx.research.test_agent;

import java.lang.instrument.Instrumentation;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.trace_recorder.TraceRecorder;

/**
 * Hello world!
 *
 */
public class PerfAgent {

	public static void premain(String agentArgs, Instrumentation _inst) {
		System.out.println("RecordDirectoryPath:" + agentArgs);
		TraceRecorder.trace_dir = agentArgs;
		List<String> flowers = new LinkedList<String>();
//		if (agentArgs != null && !agentArgs.equals("")) {
//			String[] agents = agentArgs.split(";|:|#");
//			for (String agent : agents) {
//				flowers.add(agent.replace('.', '/'));
//			}
//		}
		CmpTransformer trans = new CmpTransformer(flowers);
		_inst.addTransformer(trans);
	}

}
