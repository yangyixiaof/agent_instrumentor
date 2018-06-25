package cn.yyx.research.testgen;

public class LoopIfBreak {
  public static void main(String[] args) {
    int n = 2, m = 0;
    while (n > 0) {
      n--;
      m++;
      if (m >= 1) {
        break;
      }
    }

    if (m == 1919) {
      System.out.println("1919");
    } else {
      System.out.println();
    }
  }
}
