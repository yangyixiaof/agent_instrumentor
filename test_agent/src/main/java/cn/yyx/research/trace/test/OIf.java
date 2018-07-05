package cn.yyx.research.trace.test;

public class OIf {
	
	public Object ReturnObject() {
		return null;
	}
	
	public static void main(String[] args) {
		OIf oif = new OIf();
		Object o = oif.ReturnObject();
		if (o == null) {
			System.out.println("null judgement executed!");
		}
	}
	
}
