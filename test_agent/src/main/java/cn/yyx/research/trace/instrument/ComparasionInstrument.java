package cn.yyx.research.trace.instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * deprecated?
 */
public class ComparasionInstrument extends ClassVisitor {

  public ComparasionInstrument() {
    super(Opcodes.ASM5);
  }

  @Override
  public void visit(
      int version,
      int access,
      String name,
      String signature,
      String superName,
      String[] interfaces) {
    System.out.println(name + " extends " + superName + " {");
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public MethodVisitor visitMethod(
      int access, String name, String desc, String signature, String[] exceptions) {
    System.out.println(" " + name + desc);
    return super.visitMethod(access, name, desc, signature, exceptions);
  }

  @Override
  public void visitEnd() {
    System.out.println("}");
    super.visitEnd();
  }
}
