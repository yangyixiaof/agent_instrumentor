package cn.edu.tsinghua.thss;

public class test {

    public static transient ShadowVar rr_x;

    public static void main(String args[]){
        int var1 = 5;
//        String var1 = "Asfasdf";

        System.out.println(rr_x);
    }
}
