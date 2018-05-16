package xyz.sonion;

import cn.yyx.labtask.runtime.round.testgen.TestModel;
import cn.yyx.labtask.test_agent_trace_reader.TraceReader;

public class UseTraceReader {
	public static void main(String[] args) {
		TraceReader tr = new TraceReader(new TestModel(), "A.txt", "B.txt");
		tr.ReadFromTraceFile();
	}
}
