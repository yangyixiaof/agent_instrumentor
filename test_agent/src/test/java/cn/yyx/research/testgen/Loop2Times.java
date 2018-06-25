package cn.yyx.research.testgen;

public class Loop2Times {
  public static void main(String[] args) {
    int n = 2, m = 0;
    while (n > 0) {
      n--;
      m++;
    }

    if (m == 2) {
      System.out.println("sometwo");
    } else {
      System.out.println();
    }
  }
}
