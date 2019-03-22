package com.ba;

import static java.lang.System.out;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class TestOOM {

	static class RunnalTask implements Runnable {

		@Override
		public void run() {
			// Thread.yield();//禁止线程获取CPU资源
			String str = "new thread";
			try {
				Thread.sleep(Long.MAX_VALUE);
				System.out.println(str);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public int findSubstringInWraproundString(String p) {
			int[] p_int = new int[p.length()];
			int[] count = new int[26];
			int res, maxlen;
			for (int i = 0; i < p_int.length; i++)
				p_int[i] = p.charAt(i) - 'a';
			res = 0;
			maxlen = 0;
			for (int i = 0; i < p.length(); i++) {
				if (i > 0 && (p_int[i - 1] + 1) % 26 == p_int[i])
					maxlen++;
				else
					maxlen = 1;
				count[p_int[i]] = Math.max(count[p_int[i]], maxlen);
			}
			for (int i = 0; i < count.length; i++)
				res += count[i];
			return res;
		}

		// leetcode 532 K-diff pairs in an array
		public int findPairs(int[] nums, int k) {
			/*
			 * 解法1 用两个数的中位数来表示配对数，两组数如果中位数不同，则它们一定是不同的组合
			 */
			// Set<Double> mset = new HashSet<Double>();
			// for (int i = 0; i < nums.length - 1; i++) {
			// for (int j = i + 1; j < nums.length; j++) {
			// if (Math.abs(nums[i] - nums[j]) == k)
			// mset.add((nums[i] + nums[j]) / 2.0);
			// }
			// }
			// return mset.size();

			/*
			 * 解法2 HashMap 存储符合条件的数字对，并且在put的时候将小的数作为key，大的数作为value，以此去重。
			 */
			if (k < 0)
				return 0;
			Set<Integer> oriSet = new HashSet<Integer>();
			Map<Integer, Integer> pairMap = new HashMap<Integer, Integer>();

			for (int i = 0; i < nums.length; i++) {

				int tmp = nums[i] - k;
				if (oriSet.contains(tmp))
					pairMap.put(tmp, nums[i]);
				tmp = nums[i] + k;
				if (oriSet.contains(tmp))
					pairMap.put(nums[i], tmp);
				oriSet.add(nums[i]);
			}
			return pairMap.size();
		}

	}

	public static void main(String[] args) {
		String str = "";
		StringBuffer sb = new StringBuffer();
		// RunnalTask task = new RunnalTask();

		/*
		 * while (true) { // sb.append("1234");//out of memory new Thread(new
		 * RunnalTask()).start(); }
		 */
		out.println(1 << 30 - 1);
		int[] intarr = new int[10];
		out.println(intarr[11]);
		out.println("end of program");

		Queue<String> queue = new PriorityQueue<>();
		
	}

}
