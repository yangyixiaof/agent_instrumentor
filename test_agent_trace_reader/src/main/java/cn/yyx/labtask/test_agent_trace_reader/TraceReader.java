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

import cn.yyx.labtask.runtime.memory.state.BranchState;
import cn.yyx.labtask.runtime.round.testgen.TestModel;

public class TraceReader {

  private static final String default_trace_file =
      System.getProperty("user.home") + "/" + "trace.txt";
  String specific_file = null;
  TestModel model = null;
  String previous_sequence_identify = null;
  String sequence_identify = null;

  public TraceReader(TestModel model, String previous_sequence_identify, String sequence_identify) {
    this(model, previous_sequence_identify, sequence_identify, default_trace_file);
  }

  public TraceReader(
      TestModel model,
      String previous_sequence_identify,
      String sequence_identify,
      String traceFilePath) {
    this.model = model;
    this.previous_sequence_identify = previous_sequence_identify;
    this.sequence_identify = sequence_identify;
    this.specific_file = traceFilePath;
  }

  static int enter = 0, exit = 0, operand = 0;
  static int currentLineFrom1 = 0; // start from 1
  static String lastPop = null;

  public void ReadFromTraceFile() {
    Stack<String> runtime_stack = new Stack<String>();
    Map<String, ValuesOfBranch> branch_signature = new TreeMap<String, ValuesOfBranch>();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(new File(specific_file)));
      String one_line = null;
      while ((one_line = br.readLine()) != null) {
        currentLineFrom1++;
        one_line = one_line.trim();
        if (!one_line.equals("")) {
          String[] parts = one_line.split(":");
          if (one_line.startsWith("@Method-Enter:")) {
            enter++;
            ProcessMethodEnter(parts[1], runtime_stack);
          }
          if (one_line.startsWith("@Method-Exit:")) {
            exit++;
            ProcessMethodExit(parts[1], runtime_stack);
          }
          if (one_line.startsWith("@Branch-Operand:")) {
            operand++;
            try {
              String operandPart = parts[3];
              String[] operandParts = operandPart.split("#");
              double b1 = Double.parseDouble(operandParts[1]);
              double b2 = Double.parseDouble(operandParts[2]);
              ProcessBranchOperand(
                  runtime_stack.peek(),
                  Integer.parseInt(parts[1]),
                  parts[2],
                  b1,
                  b2,
                  runtime_stack,
                  branch_signature);
            } catch (Exception e) {
              System.out.println("lastPop: " + lastPop);
              System.out.println("currentLineFrom1 " + currentLineFrom1);
              e.printStackTrace();
              System.exit(1);
            }
          }
        } else {
          break;
        }
      }
      TraceSerializer.SerializeByIdentification(sequence_identify, branch_signature);
      @SuppressWarnings("unchecked")
      Map<String, ValuesOfBranch> previous_branch_signature =
          (Map<String, ValuesOfBranch>)
              TraceSerializer.DeserializeByIdentification(previous_sequence_identify);

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
    System.out.printf("!!!!!enter %d, exit %d, operand %d\n", enter, exit, operand);
  }

  private void ProcessMethodEnter(String method_name, Stack<String> runtime_stack) {
    runtime_stack.push(method_name);
  }

  private void ProcessMethodExit(String method_name, Stack<String> runtime_stack) {
    String mname = runtime_stack.pop();
    lastPop = mname;
    if (!mname.equals(method_name)) {
      System.err.println("very strange! stack not valid!");
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
    String[] target = new String[runtime_stack.size()];
    runtime_stack.toArray(target);
    String catted = StringUtils.join(target, "#");
    branch_signature.put(catted, vob);
  }

  private Map<String, Integer> BuildGuidedModel(
      Map<String, ValuesOfBranch> previous_branch_signature,
      Map<String, ValuesOfBranch> branch_signature) {
    Map<String, Integer> influcnce = new TreeMap<String, Integer>();

    BranchState branch_state = model.GetState();

    Set<String> pset = previous_branch_signature.keySet();
    Iterator<String> pitr = pset.iterator();
    while (pitr.hasNext()) {
      String sig = pitr.next();
      if (branch_state.BranchHasBeenIteratedOver(sig)) {
        continue;
      }
      ValuesOfBranch previous_vob = previous_branch_signature.get(sig);
      ValuesOfBranch vob = branch_signature.get(sig);
      influcnce.put(sig, -1);
      if (vob != null) {
        switch (vob.GetCmpOptr()) {
          case "D$CMPG":
          case "D$CMPL":
          case "F$CMPG":
          case "F$CMPL":
          case "L$CMP":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b111;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = vob.GetBranchValue1();
              double v2 = vob.GetBranchValue2();
              if (v1 == v2) {
                state &= 0b101;
              } else {
                if (v1 > v2) {
                  state &= 0b110;
                } else {
                  state &= 0b011;
                }
              }
              branch_state.PutBranchState(sig, state);
              int state_copy = state;
              int position = 1;
              while (state_copy > 0) {
                int bit = state_copy & 0b1;
                if (bit == 1) {
                  switch (position) {
                    case 1:
                      {
                        double gap = (v1 - v2) - (prev_v1 - prev_v2);
                        if (gap > 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                    case 3:
                      {
                        double gap = (v1 - v2) - (prev_v1 - prev_v2);
                        if (gap < 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                  }
                }
                state_copy >>= 1;
                position++;
              }
            }
            break;
          case "I$==":
          case "I$!=":
          case "A$==":
          case "A$!=":
          case "IZ$==":
          case "IZ$!=":
          case "N$!=":
          case "N$==":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b11;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = vob.GetBranchValue1();
              double v2 = vob.GetBranchValue2();
              if (v1 == v2) {
                state &= 0b01;
              } else {
                state &= 0b10;
              }
              branch_state.PutBranchState(sig, state);
              int state_copy = state;
              int position = 1;
              while (state_copy > 0) {
                int bit = state_copy & 0b1;
                if (bit == 1) {
                  switch (position) {
                    case 1:
                      {
                        double gap = v1 - v2;
                        if (gap != 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                  }
                }
                state_copy >>= 1;
                position++;
              }
            }
            break;
          case "I$>=":
          case "IZ$>=":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b11;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = vob.GetBranchValue1();
              double v2 = vob.GetBranchValue2();
              if (v1 >= v2) {
                state &= 0b10;
              } else {
                state &= 0b01;
              }
              branch_state.PutBranchState(sig, state);
              int state_copy = state;
              int position = 1;
              while (state_copy > 0) {
                int bit = state_copy & 0b1;
                if (bit == 1) {
                  switch (position) {
                    case 1:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap < 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                  }
                }
                state_copy >>= 1;
                position++;
              }
            }
            break;
          case "I$<=":
          case "IZ$<=":
            {
              {
                Integer state = branch_state.GetBranchState(sig);
                if (state == null) {
                  state = 0b11;
                }
                double prev_v1 = previous_vob.GetBranchValue1();
                double prev_v2 = previous_vob.GetBranchValue2();

                double v1 = vob.GetBranchValue1();
                double v2 = vob.GetBranchValue2();
                if (v1 <= v2) {
                  state &= 0b01;
                } else {
                  state &= 0b10;
                }
                branch_state.PutBranchState(sig, state);
                int state_copy = state;
                int position = 1;
                while (state_copy > 0) {
                  int bit = state_copy & 0b1;
                  if (bit == 1) {
                    switch (position) {
                      case 1:
                        {
                          double gap = v1 - v2;
                          double prev_gap = prev_v1 - prev_v2;
                          if (prev_gap - gap < 0) {
                            influcnce.put(sig, 1);
                          }
                        }
                        break;
                      case 2:
                        {
                          double gap = v1 - v2;
                          double prev_gap = prev_v1 - prev_v2;
                          if (prev_gap - gap > 0) {
                            influcnce.put(sig, 1);
                          }
                        }
                        break;
                    }
                  }
                  state_copy >>= 1;
                  position++;
                }
              }
            }
            break;
          case "I$>":
          case "IZ$>":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b11;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = vob.GetBranchValue1();
              double v2 = vob.GetBranchValue2();
              if (v1 > v2) {
                state &= 0b10;
              } else {
                state &= 0b01;
              }
              branch_state.PutBranchState(sig, state);
              int state_copy = state;
              int position = 1;
              while (state_copy > 0) {
                int bit = state_copy & 0b1;
                if (bit == 1) {
                  switch (position) {
                    case 1:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap < 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                  }
                }
                state_copy >>= 1;
                position++;
              }
            }
            break;
          case "I$<":
          case "IZ$<":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b11;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = vob.GetBranchValue1();
              double v2 = vob.GetBranchValue2();
              if (v1 < v2) {
                state &= 0b01;
              } else {
                state &= 0b10;
              }
              branch_state.PutBranchState(sig, state);
              int state_copy = state;
              int position = 1;
              while (state_copy > 0) {
                int bit = state_copy & 0b1;
                if (bit == 1) {
                  switch (position) {
                    case 1:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap < 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influcnce.put(sig, 1);
                        }
                      }
                      break;
                  }
                }
                state_copy >>= 1;
                position++;
              }
            }
            break;
        }
      }
    }
    return influcnce;
  }
}
