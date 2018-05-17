package cn.yyx.research.trace.test;

public class HaHaJ {

  public HaHaJ() {}

  public void test2(int x, int y) {
    if (x > y) {
      System.out.println("Great!");
    }
  }

  public static void main(String[] args) {
    int x = 0;
    if (args.length > x) {
      System.err.println("hahah");
    }
    new HaHaJ().test2(x, 100);
  }
}
