package cn.yyx.research.testgen;

public class BranchInStaticMethod {

  public static void f() {
    int m = 999999;
    if (m > 292929) {
      System.out.println("static void f(), m > 292929");
    } else {
      System.out.println("static void f(), !m > 292929");
    }
  }

  public static void main(String[] args) {
    System.out.println("BranchInStaticMethod");
    int n = 1;
    if (n > 191919) {
      System.out.println("static n>191919");
      f();
    } else {
      System.out.println("static !n>191919");
      f();
    }
  }
}
