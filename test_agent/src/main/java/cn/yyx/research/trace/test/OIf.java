package cn.yyx.research.trace.test;

public class OIf {
	
	public static Object ReturnObject() {
		return null;
	}
	
	public static void main(String[] args) {
		Object o = OIf.ReturnObject();
		if (o == null) {
			System.out.println("null judgement executed!");
		}
	}
	
}
