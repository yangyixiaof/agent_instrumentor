package cn.yyx.research.util;

import java.util.Arrays;

public class StringUtil {
	
	public static String IntArrayJoinToString(int[] arr, char delimeter) {
		StringBuilder sb = new StringBuilder("");
		for (int a : arr) {
			sb.append(a);
			sb.append(delimeter);
		}
		int length = sb.length();
		if (length > 0) {
			sb.deleteCharAt(length-1);
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		int[] arr = {12, 34, 546};
		String joined = StringUtil.IntArrayJoinToString(arr, '#');
		System.out.println(joined);
		// 12#34#546
		String[] recovered_arr = joined.split("#");
		System.out.println(Arrays.toString(recovered_arr));
		// [12, 34, 546]
		System.out.println(Arrays.toString("12#34##546#".split("#")));
		// [12, 34, , 546]
	}
	
}
