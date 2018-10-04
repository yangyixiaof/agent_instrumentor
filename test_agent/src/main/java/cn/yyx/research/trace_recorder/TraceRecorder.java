package cn.yyx.research.trace_recorder;

public class TraceRecorder {
	
	public static final String line_separator = System.getProperty("line.separator");
	
	public static boolean now_record = false;
	public static boolean print_buffer_to_console = false;
//	public static String trace_dir = ".";
	// = System.getProperty("user.home") + "/" + "trace.txt";

	public static StringBuffer buffer = new StringBuffer();

	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				if (print_buffer_to_console) {
					FlushToConsole();
				}
			}
		}));
	}

	public static void AppendObjectVar(Object x) {
		if (now_record) {
			buffer.append(x.getClass().getName() + "#" + (x == null ? "null" : x.toString() + "#"));
		}
	}
	
	public static void AppendObjectAddress(Object x) {
		if (now_record) {
			int x_hash_code_like_address = System.identityHashCode(x);
			buffer.append(x_hash_code_like_address);
		}
	}

	public static void Append(Object x) {
		if (now_record) {
			buffer.append(x + "#");
		}
	}

	public static void Append(int x) {
		if (now_record) {
			buffer.append(x + "#");
		}
	}

	public static void Append(float x) {
		if (now_record) {
			buffer.append(x + "#");
		}
	}

	public static void Append(long x) {
		if (now_record) {
			buffer.append(x + "#");
		}
	}

	public static void Append(double x) {
		if (now_record) {
			buffer.append(x + "#");
		}
	}

	public static void NewLine() {
		if (now_record) {
			buffer.append(line_separator);
		}
	}
	
	public static void PrintOperationStartUp() {
		if (now_record) {
			buffer.append("$OperationStartUp$" + line_separator);
		}
	}
	
	public static void FlushToConsole() {
		System.out.println(line_separator + "The last TraceRecorder.buffer.toString():" + line_separator + buffer.toString());
//		File f = new File(trace_dir + "/" + "trace.txt");
//		FileWriter writer = null;
//		try {
//			writer = new FileWriter(f);
//			writer.write(buffer.toString());
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			if (writer != null) {
//				try {
//					writer.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
}
