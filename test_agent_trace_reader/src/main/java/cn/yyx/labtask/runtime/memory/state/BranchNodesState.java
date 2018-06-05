package cn.yyx.labtask.runtime.memory.state;

import java.util.Map;
import java.util.TreeMap;

/** A data repository class, storing the coverage states of all visited (bytecode) branch nodes. */
public class BranchNodesState {

  // TODO 替换成 BranchNodeState
  private Map<String, Integer> branch_state = new TreeMap<>();
  private Map<BranchNodeID, BranchNodeState> states = new TreeMap<>();

  public BranchNodesState() {}

  public BranchNodeState getBranchState(BranchNodeID sig) {
    return states.get(sig);
  }

  public Integer GetBranchState(String sig) {
    return branch_state.get(sig);
  }

  public void PutBranchState(String sig, Integer state) {
    branch_state.put(sig, state);
  }

  public boolean BranchHasBeenIteratedOver(String sig) {
    Integer state = branch_state.get(sig);
    return state != null && state == 0;
  }
}
