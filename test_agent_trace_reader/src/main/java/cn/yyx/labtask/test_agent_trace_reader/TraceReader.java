package cn.yyx.labtask.test_agent_trace_reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class TraceReader {
	
	public static final String default_trace_file = System.getProperty("user.home") + "/" + "trace.txt";

	public void ReadFromDefaultTrace() {
		ReadFromSpecificFile(default_trace_file);
	}
	
	public void ReadFromSpecificFile(String specific_file) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(specific_file)));
			String one_line = null;
			while ((one_line = br.readLine()) != null) {
				one_line = one_line.trim();
				if (!one_line.equals("")) {
					String[] parts = one_line.split(":");
					if (one_line.startsWith("@Method-Enter:")) {
						ProcessMethodEnter(parts[1]);
					}
					if (one_line.startsWith("@Method-Exit:")) {
						ProcessMethodExit(parts[1]);
					}
					if (one_line.startsWith("@Branch-Operand:")) {
						try {
							double b1 = Double.parseDouble(parts[2]);
							double b2 = Double.parseDouble(parts[3]);
							ProcessBranchOperand(Integer.parseInt(parts[1]), b1, b2);
						} catch (Exception e) {
//							try {
//								long b1 = Long.parseLong(parts[2]);
//								long b2 = Long.parseLong(parts[3]);
//								ProcessBranchOperand(Integer.parseInt(parts[1]), b1, b2);
//							} catch (Exception e2) {
								e.printStackTrace();
								System.exit(1);
//							}
						}
					}
				} else {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	Stack<String> runtime_stack = new Stack<String>();
	Map<String, ValuesOfBranch> branch_signature = new TreeMap<String, ValuesOfBranch>();
	
	private void ProcessMethodEnter(String method_name) {
		runtime_stack.push(method_name);
	}
	
	private void ProcessMethodExit(String method_name) {
		String mname = runtime_stack.pop();
		if (!mname.equals(method_name)) {
			System.err.println("very strange! stack not valid!");
			System.exit(1);
		}
	}
	
//	private void ProcessBranchOperand(int relative_offset, long branch_value1, long branch_value2) {
//		ValuesOfBranch vob = new ValuesOfBranch(branch_value1, branch_value2);
//		String[] target = new String[runtime_stack.size()];
//		runtime_stack.toArray(target);
//		String catted = StringUtils.join(target, "#");
//		branch_signature.put(catted, vob);
//	}
	
	private void ProcessBranchOperand(int relative_offset, double branch_value1, double branch_value2) {
		ValuesOfBranch vob = new ValuesOfBranch(branch_value1, branch_value2);
		String[] target = new String[runtime_stack.size()];
		runtime_stack.toArray(target);
		String catted = StringUtils.join(target, "#");
		branch_signature.put(catted, vob);
	}
	
}
