package cn.edu.tsinghua.thss.agent;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import sun.rmi.runtime.Log;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.security.ProtectionDomain;
import java.util.Map;

import static javax.swing.UIManager.put;

public class RewriterAgent {

    /*
    public static void agentmain(String agentArgs, Instrumentation instrumentation){ //代理类入口函数
        premain(agentArgs, instrumentation);
    }

    public static void premain(String agentArgs, Instrumentation instrumentation){ //代理类入口函数Java SE6

        try{
            DexClassTransformer dexClassTransformer = new DexClassTransformer(); //继承自ClassFileTransformer的自定义类，多class文件的加载
            instrumentation.addTransformer(dexClassTransformer, true);
        }
        catch(Throwable ex){
            System.out.println("Agent startup error");
            throw new RuntimeException(ex);
        }
    }

    private static final class DexClassTransformer implements ClassFileTransformer {
        private Log log;
        private final Map classVisitors;

        public byte[] transform(ClassLoader classLoader, String className, Class clazz, ProtectionDomain protectionDomain, byte bytes[]) throws IllegalClassFormatException{
            ClassVisitorFactory factory = (ClassVisitorFactory)classVisitors.get(className); //根据给定className匹配相应class的工厂对象
            if(factory != null){
                if(clazz != null && !factory.isRetransformOkay()){
                    return null;
                }
                try{
                    ClassReader cr = new ClassReader(bytes);  //生产者
                    ClassWriter cw = new PatchedClassWriter(3, classLoader); //消费者 自定义的ClassWriter
                    ClassAdapter adapter = factory.create(cw); //过滤器
                    cr.accept(adapter, 4);
                    return cw.toByteArray();
                }
                catch(SkipException ex) { }
                catch(Exception ex){
                    throw ex;
                }
            }
            return null;
        }

        public DexClassTransformer() throws URISyntaxException {
            classVisitors = new HashMap<String, ClassVisitorFactory>(){  //将需要转换的类放到map中，Map中为不同的类指定相应的工厂对象
                private static final long serialVersionUID = 1L;
                {
                    put("java/lang/ProcessBuilder", new ClassVisitorFactory(true){ //实现ClassVisitorFactory并重写create方法
                        public ClassAdapter create(ClassVisitor cv){
                            return RewriterAgent.createProcessBuilderClassAdapter(cv);
                        }
                    });
                }
            };
        }

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            return new byte[0];
        }
    }

    private static ClassAdapter createProcessBuilderClassAdapter(ClassVisitor cw, Log log){
        return new ClassAdapter(cw){
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String exceptions[]){
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if("start".equals(name)){
                    System.out.println("java.lang.ProcessBuilder实例的start方法被触发");
                }
                return mv;
            }
        };
    }
    */

}
