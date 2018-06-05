package cn.yyx.labtask.test_agent_trace_reader;

import cn.yyx.labtask.runtime.round.testgen.TestModel;

import java.util.Map;

public class UseTraceReaderComparator {
  public static void main(String[] args) {
    TraceReader tr = new TraceReader("seqBefore.txt", "seqAfter.txt");
    Map<String, ValuesOfBranch> branchesInfo1 =
        tr.ReadFromTraceFile(System.getProperty("user.home") + "/trace1.txt");
    Map<String, ValuesOfBranch> branchesInfo2 =
        tr.ReadFromTraceFile(System.getProperty("user.home") + "/trace2.txt");
  }
}
