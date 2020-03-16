package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 10:01
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite, String> {
}
