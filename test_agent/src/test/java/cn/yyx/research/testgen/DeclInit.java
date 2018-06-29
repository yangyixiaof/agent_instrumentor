package cn.yyx.research.testgen;

/** TODO 看字节码区别 */
public class DeclInit {
  void f() {
    int n;
    n = 1;
    System.out.println(n);
  }

  void g() {
    int n = 1;
    System.out.println(n);
  }
}
