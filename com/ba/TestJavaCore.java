package com.ba;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/*
 * 
 */
public class TestJavaCore {

	public static void snakePrintMatrix(Object[][] matrix) {
		int x = 0, y = 0;
		int m = matrix.length;
		int n = matrix[0].length;
		out.println("m=" + m + ",n=" + n);
		while (x < m - x && y < n - y) {
			int i = x, j = y;
			while (j < n - y) {
				out.print(matrix[i][j] + ",");
				j++;
			}
			j--;
			i++;
			while (i < m - x) {
				out.print(matrix[i][j] + ",");
				i++;
			}
			i--;
			j--;
			while (j > y) {
				out.print(matrix[i][j] + ",");
				j--;
			}
			while (i > x) {
				out.print(matrix[i][j] + ",");
				i--;
			}
			x++;
			y++;
		}
	}

	public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
		List<List<Integer>> rs = new ArrayList<>();
		Stack<List<Integer>> stack = new Stack<>();
		for (int i = 0; i < graph[0].length; i++) {
			List<Integer> tmp = new ArrayList<Integer>();
			tmp.add(0);
			tmp.add(graph[0][i]);
			stack.push(tmp);
		}
		while (!stack.isEmpty()) {
			List<Integer> lis = stack.pop();

			int idx = lis.get(lis.size() - 1);
			if (idx == graph.length - 1)
				rs.add(lis);
			for (int i = 0; i < graph[idx].length; i++) {
				List<Integer> newlis = new ArrayList<>(lis);
				newlis.add(graph[idx][i]);
				stack.push(newlis);
			}
		}
		return rs;
	}

	public static void main(String[] args) {
		// E1 op=E2, equals E1 = (T)(E1 op E2)
		int x = 10;
		x += 4.6;
		out.println(x);

		// create arrayList from array
		Integer[] a = { 1, 2, 3, 4, 5 };
		List<Integer> b = new ArrayList<Integer>();
		Collections.addAll(b, a);
		b.add(6);
		for (Integer e : b)
			out.print(e + ",");
		out.println();

		// String
		String str1 = "abc"; // "abc" 存放于常量区
		String str2 = "abc";
		String str3 = str1 + ""; // str3指向的对象存放于堆区
		String str4 = "a" + "bc"; // 编译器自动优化为 "abc"
		out.println("str1==str2 : " + (str1 == str2));
		out.println("str1==str3 : " + (str1 == str3));
		out.println("str1==str4 : " + (str1 == str4));
		out.println("\"John\" == \"John\": " + ("John" == "John"));

		String str = "/127.0.0.1:57469";
		String[] split = str.split("[\\/.:]");
		for (int i = 0; i < split.length; i++)
			out.print(split[i]);
		out.println();

		Integer[][] nums = { { 1, 2, 3, 4, 5 }, { 6, 7, 8, 9, 10 }, { 11, 12, 13, 14, 15 }, { 16, 17, 18, 19, 20 },
				{ 21, 22, 23, 24, 25 } };
		snakePrintMatrix(nums);
		out.println();

		ExtraClass extraClass = new ExtraClass();
		extraClass.setName("Luis");
		out.println(extraClass.getName());

		out.println("system property,line.separator=" + System.getProperty("line.separator"));
		out.println("system property,user.dir=" + System.getProperty("user.dir"));
	}

}

class ExtraClass {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
