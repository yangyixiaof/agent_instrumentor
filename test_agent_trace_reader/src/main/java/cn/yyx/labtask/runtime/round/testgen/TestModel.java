package cn.yyx.labtask.runtime.round.testgen;

import cn.yyx.labtask.runtime.memory.state.BranchNodesState;

public class TestModel {

  private BranchNodesState state = new BranchNodesState();

  public TestModel() {}

  public BranchNodesState GetState() {
    return state;
  }
}
