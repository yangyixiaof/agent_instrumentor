package cn.yyx.labtask.test_agent_trace_reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

/**
 * 从 trace 文本文件，解析出各个分支结点的信息
 *
 * <p>以前似乎试图让 this class 负责产生 reward，现在只做 parsing
 */
public class TraceReader {

  private static final String default_trace_file =
      System.getProperty("user.home") + "/" + "trace.txt";
  String specific_file = null;

  String previous_sequence_identify = null;
  String sequence_identify = null;

  /**
   * 读默认位置的 trace 文件的 wrapper constructor
   *
   * @param previous_sequence_identifier
   * @param current_sequence_identifier
   */
  public TraceReader(String previous_sequence_identifier, String current_sequence_identifier) {
    this(previous_sequence_identifier, current_sequence_identifier, default_trace_file);
  }

  public TraceReader(
      String previous_sequence_identifier,
      String current_sequence_identifier,
      String traceFilePath) {

    this.previous_sequence_identify = previous_sequence_identifier;
    this.sequence_identify = current_sequence_identifier;
    this.specific_file = traceFilePath;
  }

  //  static int enter = 0, exit = 0, operand = 0; // debug 时为了查配对的计数
  //  static int currentLineFrom1 = 0; // start from 1
  //  static String lastPop = null;

  /**
   * Main entry of this class.
   *
   * @param specific_file
   * @return
   */
  public Map<String, ValuesOfBranch> ReadFromTraceFile(String specific_file) {
    Stack<String> runtime_stack = new Stack<>();
    Map<String, ValuesOfBranch> branch_signature_to_info = new TreeMap<>();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(new File(specific_file)));
      String one_line;
      while ((one_line = br.readLine()) != null) {
        //        currentLineFrom1++;
        one_line = one_line.trim();
        if (!one_line.equals("")) {
          String[] parts = one_line.split(":");
          if (one_line.startsWith("@Method-Enter:")) {
            //            enter++;
            ProcessMethodEnter(parts[1], runtime_stack);
          }
          if (one_line.startsWith("@Method-Exit:")) {
            //            exit++;
            ProcessMethodExit(parts[1], runtime_stack);
          }
          if (one_line.startsWith("@Branch-Operand:")) {
            //            operand++;
            try {
              String operandPart = parts[3];
              String[] operandParts = operandPart.split("#");
              double op1 = Double.parseDouble(operandParts[1]);
              double op2 = Double.parseDouble(operandParts[2]);
              String enclosingMethod = runtime_stack.peek();
              int relativeOffset = Integer.parseInt(parts[1]);
              String cmpOperator = parts[2];
              ProcessBranchOperand(
                  enclosingMethod,
                  relativeOffset,
                  cmpOperator,
                  op1,
                  op2,
                  runtime_stack,
                  branch_signature_to_info);
            } catch (Exception e) {
              //              System.out.println("lastPop: " + lastPop);
              //              System.out.println("currentLineFrom1 " + currentLineFrom1);
              e.printStackTrace();
              System.exit(1);
            }
          }
        } else {
          break;
        }
      }

      //      // TODO 做啥的？
      //      TraceSerializer.SerializeByIdentification(sequence_identify,
      // branch_signature_to_info);
      //      // TODO 做啥的？
      //      @SuppressWarnings("unchecked")
      //      Map<String, ValuesOfBranch> previous_branch_signature =
      //          (Map<String, ValuesOfBranch>)
      //              TraceSerializer.DeserializeByIdentification(previous_sequence_identify);
      //
      //      BuildGuidedModel(previous_branch_signature, branch_signature_to_info);

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
    return branch_signature_to_info;
  }

  private void ProcessMethodEnter(String method_name, Stack<String> runtime_stack) {
    runtime_stack.push(method_name);
  }

  /** pop 并检查配对儿。 */
  private void ProcessMethodExit(String method_name, Stack<String> runtime_stack) {
    String mname = runtime_stack.pop();
    //    lastPop = mname;
    if (!mname.equals(method_name)) {
      System.err.printf("very strange! stack not valid! !%s.equals(%s)\n", mname, method_name);
      System.exit(1);
    }
  }

  //	private void ProcessBranchOperand(int relative_offset, long branch_value1, long branch_value2)
  // {
  //		ValuesOfBranch vob = new ValuesOfBranch(branch_value1, branch_value2);
  //		String[] target = new String[runtime_stack.size()];
  //		runtime_stack.toArray(target);
  //		String catted = StringUtils.join(target, "#");
  //		branch_signature.put(catted, vob);
  //	}

  /**
   * @param enclosing_method
   * @param relative_offset
   * @param cmp_optr
   * @param branch_value1
   * @param branch_value2
   * @param runtime_stack
   * @param branch_signature 存放结果，在这里更新
   */
  private void ProcessBranchOperand(
      String enclosing_method,
      int relative_offset,
      String cmp_optr,
      double branch_value1,
      double branch_value2,
      Stack<String> runtime_stack,
      Map<String, ValuesOfBranch> branch_signature) {
    ValuesOfBranch vob =
        new ValuesOfBranch(
            enclosing_method, relative_offset, cmp_optr, branch_value1, branch_value2);
    // 把执行到该分支结点的 method 调用栈串起来，作为该分支的 branch_signature
    // 这个标识合理吗？？？TODO
    String[] target = new String[runtime_stack.size()];
    runtime_stack.toArray(target);
    String catted = StringUtils.join(target, "#");
    branch_signature.put(catted, vob);
  }
}
