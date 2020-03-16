package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 10:02
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
}
