package cn.yyx.labtask.test_agent_trace_reader;

import java.util.Map;

/**
 * 比较两个 Trace——这两个 trace 是一个 sequence 变异前和变异后的版本——产生 reward。
 *
 * <p>可以产生
 */
public class TracePairComparator {


  /**
   * 大问题是 before after 里的 key 集不一样时怎么办 :(
   *
   * @param before
   * @param after
   * @return
   */
  public static Map<String, Double> compare(
      Map<String, ValuesOfBranch> before, Map<String, ValuesOfBranch> after) {
    return null;
  }
}
