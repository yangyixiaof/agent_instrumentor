package cn.yyx.labtask.test_agent_trace_reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TraceSerializer {

  public static final String user_home = System.getProperty("user.dir");

  public static void SerializeByIdentification(String identify, Object object) {
    try {
      File file = new File(user_home + "/Y_Y_X_t_r_a_c_e");
      if (!file.exists()) {
        file.mkdirs();
      }
      File seria_file = new File(file.getAbsolutePath() + "/" + identify);
      if (!seria_file.exists()) {
        seria_file.createNewFile();
      } else {
        System.err.println("Error! Sequence conflict! May be serious, may be not!");
      }
      ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(seria_file));
      oout.writeObject(object);
      oout.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Object DeserializeByIdentification(String identify) {
    Object res = null;
    try {
      File file = new File(user_home + "/Y_Y_X_t_r_a_c_e");
      if (!file.exists()) {
        return null;
      }
      File seria_file = new File(file.getAbsolutePath() + "/" + identify);
      if (!seria_file.exists()) {
        return null;
      }
      ObjectInputStream oin = new ObjectInputStream(new FileInputStream(seria_file));
      res = oin.readObject();
      oin.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }
}
