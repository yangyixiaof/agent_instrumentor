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

	/**
	 * 用于 java.lang.instrument 插桩框架。
	 *
	 * @param class_name
	 * @param input_class
	 * @return
	 */
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

	/**
	 * 仅为了调起 TestInstrumentOneClass(String,String)。
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// TestInstrumentOneClass("cn/yyx/research/trace/test/HaHaJ",
		// "test_materials/HaHaJ.class");
		TestInstrumentOneClass("cn/yyx/research/trace/test/IfDNF", "test_materials/IfDNF.class");
	}

	/**
	 * 用 ClassAdapter 所指定的插桩配置，插桩 sourceClassName 输出到文件 targetClassFile。
	 *
	 * <p>
	 * E.g. TestInstrumentOneClass("cn/yyx/research/trace/test/HaHaJ",
	 * "test_materials/HaHaJ.class");
	 *
	 * @param sourceClassName
	 * @param targetClassFile
	 */
	public static void TestInstrumentOneClass(String sourceClassName, String targetClassFile) {
		try {
			ClassReader cr = new ClassReader(sourceClassName);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			ClassVisitor classAdapter = new ClassAdapter(cw, sourceClassName);
			cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
			byte[] b = cw.toByteArray();

			// print to file.
			File file = new File(targetClassFile);
			// File file = new File("test_materials/transformed.class");
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

	private String class_name = null;

	public ClassAdapter(final ClassVisitor cw, String class_name) {
		super(Opcodes.ASM5, cw);
		this.class_name = class_name;
	}

	// asm 为啥不搞个 AsmMethod 结构呢？( ﾟ∀。)
	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
			final String[] exceptions) {
		// System.out.println("---- visitMethod, name = " + name);
		MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new MethodAdapter(mv, name, desc, signature, this.class_name);
	}

	// @Override
	// public MethodVisitor visitMethod(
	// final int access,
	// final String name,
	// final String desc,
	// final String signature,
	// final String[] exceptions) {
	// MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
	// return mv == null ? null : new MethodAdapter(mv, this.class_name);
	// }
}

class MethodAdapter extends MethodVisitor {

	int object_relative_offset = 0;
	int branch_relative_offset = 0;
	String methodName;
	String methodDesc; // descriptor contains more useful information.
	String methodSignature; // why signature is often null?
	String class_name = null;

	// public MethodAdapter(final MethodVisitor mv, String class_name) {
	// super(Opcodes.ASM6, mv);
	// this.class_name = class_name;
	// }

	public MethodAdapter(final MethodVisitor mv, String methodName, String methodDesc, String methodSignature,
			String class_name) {
		super(Opcodes.ASM6, mv);
		this.methodName = methodName;
		this.methodDesc = methodDesc;
		this.methodSignature = methodSignature;
		this.class_name = class_name;
	}

//	@Override
//	public void visitVarInsn(int arg0, int arg1) {
//		if (class_name.equals("YYX_RDQ_TEST") || class_name.endsWith("HaHaJ")) {
//			switch (arg0) {
//			case Opcodes.ISTORE:
//				InstrumentLdcInsn("@Var#int");
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(Ljava/lang/String;)V", false);
//				InstrumentInsn(Opcodes.DUP);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(I)V", false);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"NewLine", "()V", false);
//				break;
//			case Opcodes.LSTORE:
//				InstrumentLdcInsn("@Var#long");
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(Ljava/lang/String;)V", false);
//				InstrumentInsn(Opcodes.DUP2);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(L)V", false);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"NewLine", "()V", false);
//				break;
//			case Opcodes.FSTORE:
//				InstrumentLdcInsn("@Var#float");
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(Ljava/lang/String;)V", false);
//				InstrumentInsn(Opcodes.DUP);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(F)V", false);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"NewLine", "()V", false);
//				break;
//			case Opcodes.DSTORE:
//				InstrumentLdcInsn("@Var#double");
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(Ljava/lang/String;)V", false);
//				InstrumentInsn(Opcodes.DUP2);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(D)V", false);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"NewLine", "()V", false);
//				break;
//			case Opcodes.ASTORE:
//				InstrumentLdcInsn("@Var");
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"Append", "(Ljava/lang/String;)V", false);
//				InstrumentInsn(Opcodes.DUP);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"AppendObjectVar", "(Ljava/lang/Object;)V", false);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//						"NewLine", "()V", false);
//				break;
//			}
//		}
//		super.visitVarInsn(arg0, arg1);
//	}
	
//	private void med() {
//		System.out.println("as");
//		int a = -1;
//		System.out.println(a);
//	}

	@Override
	public void visitCode() {
		object_relative_offset = 0;
		branch_relative_offset = 0;
		super.visitCode();
//		InstrumentLdcInsn("@Method-Enter:" + this.class_name + "~" + methodName + "~" + methodDesc
//				// + "~" + methodSignature
//				);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
//						"(Ljava/lang/String;)V", false);
//				InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
//						"()V", false);
	}

	// // owner 是类名！！
	// @Override
	// public void visitMethodInsn(int opcode, String owner, String name, String
	// desc, boolean itf)
	// {
	// // System.out.println("-------- visitMethodInsn, name = " + name);
	// // if (name.contains("main")) {
	// // System.out.println("++++++++ visitMethodInsn 回调，name.contains(\"main\")");
	// // }
	// // if (name.contains("clinit")) {
	// // System.out.println("++++++++ visitMethodInsn
	// 回调，name.contains(\"clinit\")");
	// // }
	// InstrumentLdcInsn(
	// "@Method-Enter:" + name
	// // + "~" + methodSignature
	// );
	// InstrumentThroughMethodVisitor(
	// Opcodes.INVOKESTATIC,
	// "cn/yyx/research/trace_recorder/TraceRecorder",
	// "Append",
	// "(Ljava/lang/String;)V",
	// false);
	// InstrumentThroughMethodVisitor(
	// Opcodes.INVOKESTATIC,
	// "cn/yyx/research/trace_recorder/TraceRecorder",
	// "NewLine",
	// "()V",
	// false);
	// relative_offset = 0;
	//
	// // instrument original instruction
	// InstrumentThroughMethodVisitor(opcode, owner, name, desc, itf);
	//
	// InstrumentLdcInsn("@Method-Exit:" + name);
	// InstrumentThroughMethodVisitor(
	// Opcodes.INVOKESTATIC,
	// "cn/yyx/research/trace_recorder/TraceRecorder",
	// "Append",
	// "(Ljava/lang/String;)V",
	// false);
	// InstrumentThroughMethodVisitor(
	// Opcodes.INVOKESTATIC,
	// "cn/yyx/research/trace_recorder/TraceRecorder",
	// "NewLine",
	// "()V",
	// false);
	// relative_offset = 0;
	// }

	private void PrintBranchTwoValues(String cmp, int length_for_two_words, int num_of_operands,
			String second_operand_default_value, boolean take_as_float_point) {
		// print tag information.
		branch_relative_offset++;
		InstrumentLdcInsn("@Branch-Operand#" + this.class_name + "#" + this.methodName + "#" + this.methodDesc + ":" + branch_relative_offset + ":" + cmp + ":");
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "Append",
				"(Ljava/lang/Object;)V", false);

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
					"Append", "(Ljava/lang/Object;)V", false);
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
						"Append", "(J)V", false);
			}
		}
	}
	
	private void PrintObjectAddress() {
		object_relative_offset++;
		InstrumentInsn(Opcodes.DUP);
		InstrumentLdcInsn("@Object-Address#" + this.class_name + "#" + this.methodName + "#" + this.methodDesc + ":" + object_relative_offset + ":");
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "AppendObjectAddress",
				"(Ljava/lang/Object;)V", false);
		InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder", "NewLine",
				"()V", false);
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
		if (opcode == Opcodes.IFNONNULL) {
			PrintBranchTwoValues("N$!=", 1, 1, "0", false);
		}
		if (opcode == Opcodes.IFNULL) {
			PrintBranchTwoValues("N$==", 1, 1, "0", false);
		}
		super.visitJumpInsn(opcode, label);
	}

	@Override
	public void visitInsn(int arg0) {
		// pop 2 values (must be of certain type), compare,
		// push int 0 if ==, push int 1 if >, push int -1 if <.

		// long
		if (arg0 == Opcodes.LCMP) {
//			System.out.println("executed! LCMP");
			PrintBranchTwoValues("L$CMP", 2, 2, null, false);
		}

		// double
		// G version: if at least one of the two operands is NaN, push 1.
		// L version: if at least one of the two operands is NaN, push -1.
		if (arg0 == Opcodes.DCMPG) {
			PrintBranchTwoValues("D$CMPG", 2, 2, null, true);
		}
		if (arg0 == Opcodes.DCMPL) {
			PrintBranchTwoValues("D$CMPL", 2, 2, null, true);
		}
		// float. G/L version like double's
		if (arg0 == Opcodes.FCMPG) {
			PrintBranchTwoValues("F$CMPG", 1, 2, null, true);
		}
		if (arg0 == Opcodes.FCMPL) {
			PrintBranchTwoValues("F$CMPL", 1, 2, null, true);
		}

		// insert before method return instruction
		// IRETURN, LRETURN, FRETURN, DRETURN, ARETURN, RETURN
//		List<Integer> returns = Arrays.asList(Opcodes.IRETURN, Opcodes.LRETURN, Opcodes.FRETURN, Opcodes.DRETURN,
//				Opcodes.ARETURN, // return a reference
//				Opcodes.RETURN // return void
//		);
//		if (returns.contains(arg0)) {
//			InstrumentLdcInsn("@Method-Exit:" + this.class_name + "~" + methodName + "~" + methodDesc
//			// + "~" + methodSignature
//			);
//			InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//					"Append", "(Ljava/lang/String;)V", false);
//			InstrumentThroughMethodVisitor(Opcodes.INVOKESTATIC, "cn/yyx/research/trace_recorder/TraceRecorder",
//					"NewLine", "()V", false);
//			relative_offset = 0;
//		}
		super.visitInsn(arg0);
	}
	
	@Override
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		if (opcode == Opcodes.ALOAD) {
			PrintObjectAddress();
		}
	}
	
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
//		System.out.println("visitMethodInsn:" + descriptor);
		if (descriptor != null && !descriptor.trim().endsWith(")V")) {
			PrintObjectAddress();
		}
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
		// qualified_logger +
		// ";method:" + method
		// + ";signature:" + signature);
		mv.visitMethodInsn(opc, qualified_logger, method, signature, itf);
	}
	
}
