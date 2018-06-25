package cn.yyx.research.testgen;

/**
 * Cut = CUT = Class Under Test
 *
 * <p>Simple: 无 static block，一个 instance method，里面只有一个 if 分支
 */
public class CutSimple {
  public boolean f(int n) {
    if (n > 191919) {
      System.out.println("> 191919");
      return true;
    } else {
      System.out.println("not > 191919");
      return false;
    }
  }
}
