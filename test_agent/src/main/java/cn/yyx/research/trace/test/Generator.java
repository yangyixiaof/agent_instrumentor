package cn.yyx.research.trace.test;

import cn.yyx.research.trace.instrument.CmpInstrumenter;

public class Generator {

  public static void main(String[] args) {
    CmpInstrumenter.TestInstrumentOneClass(
        "cn/yyx/research/trace/test/HaHaJ", "test_materials/HaHaJ.class");
  }
}
