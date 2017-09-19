package cn.edu.tsinghua.thss.agent;

import org.objectweb.asm.ClassWriter;

class PatchedClassWriter extends ClassWriter{
    private final ClassLoader classLoader;

    public PatchedClassWriter(int flags, ClassLoader classLoader){
        super(flags);
        this.classLoader = classLoader;
    }

    protected String getCommonSuperClass(String type1, String type2){ //返回共同的父类
        Class c;
        Class d;
        try{
            c = Class.forName(type1.replace('/', '.'), true, classLoader);
            d = Class.forName(type2.replace('/', '.'), true, classLoader);
        }
        catch(Exception e){
            throw new RuntimeException(e.toString());
        }
        if(c.isAssignableFrom(d))
            return type1;
        if(d.isAssignableFrom(c))
            return type2;
        if(c.isInterface() || d.isInterface())
            return "java/lang/Object";
        do
            c = c.getSuperclass();
        while(!c.isAssignableFrom(d));
        return c.getName().replace('.', '/');
    }
}