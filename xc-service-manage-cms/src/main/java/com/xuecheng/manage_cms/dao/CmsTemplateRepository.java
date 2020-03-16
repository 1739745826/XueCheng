package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/25 - 15:07
 */

public interface CmsTemplateRepository extends MongoRepository<CmsTemplate, String>{}
