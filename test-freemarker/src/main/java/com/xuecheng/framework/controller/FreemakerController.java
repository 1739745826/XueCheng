package com.xuecheng.framework.controller;

import com.xuecheng.framework.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.*;


/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/27 - 11:14
 */
@Controller
@RequestMapping("/freemarker")
public class FreemakerController {
	@Autowired
	RestTemplate restTemplate;

	// 测试1
	@RequestMapping("/test1")
	public String test1(Model model) {
		model.addAttribute("name", "高志宏");

		Student stu1 = new Student();
		stu1.setName("小明");
		stu1.setAge(18);
		stu1.setMoney(1000.86f);
		stu1.setBirthday(new Date());
		Student stu2 = new Student();
		stu2.setName("小红");
		stu2.setMoney(200.1f);
		stu2.setAge(19);
		stu2.setBirthday(new Date());
		// 朋友列表
		List<Student> friends = new ArrayList<>();
		friends.add(stu1);
		// 第二名学生
		stu2.setFriends(friends);
		// 最好的朋友
		stu2.setBestFriend(stu1);

		List<Student> stus = new ArrayList<>();
		stus.add(stu1);
		stus.add(stu2);

		HashMap<String, Student> stuMap = new HashMap<>();
		stuMap.put("stu1", stu1);
		stuMap.put("stu2", stu2);

		// 提交数据
		model.addAttribute("stu1", stu1);
		model.addAttribute("stu2", stu2);
		model.addAttribute("stus", stus);
		model.addAttribute("friends", friends);
		model.addAttribute("stuMap", stuMap);


		// freemarker，基于resources/templates路径的模板
		return "test1";
	}


	@RequestMapping("/banner")
	public String banner(Map<String, Object> map) {
		// 使用restTemplate请求轮播图的模型数据
		ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31001/cms/config/getmodel/5a791725dd573c3574ee333f", Map.class);
		Map body = forEntity.getBody();
		// 设置模型数据
		map.put("model", body);
		return "index_banner";
	}

	@RequestMapping("/course")
	public String course(Map<String, Object> map) {
		ResponseEntity<Map> forEntity = restTemplate.getForEntity("http://localhost:31200/course/courseview/297e7c7c62b888f00162b8a7dec20000", Map.class);
		Map body = forEntity.getBody();
		map.putAll(body);
		return "course";
	}
}
