package cn.yyx.research.trace.instrument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cn.yyx.research.util.FileUtil;

public class SimpleInstrumenter {
	
	String user_dir = System.getProperty("user.home");
	String test_visual = user_dir + "/" + "TestVisual";
	
	public SimpleInstrumenter() {
		File tv = new File(test_visual);
		if (tv.exists()) {
			FileUtil.DeleteFile(tv);
		}
		tv.mkdirs();
	}
	
	public byte[] InstrumentOneClass(String classname, byte[] input_class) {
		System.out.println("Instrumenting begin:" + classname);
		byte[] b = input_class;
		ByteArrayInputStream is = new ByteArrayInputStream(input_class);
		try {
			ClassReader cr = new ClassReader(is);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ClassVisitor cv = new SimpleClassAdapter(cw);
			cr.accept(cv, ClassReader.SKIP_FRAMES);
			b = cw.toByteArray();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("Instrumenting end:" + classname);
		// int c_last_idx = classname.lastIndexOf('/');
		String filename = classname.replace('/', '.');
		FileOutputStream fos_transform = null;
		FileOutputStream fos_original = null;
		try {
			fos_transform = new FileOutputStream(new File(test_visual + "/" + "transform_" + filename + ".class"));
			fos_transform.write(b);
			fos_original = new FileOutputStream(new File(test_visual + "/" + "original_" + filename + ".class"));
			fos_original.write(input_class);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos_transform != null) {
				try {
					fos_transform.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos_original != null) {
				try {
					fos_original.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Instrumenting test post end:" + classname);
		return b;
	}

}

class SimpleClassAdapter extends ClassVisitor {

	public SimpleClassAdapter(final ClassVisitor cw) {
		super(Opcodes.ASM5, cw);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new SimpleMethodAdapter(mv);
	}
}

class SimpleMethodAdapter extends MethodVisitor {
	
	public SimpleMethodAdapter(final MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}

//	@Override
//	public void visitCode() {
//		InstrumentLdcInsn("@Enter-Method-Code:");
//		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
//				"(Ljava/lang/String;)V", false);
//		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
//				"()V", false);
//
//		mv.visitCode();
//
//		InstrumentLdcInsn("@Exit-Method-Code:");
//		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
//				"(Ljava/lang/String;)V", false);
//		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
//				"()V", false);
//	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		InstrumentFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		InstrumentLdcInsn("Invoking method:" + name);
		InstrumentMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		// instrument original instruction
		InstrumentMethodInsn(opcode, owner, name, desc, itf);
	}
	
	protected void InstrumentInsn(int opc) {
		// System.out.println("instructed_insn_opc:" + opc);
		mv.visitInsn(opc);
	}
	
	protected void InstrumentFieldInsn(int opc, String owner, String name, String desc) {
		// System.out.println("instructed_insn_opc:" + opc);
		mv.visitFieldInsn(opc, owner, name, desc);
	}

	protected void InstrumentLdcInsn(Object insn) {
		// System.out.println("instructed_ldc_insn_insn:" + insn);
		mv.visitLdcInsn(insn);
	}

	protected void InstrumentMethodInsn(int opc, String qualified_logger, String method, String signature,
			boolean itf) {
		// System.out.println("instructed_opc:" + opc + ";qualified_logger:" +
		// qualified_logger + ";method:" + method
		// + ";signature:" + signature);
		mv.visitMethodInsn(opc, qualified_logger, method, signature, itf);
	}

}
