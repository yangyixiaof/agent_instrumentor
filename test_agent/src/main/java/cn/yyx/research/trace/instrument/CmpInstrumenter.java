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

	public static byte[] InstrumentOneClass(String class_name, byte[] input_class) {
		System.out.println("class_name:" + class_name);
		byte[] b = input_class;
		ByteArrayInputStream is = new ByteArrayInputStream(input_class);
		try {
			ClassReader cr = new ClassReader(is);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			ClassVisitor cv = new ClassAdapter(cw, class_name);
			cr.accept(cv, ClassReader.SKIP_DEBUG);
			b = cw.toByteArray();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return b;
	}

	public static void TestInstrumentOneClass() {
		try {
			String test_class_name = "cn/yyx/research/trace/test/HaHaJ";
			ClassReader cr = new ClassReader(test_class_name);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ClassVisitor classAdapter = new ClassAdapter(cw, test_class_name);
			cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
			byte[] b = cw.toByteArray();

			// print to file.
			File file = new File("test_materials/transformed.class");
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
	
	String class_name = null;
	
	public ClassAdapter(final ClassVisitor cw, String class_name) {
		super(Opcodes.ASM5, cw);
		this.class_name = class_name;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new MethodAdapter(mv, this.class_name);
	}
}

class MethodAdapter extends MethodVisitor {
	
	int relative_offset = 0;
	String class_name = null;
	
	public MethodAdapter(final MethodVisitor mv, String class_name) {
		super(Opcodes.ASM5, mv);
		this.class_name = class_name;
	}
	
	@Override
	public void visitVarInsn(int arg0, int arg1) {
		if (class_name.equals("YYX_RDQ_TEST")) {// 
			switch (arg0) {
			case Opcodes.ISTORE:
				InstrumentLdcInsn("@Var#int");
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(Ljava/lang/String;)V", false);
				InstrumentInsn(Opcodes.DUP);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(I)V", false);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
						"()V", false);
				break;
			case Opcodes.LSTORE:
				InstrumentLdcInsn("@Var#long");
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(Ljava/lang/String;)V", false);
				InstrumentInsn(Opcodes.DUP2);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(L)V", false);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
						"()V", false);
				break;
			case Opcodes.FSTORE:
				InstrumentLdcInsn("@Var#float");
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(Ljava/lang/String;)V", false);
				InstrumentInsn(Opcodes.DUP);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(F)V", false);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
						"()V", false);
				break;
			case Opcodes.DSTORE:
				InstrumentLdcInsn("@Var#double");
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(Ljava/lang/String;)V", false);
				InstrumentInsn(Opcodes.DUP2);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(D)V", false);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
						"()V", false);
				break;
			case Opcodes.ASTORE:
				InstrumentLdcInsn("@Var");
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(Ljava/lang/String;)V", false);
				// TODO here exists some doubts
				InstrumentInsn(Opcodes.DUP);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(Ljava/lang/Object;)V", false);
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
						"()V", false);
				break;
			}
		}
		super.visitVarInsn(arg0, arg1);
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		InstrumentLdcInsn("@Method-Enter:" + name);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
				"Append", "(Ljava/lang/String;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);
		relative_offset = 0;

		// instrument original instruction
		InstrumentThroughMethodVisitor(opcode, owner, name, desc, itf);

		InstrumentLdcInsn("@Method-Exit:" + name);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
				"Append", "(Ljava/lang/String;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);
		relative_offset = 0;
	}

	private void PrintBranchTwoValues(String cmp, int length_for_two_words, int num_of_operands,
			String second_operand_default_value, boolean take_as_float_point) {
		// print tag information.
		relative_offset++;
		InstrumentLdcInsn("@Branch-Operand:" + relative_offset + ":" + cmp + ":");
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
				"(Ljava/lang/String;)V", false);

		// duplicate top of stack.
		if (num_of_operands == 1) {
			if (length_for_two_words == 1) {
				InstrumentInsn(Opcodes.DUP);
			} else {
				InstrumentInsn(Opcodes.DUP2);
			}
			PrintValueAccordingToLength(length_for_two_words, take_as_float_point);
			InstrumentLdcInsn("" + second_operand_default_value);
			InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
					"Append", "(Ljava/lang/String;)V", false);
		} else {
			if (length_for_two_words == 1) {
				InstrumentInsn(Opcodes.DUP_X1);
				PrintValueAccordingToLength(length_for_two_words, take_as_float_point);
				InstrumentInsn(Opcodes.DUP_X1);
				PrintValueAccordingToLength(length_for_two_words, take_as_float_point);
			} else {
				InstrumentInsn(Opcodes.DUP2_X2);
				PrintValueAccordingToLength(length_for_two_words, take_as_float_point);
				InstrumentInsn(Opcodes.DUP2_X2);
				PrintValueAccordingToLength(length_for_two_words, take_as_float_point);
			}
		}
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);
	}

	private void PrintValueAccordingToLength(int length, boolean take_as_float_point) {
		if (length == 1) {
			if (take_as_float_point) {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(F)V", false);
			} else {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(I)V", false);
			}
		} else {
			if (take_as_float_point) {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(D)V", false);
			} else {
				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
						"Append", "(L)V", false);
			}
		}
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		// two operands.
		if (opcode == Opcodes.IF_ICMPEQ) {
			PrintBranchTwoValues("I$==", 1, 2, null, false);
		}
		if (opcode == Opcodes.IF_ICMPNE) {
			PrintBranchTwoValues("I$!=", 1, 2, null, false);
		}
		if (opcode == Opcodes.IF_ACMPEQ) {
			PrintBranchTwoValues("A$==", 1, 2, null, false);
		}
		if (opcode == Opcodes.IF_ACMPNE) {
			PrintBranchTwoValues("A$!=", 1, 2, null, false);
		}
		if (opcode == Opcodes.IF_ICMPGE) {
			PrintBranchTwoValues("I$>=", 1, 2, null, false);
		}
		if (opcode == Opcodes.IF_ICMPGT) {
			PrintBranchTwoValues("I$>", 1, 2, null, false);
		}
		if (opcode == Opcodes.IF_ICMPLE) {
			PrintBranchTwoValues("I$<=", 1, 2, null, false);
		}
		if (opcode == Opcodes.IF_ICMPLT) {
			PrintBranchTwoValues("I$<", 1, 2, null, false);
		}
		// one operand.
		if (opcode == Opcodes.IFEQ) {
			PrintBranchTwoValues("IZ$==", 1, 1, "0", false);
		}
		if (opcode == Opcodes.IFNE) {
			PrintBranchTwoValues("IZ$!=", 1, 1, "0", false);
		}
		if (opcode == Opcodes.IFGE) {
			PrintBranchTwoValues("IZ$>=", 1, 1, "0", false);
		}
		if (opcode == Opcodes.IFGT) {
			PrintBranchTwoValues("IZ$>", 1, 1, "0", false);
		}
		if (opcode == Opcodes.IFLE) {
			PrintBranchTwoValues("IZ$<=", 1, 1, "0", false);
		}
		if (opcode == Opcodes.IFLT) {
			PrintBranchTwoValues("IZ$<", 1, 1, "0", false);
		}
		super.visitJumpInsn(opcode, label);
		if (opcode == Opcodes.IFNONNULL) {
			PrintBranchTwoValues("N$!=", 1, 1, "1", false);
		}
		if (opcode == Opcodes.IFNULL) {
			PrintBranchTwoValues("N$==", 1, 1, "1", false);
		}
	}

	@Override
	public void visitInsn(int arg0) {
		if (arg0 == Opcodes.DCMPG) {
			PrintBranchTwoValues("D$CMPG", 2, 2, null, true);
		}
		if (arg0 == Opcodes.DCMPL) {
			PrintBranchTwoValues("D$CMPL", 2, 2, null, true);
		}
		if (arg0 == Opcodes.FCMPG) {
			PrintBranchTwoValues("F$CMPG", 1, 2, null, true);
		}
		if (arg0 == Opcodes.FCMPG) {
			PrintBranchTwoValues("F$CMPL", 1, 2, null, true);
		}
		if (arg0 == Opcodes.LCMP) {
			PrintBranchTwoValues("L$CMP", 2, 2, null, false);
		}
		super.visitInsn(arg0);
	}

	protected void InstrumentInsn(int opc) {
		// System.out.println("instructed_insn_opc:" + opc);
		mv.visitInsn(opc);
	}

	protected void InstrumentLdcInsn(Object insn) {
		// System.out.println("instructed_ldc_insn_insn:" + insn);
		mv.visitLdcInsn(insn);
	}

	protected void InstrumentThroughMethodVisitor(int opc, String qualified_logger, String method, String signature, boolean itf) {
		// System.out.println("instructed_opc:" + opc + ";qualified_logger:" + qualified_logger + ";method:" + method
		//		+ ";signature:" + signature);
		mv.visitMethodInsn(opc, qualified_logger, method, signature, itf);
	}

}
