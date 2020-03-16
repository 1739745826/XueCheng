package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/4 - 19:10
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> { }
