package com.xuecheng.framework.model;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * 标题：学生类
 * 作者：何处是归程
 * 时间：2020/2/27 - 11:12
 */
@Data
@ToString
public class Student {
	private String name;//姓名
	private int age;//年龄
	private Date birthday;//生日
	private Float money;//钱包
	private List<Student> friends;//朋友列表
	private Student bestFriend;//最好的朋友
}
