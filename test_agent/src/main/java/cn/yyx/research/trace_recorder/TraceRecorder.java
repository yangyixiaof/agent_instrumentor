package cn.yyx.research.trace_recorder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/** Hello world! */
public class TraceRecorder {

  public static String trace_dir;
  //	= System.getProperty("user.home") + "/" + "trace.txt";

  private static StringBuffer buffer = new StringBuffer();

  static {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                new Runnable() {
                  public void run() {
                    Flush();
                  }
                }));
  }

  /**
   * 这些小函数是被插桩插进去的代码调用的！
   *
   * <p>见 cn.yyx.research.trace.instrument.CmpInstrumenter
   *
   * <p>TODO 所以可以在这种类里记全局变量来做过滤？搜搜 TraceRecorder 所有出现
   */
  public static void Append(String x) {
    buffer.append(x + "#");
  }

  public static void Append(int x) {
    buffer.append(x + "#");
  }

  public static void Append(float x) {
    buffer.append(x + "#");
  }

  public static void Append(long x) {
    buffer.append(x + "#");
  }

  public static void Append(double x) {
    buffer.append(x + "#");
  }

  public static void NewLine() {
    buffer.append(System.getProperty("line.separator"));
  }

  /** Ensure writing to file. */
  public static void Flush() {
    File f = new File(trace_dir + "/" + "trace.txt");

    FileWriter writer = null;
    try {
      writer = new FileWriter(f);
      writer.write(buffer.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
