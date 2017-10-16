package cn.edu.tsinghua.thss.asm;

public class TestAddTimeClass {
    public static void main(String[] args) throws InterruptedException, NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        C c = new C();
        c.m();
        Class<?> cc = c.getClass();
        System.out.println(cc.getField("timer").get(c));
    }
}
