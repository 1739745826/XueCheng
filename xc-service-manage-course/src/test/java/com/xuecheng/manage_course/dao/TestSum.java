package com.xuecheng.manage_course.dao;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/13 - 11:18
 */
public class TestSum {
	public static void main(String[] args) {
		int sum = 0;
		for (int i = 1; i <= 99; i++) {
			if (i % 2 != 0) {
				sum += i;
			}
		}
		System.out.println(sum);
	}
}
