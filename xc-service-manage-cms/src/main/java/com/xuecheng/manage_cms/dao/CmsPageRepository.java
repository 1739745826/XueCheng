package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/25 - 15:07
 */
public interface CmsPageRepository extends MongoRepository<CmsPage, String> {
	// 根据页面名称查询
	CmsPage findByPageName(String pageName);

	// 根据页面名称 、站点id、和页面webpath查询
	CmsPage findByPageNameAndSiteIdAndPageWebPath(String pageName, String siteId, String pageWebPath);
}
