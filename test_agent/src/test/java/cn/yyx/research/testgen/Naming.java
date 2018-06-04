package cn.yyx.research.testgen;

import java.util.ArrayList;

/** <code>词汇约定.md</code> 文档里的例子 */
public class Naming {
  public static void main(String[] args) {
    int n = 2929;
    ArrayList<Object> list = new ArrayList<>();
    Object obj = new Object();

    if (n > 1919 && list.contains(obj)) { // ①
      // ②
      System.out.println("n > 1919 && list.contains(obj)");
    } else {
      // ③
      System.out.println("Not the case that: n > 1919 && list.contains(obj)");
    }
  }
}
