package cn.yyx.research.test_agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.trace.instrument.CmpInstrumenter;

public class CmpTransformer implements ClassFileTransformer {

  // 没有使用……准备干啥的？（似乎曾经准备接收 agent 参数） 啊InFlower，过滤吗？
  private List<String> flowers = new LinkedList<>();

  public CmpTransformer(List<String> flowers) {
    //		for (String fileter : filters) {
    //			System.out.println("filter:" + fileter);
    //		}
    this.flowers.addAll(flowers);
  }

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
      throws IllegalClassFormatException {
    //		if (InFlower(className)) {
    return CmpInstrumenter.InstrumentOneClass(className, classfileBuffer);
    //		}
    //		return classfileBuffer;
  }

  //	protected boolean InFlower(String class_name) {
  //		for (String flower : flowers) {
  //			if (class_name.startsWith(flower)) {
  //				return true;
  //			}
  //		}
  //		return false;
  //	}

}
