package cn.yyx.research.util;

public class ArrayUtil {
	
	public static int[] GenerateIntArrayAccordingToMinMaxValue(int min, int max) {
		int[] arr = new int[max-min+1];
		for (int i=min; i<=max; i++) {
			arr[i-min] = i;
		}
		return arr;
	}
	
}
