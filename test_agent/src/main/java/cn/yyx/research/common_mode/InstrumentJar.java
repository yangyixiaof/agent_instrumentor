package cn.yyx.research.common_mode;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;

import cn.yyx.research.trace.instrument.SimpleInstrumenter;
import cn.yyx.research.util.FileIterator;
import cn.yyx.research.util.FileUtil;

public class InstrumentJar {

	public static final String dex_work_dir = "DexInstrumentDirectory";
	public static final String work_dir = "InstrumentDirectory";
	// public static final String backup_work_dir = "BackupInstrumentDirectory";

	public static final String dex2jar = "/home/ren/MyProject/Instrument/agent_instrumentor/test_agent/commands/dex2jar/d2j-dex2jar.sh";
	public static final String jar2dex = "/home/ren/MyProject/Instrument/agent_instrumentor/test_agent/commands/dex2jar/d2j-jar2dex.sh";

	public static SimpleInstrumenter inst = new SimpleInstrumenter();

	static {
		// work_dir
		File dir = new File(dex_work_dir);
		if (dir.exists()) {
			FileUtil.DeleteFile(dir);
		}
		dir.mkdirs();
	}

	public static void ReadZipFile(String file) {
		ZipFile zf = null;
		ZipInputStream zin = null;
		try {
			zf = new ZipFile(file);
			InputStream in = new BufferedInputStream(new FileInputStream(file));
			zin = new ZipInputStream(in);
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null) {
				try {
					if (!ze.isDirectory()) {
						String norm_name = ze.getName().replace('\\', '/');
						if (!norm_name.contains("/") && norm_name.endsWith(".dex")) {
							System.out.println("file - " + norm_name + "; " + ze.getName() + " : " + ze.getSize() + " bytes");
							InputStream is = zf.getInputStream(ze);
							String unzipped_dex_file_path = dex_work_dir + "/" + norm_name;
							FileOutputStream fos = new FileOutputStream(new File(unzipped_dex_file_path));
							try {
								byte[] buf = new byte[1024];
								int length = 0;
								while ((length = is.read(buf)) != -1) {
									fos.write(buf, 0, length);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							is.close();
							fos.close();
							
							{
								ProcessBuilder pb = new ProcessBuilder(dex2jar, norm_name);
								pb.directory(new File(dex_work_dir));
								pb.redirectOutput(Redirect.INHERIT);
								pb.redirectError(Redirect.INHERIT);
								try {
									Process p = pb.start();
									p.waitFor();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
							int index = norm_name.lastIndexOf(".dex");
							String raw_norm_name = norm_name.substring(0, index);
							String jar_name = raw_norm_name + "-dex2jar.jar";
							InstrumentOneJar(dex_work_dir + "/" + jar_name, 100);
							
							System.out.println("generate instrumented dex:d2j-jar2dex.sh " + jar_name);
							{
								ProcessBuilder pb = new ProcessBuilder(jar2dex, jar_name);
								pb.directory(new File(dex_work_dir));
								pb.redirectOutput(Redirect.INHERIT);
								pb.redirectError(Redirect.INHERIT);
								try {
									Process p = pb.start();
									p.waitFor();
								} catch (IOException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zf != null) {
				try {
					zf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (zin != null) {
				try {
					zin.closeEntry();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void InstrumentOneJar(String jar_file_path, int max_instrument_file_num) {
		File jar_file = new File(jar_file_path);
		String jar_name = jar_file.getName();
		
		System.out.println("Deleting existing directory:" + work_dir);
		File dir = new File(work_dir);
		if (dir.exists()) {
			FileUtil.DeleteFile(dir);
		}
		dir.mkdirs();
		File new_jar_path = new File(work_dir + "/" + jar_name);
		FileUtil.CopyFile(new File(jar_file_path), new_jar_path);
		
		System.out.println("Depackaging " + jar_name);
		{
			ProcessBuilder pb = new ProcessBuilder("jar", "xvf", jar_name);
			pb.directory(new File(work_dir));
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			try {
				Process p = pb.start();
				p.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Deleting " + new_jar_path);
		FileUtil.DeleteFile(new_jar_path);
		
		File wdir = new File(work_dir);
		String wdir_abs_path = wdir.getAbsolutePath();
		String wdir_abs_path_norm = wdir_abs_path.replace('\\', '/');
		FileIterator fi = new FileIterator(work_dir, ".*\\.class");
		Iterator<File> fi_itr = fi.EachFileIterator();
		int count = 0;
		while (fi_itr.hasNext()) {
			File class_f = fi_itr.next();
			System.out.println("Instrumenting " + class_f.getName());
			try {
				byte[] bytes = FileUtil.ReadBytesFromFile(class_f);
				String classname = class_f.getCanonicalPath().replace('\\', '/').substring(wdir_abs_path_norm.length());
				while (classname.startsWith("/")) {
					classname = classname.substring(1);
				}
				byte[] instrumented_bytes = inst.InstrumentOneClass(classname, bytes);
				FileUtil.WriteBytesToFile(instrumented_bytes, class_f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			count++;
			if (max_instrument_file_num >= 0 && count >= max_instrument_file_num) {
				System.out.println("We will stop soon!");
				break;
			}
		}
		System.out.println("Repackaging " + jar_name);
		{
			ProcessBuilder pb = new ProcessBuilder("jar", "cvf", jar_name, ".");
			pb.directory(new File(work_dir));
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			try {
				Process p = pb.start();
				p.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Replacing original jar:" + jar_file_path);
		FileUtil.CopyFile(new File(work_dir + "/" + jar_name), new File(jar_file_path));
	}

    public static void InstrumentOneJar(String jar_file_path) {
        File jar_file = new File(jar_file_path);
        String jar_name = jar_file.getName();

        System.out.println("Deleting existing directory:" + work_dir);
        File dir = new File(work_dir);
        if (dir.exists()) {
            FileUtil.DeleteFile(dir);
        }
        dir.mkdirs();
        File new_jar_path = new File(work_dir + "/" + jar_name);
        FileUtil.CopyFile(new File(jar_file_path), new_jar_path);

        System.out.println("Depackaging " + jar_name);
        {
            ProcessBuilder pb = new ProcessBuilder("jar", "xvf", jar_name);
            pb.directory(new File(work_dir));
            pb.redirectOutput(Redirect.INHERIT);
            pb.redirectError(Redirect.INHERIT);
            try {
                Process p = pb.start();
                p.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Deleting " + new_jar_path);
        FileUtil.DeleteFile(new_jar_path);

        File wdir = new File(work_dir);
        String wdir_abs_path = wdir.getAbsolutePath();
        String wdir_abs_path_norm = wdir_abs_path.replace('\\', '/');
        FileIterator fi = new FileIterator(work_dir, ".*\\.class");
        Iterator<File> fi_itr = fi.EachFileIterator();
        int count = 0;
        while (fi_itr.hasNext()) {
            File class_f = fi_itr.next();
            System.out.println("Instrumenting " + class_f.getName());
            try {
                byte[] bytes = FileUtil.ReadBytesFromFile(class_f);
                String classname = class_f.getCanonicalPath().replace('\\', '/').substring(wdir_abs_path_norm.length());
                while (classname.startsWith("/")) {
                    classname = classname.substring(1);
                }
//                byte[] instrumented_bytes = inst.InstrumentOneClass(classname, bytes);

                final IRuntime runtime = new LoggerRuntime();
                final Instrumenter instr = new Instrumenter(runtime);
                final byte[] instrumented_bytes = instr.instrument(bytes, classname);


                FileUtil.WriteBytesToFile(instrumented_bytes, class_f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
        }
        System.out.println("Repackaging " + jar_name);
        {
            ProcessBuilder pb = new ProcessBuilder("jar", "cvf", jar_name, ".");
            pb.directory(new File(work_dir));
            pb.redirectOutput(Redirect.INHERIT);
            pb.redirectError(Redirect.INHERIT);
            try {
                Process p = pb.start();
                p.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Replacing original jar:" + jar_file_path);
        FileUtil.CopyFile(new File(work_dir + "/" + jar_name), new File(jar_file_path));
    }

	public static void main(String[] args) {
		String apk_file_path = args[0];
		ReadZipFile(apk_file_path);
		return;
	}

}
