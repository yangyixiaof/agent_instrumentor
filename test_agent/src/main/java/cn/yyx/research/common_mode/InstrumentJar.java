package cn.yyx.research.common_mode;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import cn.yyx.research.trace.instrument.SimpleInstrumenter;
import cn.yyx.research.util.FileIterator;
import cn.yyx.research.util.FileUtil;

public class InstrumentJar {
	
	public static final String work_dir = "InstrumentDirectory";
	public static final String backup_work_dir = "BackupInstrumentDirectory";
	
	public static SimpleInstrumenter inst = new SimpleInstrumenter();
	
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
		File wdir = new File(work_dir);
		String wdir_abs_path = wdir.getAbsolutePath();
		String wdir_abs_path_norm = wdir_abs_path.replace('\\', '/'); 
		FileIterator fi = new FileIterator(work_dir, "*.class");
		Iterator<File> fi_itr = fi.EachFileIterator();
		while (fi_itr.hasNext()) {
			File class_f = fi_itr.next();
			try {
				byte[] bytes = FileUtil.ReadBytesFromFile(class_f);
				String classname = class_f.getAbsolutePath().replace('\\', '/').substring(wdir_abs_path_norm.length());
				while (classname.startsWith("/")) {
					classname = classname.substring(1);
				}
				byte[] instrumented_bytes = inst.InstrumentOneClass(classname, bytes);
				FileUtil.WriteBytesToFile(instrumented_bytes, class_f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
