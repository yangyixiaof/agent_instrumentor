package cn.yyx.research.testgen;

import java.util.Scanner;

public class IfOr {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    if (n > 100 || n < -100) {
      System.out.println("abs high");
    } else {
      System.out.println("abs low");
    }
  }
}
