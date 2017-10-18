package cn.yyx.labtask.runtime.memory.state;

import java.util.Map;
import java.util.TreeMap;

public class BranchState {
	
	Map<String, Integer> branch_state = new TreeMap<String, Integer>();
	
	public BranchState() {
	}
	
	public Integer GetBranchState(String sig) {
		return branch_state.get(sig);
	}
	
	public void PutBranchState(String sig, Integer state) {
		branch_state.put(sig, state);
	}
	
	public boolean BranchHasBeenIteratedOver(String sig) {
		Integer state = branch_state.get(sig);
		if (state != null && state == 0) {
			return true;
		}
		return false;
	}
	
}
