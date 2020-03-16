package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 10:03
 */
@Service
public class PageService {
	@Autowired
	CmsPageRepository cmsPageRepository;
	@Autowired
	GridFsTemplate gridFsTemplate;
	@Autowired
	CmsSiteRepository cmsSiteRepository;
	@Autowired
	GridFSBucket gridFSBucket;

	private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);


	// 保存HTML页面到服务器的物理路径
	public void savePageToServerPath(String pageId) {
		// 根据pageId 查询cmsPage
		CmsPage cmsPage = findCmsPageById(pageId);

		// 得到html文件Id, 从cmsPage中获取htmlFileId的内容
		String htmlFileId = cmsPage.getHtmlFileId();

		// 从gridFS中查询html文件
		InputStream inputStream = this.getFileById(htmlFileId);
		if (inputStream == null) {
			LOGGER.error("getFileById InputStream is null , htmlFileId:{}", htmlFileId);
			return;
		}

		// 得到站点ID
		String siteId = cmsPage.getSiteId();

		// 得到站点的信息
		CmsSite cmsSite = this.findCmsSiteById(siteId);

		// 得到站点的物理路径
		String sitePhysicalPath = cmsSite.getSitePhysicalPath();

		// 得到页面的物理路径
		String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
		System.out.println(pagePath);

		// 将html文件保存到服务器的物理路径上
		FileOutputStream fs = null;
		try {
			fs = new FileOutputStream(new File(pagePath));
			IOUtils.copy(inputStream, fs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 根据页面ID查询页面
	public CmsPage findCmsPageById(String pageId) {
		Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 根据站点ID查询站点
	public CmsSite findCmsSiteById(String siteID) {
		Optional<CmsSite> optional = cmsSiteRepository.findById(siteID);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	// 根据文件Id从GirdFS中查询文件的内容
	public InputStream getFileById(String fileId) {
		// 获取文件对象
		GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
		// 打开下载流
		GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
		// 定义 GridFsResource
		GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
		try {
			return gridFsResource.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
