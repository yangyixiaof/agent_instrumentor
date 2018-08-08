package cn.yyx.research.trace.test;

import java.lang.reflect.Field;

public class HaHaJ {

	static {
		int n = 1;
		if (n > 191919) {
			System.out.println("static n>191919");
		} else {
			System.out.println("static !n>191919");
		}
	}

	public HaHaJ() {
	}

	public void test2(int x, int y) {
		if (x > y) {
			System.out.println("Great!");
		}
	}

	public int testInt(int x) {
		return x;
	}

	String testStr() {
		return null;
	}

	public static void main(String[] args) {
		try {
			Class<?> c = Class.forName("cn.yyx.research.trace_recorder.TraceRecorder");
			System.out.println(c);
			Field f = c.getDeclaredField("now_record");
			Boolean f_v1 = (Boolean)f.get(null);
			System.out.println("testing f_v1:" + f_v1);
			f.set(null, Boolean.TRUE);
			Boolean f_v2 = (Boolean)f.get(null);
			System.out.println("testing f_v2:" + f_v2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int x = 0;
		String s = "string_info";
		System.err.println(s);
		if (args.length > x) {
			System.err.println("hahah");
		}
		new HaHaJ().test2(x, 100);
	}
}
