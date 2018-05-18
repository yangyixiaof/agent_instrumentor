package cn.yyx.labtask.test_agent_trace_reader;

import cn.yyx.labtask.runtime.round.testgen.TestModel;

public class UseTraceReader {
	public static void main(String[] args) {
		TraceReader tr = new TraceReader(new TestModel(), "A.txt", "B.txt");
		tr.ReadFromTraceFile();
	}
}
