package cn.test.example;

public class HaHaJ {
	
	public HaHaJ() {
	}
	
	public void test2(int x, int y) {
		if (x > y) {
			System.out.println("Great!");
		}
	}
	
	public static void main(String[] args) {
		int x = -1;
		if (args.length > x) {
			System.out.println("hahah");
		}
		new HaHaJ().test2(x, 100);
	}
	
}
