package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/27 - 15:25
 */
public interface CmsConfigRespository extends MongoRepository<CmsConfig, String> { }
