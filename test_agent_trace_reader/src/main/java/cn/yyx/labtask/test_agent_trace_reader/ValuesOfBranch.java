package cn.yyx.labtask.test_agent_trace_reader;

import java.io.Serializable;

public class ValuesOfBranch implements Serializable {
	
	private static final long serialVersionUID = 8325093925948900533L;
	
	private double branch_value1 = -1;
	private double branch_value2 = -1;
	
	public ValuesOfBranch(double branch_value1, double branch_value2) {
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
	
}
