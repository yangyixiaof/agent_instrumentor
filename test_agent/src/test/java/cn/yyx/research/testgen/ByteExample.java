package cn.yyx.research.testgen;

import junit.framework.TestCase;

public class ByteExample extends TestCase {
	
	public ByteExample(int x, int y) {
	}
	
	public static void main(String[] args) {
		// int x = 0;
		// double y = 0;
		// double z = x + y;
		// System.out.println("haha1:" + (x+y)); //  + ";haha2:" + y
		int[] arr = new int[]{1, 2};
		System.err.println(arr);
		System.err.println("haha haha.");
		// StringBuffer sb = 
		new StringBuffer().append(12);
		//sb.append(12);
		//sb.append(123);
		float p = 0.4F;
		System.err.println(p);
		char a = 0;
		System.err.println(a);
		long b = 0;
		System.err.println(b);
		if (p > b) {
			System.err.println("hahhaha.");
		}
		System.exit(1);
	}
	
}
