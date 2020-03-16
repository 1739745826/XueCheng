package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 14:22
 */
public interface SysDicthinaryRespository extends MongoRepository<SysDictionary, String> {
	// 根据类型查询
	SysDictionary findBydType(String type);
}
