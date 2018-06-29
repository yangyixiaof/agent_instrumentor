package cn.yyx.research.testgen;

/** */
public class CutSimpleTestSimple {

  // 一个测试用例。即一个 sequence。
  public static void test0() {
    CutSimple appMutant0 = new CutSimple();
    int int1 = 114514;
    boolean boolean2 = appMutant0.f(int1);
    System.out.println(boolean2);
  }

  public static void main(String[] args) {
    test0();
  }
}
