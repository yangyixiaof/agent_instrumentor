package cn.yyx.labtask.test_agent_trace_reader;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TraceInfo {
	
	Map<String, LinkedList<ValuesOfBranch>> vobs = new HashMap<String, LinkedList<ValuesOfBranch>>();
	List<StatementReturn> stmt_rets = new LinkedList<StatementReturn>();
	
	public TraceInfo() {
	}
	
	public void AddOneReturnOfStatement(StatementReturn sr) {
		stmt_rets.add(sr);
	}
	
	public void AddOneValueOfBranch(String sig_info, ValuesOfBranch vob) {
		LinkedList<ValuesOfBranch> vob_list = vobs.get(sig_info);
		if (vob_list == null) {
			vob_list = new LinkedList<ValuesOfBranch>();
			vobs.put(sig_info, vob_list);
		}
		vob_list.add(vob);
	}
	
}
