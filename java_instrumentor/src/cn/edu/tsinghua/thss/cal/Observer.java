package cn.edu.tsinghua.thss.cal;

public class Observer {

    public static Long idInt(int f, int s) {
        long l = f;
        l = l << 32;
        l += s;
        return l;
    }

    public static void main(String args[]){
        System.out.println(idInt(1,2));
    }

}
