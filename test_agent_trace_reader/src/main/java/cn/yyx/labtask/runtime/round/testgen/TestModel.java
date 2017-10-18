package cn.yyx.labtask.runtime.round.testgen;

import cn.yyx.labtask.runtime.memory.state.BranchState;

public class TestModel {
	
	private BranchState state = new BranchState();
	
	public TestModel() {
	}

	public BranchState GetState() {
		return state;
	}
	
}
