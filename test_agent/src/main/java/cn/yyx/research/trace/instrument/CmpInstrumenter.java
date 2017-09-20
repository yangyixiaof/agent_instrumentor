package cn.yyx.research.trace.instrument;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class CmpInstrumenter {

	public static byte[] InstrumentOneClass(byte[] input_class) {
		byte[] b = input_class;
		ByteArrayInputStream is = new ByteArrayInputStream(input_class);
		try {
			ClassReader cr = new ClassReader(is);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			ClassVisitor cv = new ClassAdapter(cw);
			cr.accept(cv, ClassReader.SKIP_DEBUG);
			b = cw.toByteArray();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return b;
	}

	public static void TestInstrumentOneClass() {
		try {
			ClassReader cr = new ClassReader("cn/yyx/research/trace/test/HaHaJ");
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ClassVisitor classAdapter = new ClassAdapter(cw);
			cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
			byte[] b = cw.toByteArray();
			
			// print to file.
			File file = new File("C.class");
			FileOutputStream fout = new FileOutputStream(file);
			fout.write(b);
			fout.close();
			System.out.println("success!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

class ClassAdapter extends ClassVisitor {

	public ClassAdapter(final ClassVisitor cw) {
		super(Opcodes.ASM5, cw);
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new MethodAdapter(mv);
	}
}

class MethodAdapter extends MethodVisitor {

	public MethodAdapter(final MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
	}

	// @Override
	// public void visitMethodInsn(int opcode, String owner, String name, String
	// desc, boolean itf) {
	// /* System.err.println("CALL" + name); */
	// mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err",
	// "Ljava/io/PrintStream;");
	// mv.visitLdcInsn("CALL " + name);
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
	// "(Ljava/lang/String;)V", false);
	//
	// /* do call */
	// mv.visitMethodInsn(opcode, owner, name, desc, itf);
	//
	// /* System.err.println("RETURN" + name); */
	// mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err",
	// "Ljava/io/PrintStream;");
	// mv.visitLdcInsn("RETURN " + name);
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",
	// "(Ljava/lang/String;)V", false);
	// }

	private void PrintBranchTwoValues(String cmp, int length_for_two_words, boolean one_operand, String constant,
			boolean take_as_float_point) {
		// duplicate top of stack.
		if (one_operand) {
			if (length_for_two_words == 1) {
				InstrumentInsn(Opcodes.DUP);
			} else {
				InstrumentInsn(Opcodes.DUP2);
			}
		} else {
			if (length_for_two_words == 1) {
				InstrumentInsn(Opcodes.DUP_X1);
				InstrumentInsn(Opcodes.DUP_X1);
			} else {
				InstrumentInsn(Opcodes.DUP2_X2);
				InstrumentInsn(Opcodes.DUP2_X2);
			}
		}

		// print tag information.
		InstrumentLdcInsn("@Branch-Operand:" + cmp);
		// mv.visitMethodInsn(Opcodes.INVOKESTATIC,
		// "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
		// "(Ljava/lang/String;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
				"(Ljava/lang/String;)V");

		// print first information.
		PrintValueAccordingToLength(length_for_two_words, take_as_float_point);

		// print second information.
		if (one_operand) {
			// do nothing.
			InstrumentLdcInsn("" + constant);
			InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
					"Append", "(Ljava/lang/String;)V");
			// mv.visitMethodInsn(Opcodes.INVOKESTATIC,
			// "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
			// "(Ljava/lang/String;)V", false);
		} else {
			PrintValueAccordingToLength(length_for_two_words, take_as_float_point);
		}

		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V");
		// mv.visitMethodInsn(Opcodes.INVOKESTATIC,
		// "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine", "()V", false);

	}

	public void PrintValueAccordingToLength(int length, boolean take_as_float_point) {
		if (length == 1) {
			if (take_as_float_point) {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(F)V");
				// mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				// "cn/yyx/research/trace_recorder/TraceRecorder", "Append", "(F)V", false);
			} else {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(I)V");
				// mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				// "cn/yyx/research/trace_recorder/TraceRecorder", "Append", "(I)V", false);
			}
		} else {
			if (take_as_float_point) {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(D)V");
				// mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				// "cn/yyx/research/trace_recorder/TraceRecorder", "Append", "(D)V", false);
			} else {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(L)V");
				// mv.visitMethodInsn(Opcodes.INVOKESTATIC,
				// "cn/yyx/research/trace_recorder/TraceRecorder", "Append", "(L)V", false);
			}
		}
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		// two operands.
		if (opcode == Opcodes.IF_ICMPEQ) {
			PrintBranchTwoValues("I$==", 1, false, null, false);
		}
		if (opcode == Opcodes.IF_ICMPNE) {
			PrintBranchTwoValues("I$!=", 1, false, null, false);
		}
		if (opcode == Opcodes.IF_ACMPEQ) {
			PrintBranchTwoValues("A$==", 1, false, null, false);
		}
		if (opcode == Opcodes.IF_ACMPNE) {
			PrintBranchTwoValues("A$!=", 1, false, null, false);
		}
		if (opcode == Opcodes.IF_ICMPGE) {
			PrintBranchTwoValues("I$>=", 1, false, null, false);
		}
		if (opcode == Opcodes.IF_ICMPGT) {
			PrintBranchTwoValues("I$>", 1, false, null, false);
		}
		if (opcode == Opcodes.IF_ICMPLE) {
			PrintBranchTwoValues("I$<=", 1, false, null, false);
		}
		if (opcode == Opcodes.IF_ICMPLT) {
			PrintBranchTwoValues("I$<", 1, false, null, false);
		}
		// one operand.
		if (opcode == Opcodes.IFEQ) {
			PrintBranchTwoValues("I$==", 1, true, "0", false);
		}
		if (opcode == Opcodes.IFNE) {
			PrintBranchTwoValues("I$!=", 1, true, "0", false);
		}
		if (opcode == Opcodes.IFGE) {
			PrintBranchTwoValues("I$>=", 1, true, "0", false);
		}
		if (opcode == Opcodes.IFGT) {
			PrintBranchTwoValues("I$>", 1, true, "0", false);
		}
		if (opcode == Opcodes.IFLE) {
			PrintBranchTwoValues("I$<=", 1, true, "0", false);
		}
		if (opcode == Opcodes.IFLT) {
			PrintBranchTwoValues("I$<", 1, true, "0", false);
		}
		if (opcode == Opcodes.IFNONNULL) {
			PrintBranchTwoValues("N$!=", 1, true, "null", false);
		}
		if (opcode == Opcodes.IFNULL) {
			PrintBranchTwoValues("N$==", 1, true, "null", false);
		}
		super.visitJumpInsn(opcode, label);
	}

	@Override
	public void visitInsn(int arg0) {
		if (arg0 == Opcodes.DCMPG) {
			PrintBranchTwoValues("D$CMP", 2, false, null, true);
		}
		if (arg0 == Opcodes.DCMPL) {
			PrintBranchTwoValues("D$CMP", 2, false, null, true);
		}
		if (arg0 == Opcodes.FCMPG) {
			PrintBranchTwoValues("F$CMP", 1, false, null, true);
		}
		if (arg0 == Opcodes.FCMPG) {
			PrintBranchTwoValues("F$CMP", 1, false, null, true);
		}
		if (arg0 == Opcodes.LCMP) {
			PrintBranchTwoValues("L$CMP", 2, false, null, false);
		}
		super.visitInsn(arg0);
	}
	
	protected void InstrumentInsn(int opc) {
		System.out.println("instructed_insn_opc:" + opc);
		mv.visitInsn(opc);
	}
	
	protected void InstrumentLdcInsn(Object insn) {
		System.out.println("instructed_ldc_insn_insn:" + insn);
		mv.visitLdcInsn(insn);
	}

	protected void InstrumentThroughMethodVisitor(int opc, String qualified_logger, String method, String signature) {
		System.out.println("instructed_opc:" + opc + ";qualified_logger:" + qualified_logger + ";method:" + method
				+ ";signature:" + signature);
		mv.visitMethodInsn(opc, qualified_logger, method, signature, false);
	}

}
