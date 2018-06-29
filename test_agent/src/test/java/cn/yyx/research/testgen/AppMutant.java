package cn.yyx.research.testgen;

public class AppMutant {

  // 被测函数
  public int cut0(int n) {
    int s = 0;
    for (int i = 0; i < n; i++) {
      if (i % 3 != 0) {
        s += 10;
      }
    }
    return s;
  }

  // 一个测试用例。即一个 sequence。
  public static void test0() {
    AppMutant appMutant0 = new AppMutant();
    int int1 = 145;
    int int2 = appMutant0.cut0(int1);
    System.out.println(int2);
  }

  public static void main(String[] args) {
    test0();
  }
}
