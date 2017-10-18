package cn.yyx.research.testgen;

public class AppMutant {
	
	public static void main(String[] args) {
		int n = 145;
		int s = 0;
		for (int i=0;i<n;i++) {
			if (i % 3 != 0) {
				s += 10;
			}
		}
		System.out.println(s);
	}
	
}
