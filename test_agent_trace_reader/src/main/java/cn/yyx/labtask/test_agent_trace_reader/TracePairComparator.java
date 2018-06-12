package cn.yyx.labtask.test_agent_trace_reader;

import cn.yyx.labtask.runtime.memory.state.BranchNodesState;
import cn.yyx.labtask.runtime.round.testgen.TestModel;

import java.util.*;

/**
 * 比较两个 Trace——这两个 trace 应是一个 sequence 变异前和变异后的版本分别执行产生的——产生 reward。
 *
 * <p>可以产生
 */
public class TracePairComparator {

  TestModel model = new TestModel();
  private double v1;

  /**
   * 大问题是 before after 里的 key 集不一样时怎么办 :(
   *
   * <p>大概要用 this class 存一些"已覆盖分支"等状态？
   *
   * @param before
   * @param after
   * @return
   */
  public Map<String, Double> compare(
      Map<String, ValuesOfBranch> before, Map<String, ValuesOfBranch> after) {
    Set<String> commonKeys = new TreeSet<>(before.keySet()); // copy, for the
    commonKeys.retainAll(after.keySet()); // in-place sets intersection.

    TreeMap<String, Double> result = new TreeMap<>();
    for (String b : commonKeys) {

    }

    return null;
  }

  /**
   * YYX Old wisdom...
   *
   * <p>何必在这儿搞位运算过早优化呢！
   *
   * @param previous_branch_signature
   * @param current_branch_signature
   * @return
   */
  public Map<String, Double> BuildGuidedModel(
      Map<String, ValuesOfBranch> previous_branch_signature,
      Map<String, ValuesOfBranch> current_branch_signature) {
    Map<String, Double> influence = new TreeMap<>();

    BranchNodesState branch_state = model.GetState();

    Set<String> pset = previous_branch_signature.keySet();
    for (String sig : pset) {
      if (branch_state.BranchHasBeenIteratedOver(sig)) {
        continue;
      }
      ValuesOfBranch previous_vob = previous_branch_signature.get(sig);
      ValuesOfBranch current_vob = current_branch_signature.get(sig);
      influence.put(sig, -1.);
      if (current_vob != null) {
        switch (current_vob.GetCmpOptr()) {
            // ``compare then store'' series
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

              double v1 = current_vob.GetBranchValue1();
              double v2 = current_vob.GetBranchValue2();
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
                          influence.put(sig, 1.);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influence.put(sig, 1.);
                        }
                      }
                      break;
                    case 3:
                      {
                        double gap = (v1 - v2) - (prev_v1 - prev_v2);
                        if (gap < 0) {
                          influence.put(sig, 1.);
                        }
                      }
                      break;
                  }
                }
                state_copy >>= 1;
                position++;
              }
            } // // ``compare then store'' series BLOCK
            break;
            // eq neq series... *8
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

              double v1 = current_vob.GetBranchValue1();
              double v2 = current_vob.GetBranchValue2();
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
                          influence.put(sig, 1.);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influence.put(sig, 1.);
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
            // ge, ge 0 *2
          case "I$>=":
          case "IZ$>=":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b11;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = current_vob.GetBranchValue1();
              double v2 = current_vob.GetBranchValue2();
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
                          influence.put(sig, 1.);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influence.put(sig, 1.);
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
            // le, le 0
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

                double v1 = current_vob.GetBranchValue1();
                double v2 = current_vob.GetBranchValue2();
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
                            influence.put(sig, 1.);
                          }
                        }
                        break;
                      case 2:
                        {
                          double gap = v1 - v2;
                          double prev_gap = prev_v1 - prev_v2;
                          if (prev_gap - gap > 0) {
                            influence.put(sig, 1.);
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
            // gt, gt 0
          case "I$>":
          case "IZ$>":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b11;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = current_vob.GetBranchValue1();
              double v2 = current_vob.GetBranchValue2();
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
                          influence.put(sig, 1.);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influence.put(sig, 1.);
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
            // lt, lt 0
          case "I$<":
          case "IZ$<":
            {
              Integer state = branch_state.GetBranchState(sig);
              if (state == null) {
                state = 0b11;
              }
              double prev_v1 = previous_vob.GetBranchValue1();
              double prev_v2 = previous_vob.GetBranchValue2();

              double v1 = current_vob.GetBranchValue1();
              double v2 = current_vob.GetBranchValue2();
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
                          influence.put(sig, 1.);
                        }
                      }
                      break;
                    case 2:
                      {
                        double gap = v1 - v2;
                        double prev_gap = prev_v1 - prev_v2;
                        if (prev_gap - gap > 0) {
                          influence.put(sig, 1.);
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
        } // switch (current_vob.GetCmpOptr())
      } // if (current_vob != null)
    } // for (String sig : pset)
    return influence;
  }
}
