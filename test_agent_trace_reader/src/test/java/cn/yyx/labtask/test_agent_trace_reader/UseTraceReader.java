package cn.yyx.labtask.test_agent_trace_reader;

import java.util.LinkedList;
import java.util.Map;

public class UseTraceReader {
  public static void main(String[] args) {
    String traceFilePath = System.getProperty("user.home") + "/trace.txt";
    TraceReader tr = new TraceReader("seqBefore.txt", "seqAfter.txt");
    Map<String, LinkedList<ValuesOfBranch>> branchesInfo1 = tr.ReadFromTraceFile(traceFilePath);
    System.out.println(branchesInfo1);
  }
}
