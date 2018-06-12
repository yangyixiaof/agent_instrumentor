package cn.yyx.research.testgen;

import java.util.Arrays;
import java.util.List;

/**
 * 短小精悍，要素丰富.
 *
 * <p>Let's cover all branches!
 */
public class ListStringIfFor {
  private static List<String> l = Arrays.asList("one", "two", "three", "four");

  private static void printNumOfIntersection(List<String> p) {
    int num = 0;
    for (String pe : p) {
      if (l.contains(pe)) {
        num++;
      }
    }
    if (num == 3) {
      System.out.println("Wow!! 3 common elements!");
    } else {
      System.out.printf("Well, %d common element(s).\n", num);
    }
  }

  public static void main(String[] args) {
    List<String> oneCommon = Arrays.asList("five", "three");
    //    List<String> threeCommon = Arrays.asList("three", "two", "one");
    printNumOfIntersection(oneCommon);
    //    printNumOfIntersection(threeCommon);
  }
}
