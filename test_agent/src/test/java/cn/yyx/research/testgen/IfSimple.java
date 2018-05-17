package cn.yyx.research.testgen;

import java.util.Scanner;

public class IfSimple {

  int m = 1234567; // 初始化会被放到 <init> 里！

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    if (n > 100) {
      System.out.println("high");
    } else {
      System.out.println("low");
    }
  }
}
