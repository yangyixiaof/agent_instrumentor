// package cn.yyx.research.agent_mode;
//
// import java.lang.instrument.ClassFileTransformer;
// import java.lang.instrument.IllegalClassFormatException;
// import java.security.ProtectionDomain;
// import java.util.LinkedList;
// import java.util.List;
//
// import cn.yyx.research.trace.instrument.CmpInstrumenter;
//
/// ** @deprecated */
// public class CmpTransformer implements ClassFileTransformer {
//
//  List<String> filters = new LinkedList<String>();
//
//  public CmpTransformer(List<String> filters) {
//    //		for (String fileter : filters) {
//    //			System.out.println("filter:" + fileter);
//    //		}
//    this.filters.addAll(filters);
//  }
//
//  @Override
//  public byte[] transform(
//      ClassLoader loader,
//      String className,
//      Class<?> classBeingRedefined,
//      ProtectionDomain protectionDomain,
//      byte[] classfileBuffer)
//      throws IllegalClassFormatException {
//    //		System.out.println("class_name:" + className);
//    if (!InFilters(className)) {
//      return CmpInstrumenter.InstrumentOneClass(className, classfileBuffer);
//    }
//    return classfileBuffer;
//  }
//
//  protected boolean InFilters(String class_name) {
//    for (String filter : filters) {
//      if (class_name.startsWith(filter)) {
//        return true;
//      }
//    }
//    return false;
//  }
// }
