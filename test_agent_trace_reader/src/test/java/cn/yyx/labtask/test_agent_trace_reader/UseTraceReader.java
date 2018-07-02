package cn.yyx.labtask.test_agent_trace_reader;

public class UseTraceReader {
  public static void main(String[] args) {
    String traceFilePath = System.getProperty("user.home") + "/trace.txt";
    TraceReader tr = new TraceReader("seqBefore.txt", "seqAfter.txt");
    TraceInfo branchesInfo1 = tr.ReadFromTraceFile(traceFilePath);
    System.out.println(branchesInfo1);
  }
}
