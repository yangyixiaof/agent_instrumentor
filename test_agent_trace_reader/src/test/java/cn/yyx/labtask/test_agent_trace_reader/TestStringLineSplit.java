package cn.yyx.labtask.test_agent_trace_reader;

public class TestStringLineSplit {
	
	public static void main(String[] args) {
		String text = "".trim();
		String[] lines = text.split("\\r?\\n");
		System.out.println("=== start ===");
		for (String line : lines) {
			System.out.println(line);
		}
		System.out.println("=== end ===");
	}
	
}
