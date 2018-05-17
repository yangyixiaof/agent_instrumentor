package cn.yyx.research.testgen;

import java.util.Scanner;

public class IfDNF {

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    int n = in.nextInt();
    if (1000 > n && n > 100 || -1000 < n && n < -100) {
      System.out.println("abs good");
    } else {
      System.out.println("abs bad");
    }
  }
}
