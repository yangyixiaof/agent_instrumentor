package cn.yyx.labtask.test_agent_trace_reader;

public class UseTraceReaderComparator {
  public static void main(String[] args) {
    TraceReader tr = new TraceReader("traceLoop-1", "traceLoop-2");
    TraceInfo branchesInfo1 =
        tr.ReadFromTraceFile(System.getProperty("user.home") + "/traceLoop-1.txt");
    TraceInfo branchesInfo2 =
        tr.ReadFromTraceFile(System.getProperty("user.home") + "/traceLoop-2.txt");

    TracePairComparator cmpor = new TracePairComparator();
    System.out.println("branchesInfo1:" + branchesInfo1);
    System.out.println("branchesInfo2:" + branchesInfo2);
    System.out.println("TracePairComparator:" + cmpor);
  }
}
