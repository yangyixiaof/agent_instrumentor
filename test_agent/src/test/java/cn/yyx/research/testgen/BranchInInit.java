package cn.yyx.research.testgen;

public class BranchInInit {
  int m = 1234567; // 初始化会被放到 <init> 里！
  int p;

  BranchInInit() {
    this.p = 7654321;
  }

  BranchInInit(int p) {
    this.p = p;
  }

  public static void main(String[] args) {
    System.out.println("BranchInInit");
  }
}
