package cn.yyx.research.trace_recorder.test;

import cn.yyx.research.trace_recorder.TraceRecorder;

/**
 * Hello world!
 *
 */
public class TestTraceRecorder {

	public static void main(String[] args) {
		int x = 0;
		System.out.println("Serial x:" + x + "Haha! Test begins!");
		TraceRecorder.Append(100);
		System.out.println("Test End!");
	}
	
}
