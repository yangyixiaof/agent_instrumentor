package cn.yyx.research.testgen;

public class OverloadStaticNonStatic {
  public void g() {}

  public void g(int x) {}

  public int g(String x) {
    return 0;
  }
}
