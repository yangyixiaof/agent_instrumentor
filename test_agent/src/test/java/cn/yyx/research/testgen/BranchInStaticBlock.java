package cn.yyx.research.testgen;

public class BranchInStaticBlock {

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
    int n = 1;
    if (n > 292929) {
      System.out.println("static n>191919");
    } else {
      System.out.println("static !n>191919");
    }
  }
}
