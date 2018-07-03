package cn.yyx.labtask.test_agent_trace_reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 从 trace 文本文件，解析出各个分支结点的信息
 *
 * <p>
 * 以前似乎试图让 this class 负责产生 reward，现在只做 parsing
 */
public class TraceReader {

//	private static final String default_trace_file = System.getProperty("user.home") + "/" + "trace.txt";
//	String specific_file = null;

	String previous_sequence_identifier = null;
	String current_sequence_identifier = null;

	/**
	 * read default-position trace file's wrapper constructor
	 *
	 * @param previous_sequence_identifier
	 * @param current_sequence_identifier
	 */
	public TraceReader(String previous_sequence_identifier, String current_sequence_identifier) {
		this.previous_sequence_identifier = previous_sequence_identifier;
		this.current_sequence_identifier = current_sequence_identifier;
	}
	
//	static int enter = 0, exit = 0, branch_operand = 0; // debug to check whether enter and exit are matched.
//	static int currentLineFrom1 = 0; // start from 1
//	static String lastPop = null;

	/**
	 * Main entry of this class.
	 *
	 * @param specific_file
	 * @return
	 */
	public TraceInfo ReadFromTraceFile(String specific_file) {
//		Stack<String> runtime_stack = new Stack<>();
		TraceInfo ti = new TraceInfo();
//		Map<String, LinkedList<ValuesOfBranch>> branch_signature_to_info = new TreeMap<>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(specific_file)));
			String one_line;
			while ((one_line = br.readLine()) != null) {
//				currentLineFrom1++;
				one_line = one_line.trim();
				if (!one_line.equals("")) {
//					if (one_line.startsWith("@Method-Enter:")) {
//						String[] parts = one_line.split(":");
//						enter++;
//						ProcessMethodEnter(parts[1], runtime_stack);
//					}
//					if (one_line.startsWith("@Method-Exit:")) {
//						String[] parts = one_line.split(":");
//						exit++;
//						ProcessMethodExit(parts[1], runtime_stack);
//					}
					if (one_line.startsWith("@Branch-Operand_")) {
						String[] parts = one_line.split(":");
//						branch_operand++;
						try {
							String operandPart = parts[3];
							String[] operandParts = operandPart.split("#");
							double op1 = Double.parseDouble(operandParts[1]);
							double op2 = Double.parseDouble(operandParts[2]);
//							String enclosingMethod = runtime_stack.peek();
							int relativeOffset = Integer.parseInt(parts[1]);
							String cmpOperator = parts[2];
							ProcessBranchOperand(parts[0], relativeOffset, cmpOperator, op1, op2, ti);
						} catch (Exception e) {
							// System.out.println("lastPop: " + lastPop);
							// System.out.println("currentLineFrom1 " + currentLineFrom1);
							e.printStackTrace();
							System.exit(1);
						}
					}
					if (one_line.startsWith("@Var")) {
						String[] parts = one_line.split("#");
						String var_type = parts[1];
						String var_value = parts[2];
						Class<?> var_class = Class.forName(var_type);
						ti.AddOneReturnOfStatement(new StatementReturn(var_class, var_value));
					}
				} else {
					break;
				}
			}
			
			// TraceSerializer.SerializeByIdentification(current_sequence_identifier,
			// branch_signature_to_info);
			
			// @SuppressWarnings("unchecked")
			// Map<String, ValuesOfBranch> previous_branch_signature =
			// (Map<String, ValuesOfBranch>)
			// TraceSerializer.DeserializeByIdentification(previous_sequence_identifier);
			//
			// BuildGuidedModel(previous_branch_signature, branch_signature_to_info);

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
		return ti;
	}

//	private void ProcessMethodEnter(String method_name, Stack<String> runtime_stack) {
//		runtime_stack.push(method_name);
//	}
//	
//	private void ProcessMethodExit(String method_name, Stack<String> runtime_stack) {
//		String mname = runtime_stack.pop();
//		// lastPop = mname;
//		if (!mname.equals(method_name)) {
//			System.err.println("very strange! stack not valid! Should be the same:");
//			System.err.println(mname);
//			System.err.println(method_name);
//			System.err.println("currentLineFrom1: " + currentLineFrom1);
//			System.exit(1);
//		}
//	}
	
	/**
	 * @param enclosing_method
	 * @param relative_offset
	 * @param cmp_optr
	 * @param branch_value1
	 * @param branch_value2
	 * @param runtime_stack
	 * @param branch_signature
	 */
	private void ProcessBranchOperand(String enclosing_method, int relative_offset, String cmp_optr,
			double branch_value1, double branch_value2, TraceInfo ti) {
		ValuesOfBranch vob = new ValuesOfBranch(enclosing_method, relative_offset, cmp_optr, branch_value1,
				branch_value2);
		String catted = enclosing_method + "#" + relative_offset;
		ti.AddOneValueOfBranch(catted, vob);
	}
}
