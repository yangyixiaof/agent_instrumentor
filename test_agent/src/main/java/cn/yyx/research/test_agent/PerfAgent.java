package cn.yyx.research.test_agent;

import java.lang.instrument.Instrumentation;

import cn.yyx.research.trace_recorder.TraceRecorder;

public class PerfAgent {

	public static void premain(String agentArgs, Instrumentation _inst) {
		System.out.println("RecordDirectoryPath:" + agentArgs);
		TraceRecorder.trace_dir = agentArgs;
		CmpTransformer trans = new CmpTransformer();
		_inst.addTransformer(trans);
	}
}
