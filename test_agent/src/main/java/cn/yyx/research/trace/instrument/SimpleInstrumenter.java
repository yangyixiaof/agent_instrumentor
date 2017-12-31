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

public class SimpleInstrumenter {

	public SimpleInstrumenter() {
	}

	public static byte[] InstrumentOneClass(byte[] input_class) {
		byte[] b = input_class;
		ByteArrayInputStream is = new ByteArrayInputStream(input_class);
		try {
			ClassReader cr = new ClassReader(is);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			ClassVisitor cv = new SimpleClassAdapter(cw);
			cr.accept(cv, ClassReader.SKIP_DEBUG);
			b = cw.toByteArray();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("HaHa.class"));
			fos.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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

	int relative_offset = 0;

	public SimpleMethodAdapter(final MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}

	@Override
	public void visitCode() {
		InstrumentLdcInsn("@Enter-Method-Code:");
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
				"(Ljava/lang/String;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);

		mv.visitCode();

		InstrumentLdcInsn("@Exit-Method-Code:");
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
				"(Ljava/lang/String;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		InstrumentLdcInsn("@Method-Enter:" + name);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
				"(Ljava/lang/String;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);
		relative_offset = 0;

		// instrument original instruction
		InstrumentThroughMethodVisitor(opcode, owner, name, desc, itf);

		InstrumentLdcInsn("@Method-Exit:" + name);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
				"(Ljava/lang/String;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);
		relative_offset = 0;
	}

	protected void InstrumentInsn(int opc) {
		// System.out.println("instructed_insn_opc:" + opc);
		mv.visitInsn(opc);
	}

	protected void InstrumentLdcInsn(Object insn) {
		// System.out.println("instructed_ldc_insn_insn:" + insn);
		mv.visitLdcInsn(insn);
	}

	protected void InstrumentThroughMethodVisitor(int opc, String qualified_logger, String method, String signature,
			boolean itf) {
		// System.out.println("instructed_opc:" + opc + ";qualified_logger:" +
		// qualified_logger + ";method:" + method
		// + ";signature:" + signature);
		mv.visitMethodInsn(opc, qualified_logger, method, signature, itf);
	}

}
