package cn.yyx.labtask.test_agent_trace_reader;

public class StatementReturn {
	
	Class<?> var_type = null;
	String var_value = null;
	
	public StatementReturn(Class<?> var_type, String var_value) {
		this.var_type = var_type;
		this.var_value = var_value;
	}
	
	public Class<?> GetVarType() {
		return var_type;
	}
	
	public String GetVarValue() {
		return var_value;
	}
	
}
