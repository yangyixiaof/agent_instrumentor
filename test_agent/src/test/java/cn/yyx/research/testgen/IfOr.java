package cn.yyx.research.testgen;

import java.util.Scanner;

public class IfOr {

  private static Scanner in;

public static void main(String[] args) {
    in = new Scanner(System.in);
    int n = in.nextInt();
    if (n > 100 || n < -100) {
      System.out.println("abs high");
    } else {
      System.out.println("abs low");
    }
  }
}
