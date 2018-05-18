package cn.yyx.research.testgen;

public class BranchInStatic {

  static {
    int n = 1;
    if (n > 191919) {
      System.out.println("static n>191919");
    } else {
      System.out.println("static !n>191919");
    }
  }

  public static void main(String[] args) {
    System.out.println("BranchInStaticcccc");
  }
}
