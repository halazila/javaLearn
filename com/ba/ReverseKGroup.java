package com.ba;

import static java.lang.System.out;

public class ReverseKGroup {

	// Definition for singly-linked list.
	class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
			next = null;
		}
	};

	public static ListNode reverseGroup(ListNode head, int k) {
		if (head == null)
			return head;
		ListNode point;
		ListNode lhead, ltail, thead, ttail, cur, p;
		lhead = new ReverseKGroup().new ListNode(0);
		ltail = lhead;
		cur = point = head;
		int i;
		while (point != null) {
			i = k;
			do {
				point = point.next;
			} while (--i > 0 && point != null);
			if (i == 0) {
				ttail = head;
				cur = head.next;
				while (cur != point) {
					p = cur.next;
					cur.next = head;
					head = cur;
					cur = p;
				}
				// thead = head;
				// ltail->next = thead;
				ltail.next = head;
				ltail = ttail;
				head = cur;
				if (point == null)
					ttail.next = null;
			} else {
				head = cur;
				ltail.next = head;
			}
		}
		return lhead.next;
	}

	public static void main(String[] args) {
		ReverseKGroup test = new ReverseKGroup();
		ListNode head = test.new ListNode(1);
		ListNode tail = head;
		int k = 3;
		for (int i = 2; i < 13; i++) {
			tail.next = test.new ListNode(i);
			tail = tail.next;
		}
		tail = reverseGroup(head, k);
		while (tail != null) {
			out.print(tail.val + " ");
			tail = tail.next;
		}
	}
}
