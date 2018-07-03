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
			Field f = c.getDeclaredField("now_record");
			Object f_v = f.get(null);
			System.out.println("testing f_v:" + f_v);
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
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
