package cn.yyx.labtask.runtime.memory.state;

/**
 * A data bean class. The coverage state of a single (bytecode) branch node (e.g. IF_ICMPEQ,
 * IFNONNULL, LCMP).
 */
public class BranchNodeState {
  boolean trueCovered;
  boolean falseCovered;

  public BranchNodeState(boolean trueCovered, boolean falseCovered) {
    this.trueCovered = trueCovered;
    this.falseCovered = falseCovered;
  }
}
