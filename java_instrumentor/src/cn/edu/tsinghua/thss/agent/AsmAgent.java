package cn.edu.tsinghua.thss.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class AsmAgent {
    public static void premain(String agentArgs, Instrumentation inst) {//该方法在main方法前运行
        inst.addTransformer(new ClassFileTransformer() {
            public byte[] transform(ClassLoader l, String name, Class<?> c, ProtectionDomain d, byte[] b) throws IllegalClassFormatException {
                ClassReader cr = new ClassReader(b);
                ClassWriter cw = new ClassWriter(cr, 0);
                ClassVisitor cv = new ChangeVersionAdapter(cw);
                cr.accept(cv, 0);
                return cw.toByteArray();
            }
        });
    }
}
