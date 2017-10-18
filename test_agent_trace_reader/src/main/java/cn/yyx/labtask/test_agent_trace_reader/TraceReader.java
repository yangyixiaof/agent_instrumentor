package cn.yyx.labtask.test_agent_trace_reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

public class TraceReader {
	
	public static final String default_trace_file = System.getProperty("user.home") + "/" + "trace.txt";
	String previous_sequence_identify = null;
	String sequence_identify = null;
	
	public TraceReader(String previous_sequence_identify, String sequence_identify) {
		this.previous_sequence_identify = previous_sequence_identify;
		this.sequence_identify = sequence_identify;
	}
	
//	public void ReadFromDefaultTrace(String sequence_identify) {
//		this.sequence_identify = sequence_identify;
//		ReadFromSpecificFile(default_trace_file);
//	}
	
	public void ReadFromDefaultTraceFile() {
		String specific_file = default_trace_file;
		Stack<String> runtime_stack = new Stack<String>();
		Map<String, ValuesOfBranch> branch_signature = new TreeMap<String, ValuesOfBranch>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(specific_file)));
			String one_line = null;
			while ((one_line = br.readLine()) != null) {
				one_line = one_line.trim();
				if (!one_line.equals("")) {
					String[] parts = one_line.split(":");
					if (one_line.startsWith("@Method-Enter:")) {
						ProcessMethodEnter(parts[1], runtime_stack);
					}
					if (one_line.startsWith("@Method-Exit:")) {
						ProcessMethodExit(parts[1], runtime_stack);
					}
					if (one_line.startsWith("@Branch-Operand:")) {
						try {
							double b1 = Double.parseDouble(parts[3]);
							double b2 = Double.parseDouble(parts[4]);
							ProcessBranchOperand(runtime_stack.peek(), Integer.parseInt(parts[1]), parts[2], b1, b2, runtime_stack, branch_signature);
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
			TraceSerializer.SerializeByIdentification(sequence_identify, branch_signature);
			@SuppressWarnings("unchecked")
			Map<String, ValuesOfBranch> previous_branch_signature = (Map<String, ValuesOfBranch>)TraceSerializer.DeserializeByIdentification(previous_sequence_identify);
			
			BuildGuidedModel(previous_branch_signature, branch_signature);
			
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
	
	private void ProcessMethodEnter(String method_name, Stack<String> runtime_stack) {
		runtime_stack.push(method_name);
	}
	
	private void ProcessMethodExit(String method_name, Stack<String> runtime_stack) {
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
	
	private void ProcessBranchOperand(String enclosing_method, int relative_offset, String cmp_optr, double branch_value1, double branch_value2, Stack<String> runtime_stack, Map<String, ValuesOfBranch> branch_signature) {
		ValuesOfBranch vob = new ValuesOfBranch(enclosing_method, relative_offset, cmp_optr, branch_value1, branch_value2);
		String[] target = new String[runtime_stack.size()];
		runtime_stack.toArray(target);
		String catted = StringUtils.join(target, "#");
		branch_signature.put(catted, vob);
	}
	
	private void BuildGuidedModel(Map<String, ValuesOfBranch> previous_branch_signature, Map<String, ValuesOfBranch> branch_signature) {
		Map<String, Integer> influcnce = new TreeMap<String, Integer>();
		
		Set<String> pset = previous_branch_signature.keySet();
		Iterator<String> pitr = pset.iterator();
		while (pitr.hasNext()) {
			String sig = pitr.next();
			ValuesOfBranch previous_vob = previous_branch_signature.get(sig);
			ValuesOfBranch vob = branch_signature.get(sig);
			if (vob == null) {
				influcnce.put(sig, -5);
			} else {
				switch (vob.GetCmpOptr())
				{
					case "D$CMPG":
					case "D$CMPL":
					case "F$CMPG":
					case "F$CMPL":
					case "L$CMP":
						
						break;
					case "I$==":
					case "I$!=":
					case "A$==":
					case "A$!=":
						
						break;
					case "I$>=":
					case "I$<=":
						
						break;
						
					case "I$>":
					case "I$<":
						
						break;
					case "IZ$==":
					case "IZ$!=":
						
						break;
					case "IZ$>=":
					case "IZ$<=":
						
						break;
					case "IZ$>":
					case "IZ$<":
						
						break;
					case "N$!=":
					case "N$==":
						
						break;
				}
			}
		}
	}
	
}
