package cn.yyx.labtask.test_agent_trace_reader;

import java.io.Serializable;

/**
 * .class 文件中（一行行 JVM 指令），method 定义块里出现的 CMP 系列指令，我们称为分支结点。this class 记录一个分支结点的属性。每个分支结点的属性有：
 * <li>所在的 method
 * <li>是这个 method 里的第几个分支结点：relative_offset
 * <li>该 CMP 指令对应的 Java 源代码里的运算符是什么（如 >=）TODO 是否一一对应？
 * <li>该 CMP 指令所比较的两个操作数的值，转成 double TODO null 情况？
 */
public class ValuesOfBranch implements Serializable {

  private static final long serialVersionUID = 8325093925948900533L;

  private String enclosing_method = null;
  private int relative_offset = -1;
  private String cmp_optr = null;
  private double branch_value1 = -1;
  private double branch_value2 = -1;

  public ValuesOfBranch(
      String enclosing_method,
      int relative_offset,
      String cmp_optr,
      double branch_value1,
      double branch_value2) {
    this.setEnclosing_method(enclosing_method);
    this.setRelative_offset(relative_offset);
    this.setCmp_optr(cmp_optr);
    this.setBranch_value1(branch_value1);
    this.setBranch_value2(branch_value2);
  }

  public double GetBranchValue1() {
    return branch_value1;
  }

  public void setBranch_value1(double branch_value1) {
    this.branch_value1 = branch_value1;
  }

  public double GetBranchValue2() {
    return branch_value2;
  }

  public void setBranch_value2(double branch_value2) {
    this.branch_value2 = branch_value2;
  }

  public String GetCmpOptr() {
    return cmp_optr;
  }

  public void setCmp_optr(String cmp_optr) {
    this.cmp_optr = cmp_optr;
  }

  public int GetRelativeOffset() {
    return relative_offset;
  }

  public void setRelative_offset(int relative_offset) {
    this.relative_offset = relative_offset;
  }

  public String GetEnclosingMethod() {
    return enclosing_method;
  }

  public void setEnclosing_method(String enclosing_method) {
    this.enclosing_method = enclosing_method;
  }
}
