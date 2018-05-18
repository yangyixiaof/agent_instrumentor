package cn.yyx.labtask.test_agent_trace_reader;

import java.io.Serializable;

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
