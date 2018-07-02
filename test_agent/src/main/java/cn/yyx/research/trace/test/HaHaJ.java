package cn.yyx.research.trace.test;

public class HaHaJ {

  static {
    int n = 1;
    if (n > 191919) {
      System.out.println("static n>191919");
    } else {
      System.out.println("static !n>191919");
    }
  }

  public HaHaJ() {}

  public void test2(int x, int y) {
    if (x > y) {
      System.out.println("Great!");
    }
  }

  public int testInt(int x) {
    return x;
  }

  String testStr() {
    return null;
  }

  public static void main(String[] args) {
    int x = 0;
    String s = "string info";
    System.err.println(s);
    if (args.length > x) {
      System.err.println("hahah");
    }
    new HaHaJ().test2(x, 100);
  }
}
