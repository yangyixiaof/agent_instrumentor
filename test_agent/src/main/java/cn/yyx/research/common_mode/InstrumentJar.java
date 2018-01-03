package cn.yyx.research.common_mode;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import cn.yyx.research.util.FileUtil;

public class InstrumentJar {
	
	public static final String work_dir = "InstrumentDirectory";
	public static final String backup_work_dir = "BackupInstrumentDirectory";
	
	static {
		File dir = new File(work_dir);
		if (dir.exists()) {
			FileUtil.DeleteFile(dir);
		}
		dir.mkdirs();
	}
	
	public static void main(String[] args) {
		String jar_file_path = args[0];
		File jar_file = new File(jar_file_path);
		String jar_name = jar_file.getName();
		System.out.println("jar_name:" + jar_name);
		
		File new_jar_path = new File(work_dir + "/" + jar_name);
		FileUtil.CopyFile(new File(jar_file_path), new File(work_dir + "/" + jar_name));
		ProcessBuilder pb = new ProcessBuilder("jar", "xvf ", jar_name + ".jar");
		pb.directory(new File(work_dir));
		try {
			Process p = pb.start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		FileUtil.DeleteFile(new_jar_path);
		
	}
	
}
