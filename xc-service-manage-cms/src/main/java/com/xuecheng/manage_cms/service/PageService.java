package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRespository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/25 - 16:06
 */
@Service
public class PageService {
	@Autowired
	CmsPageRepository cmsPageRepository;
	@Autowired
	CmsConfigRespository cmsConfigRespository;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	CmsTemplateRepository cmsTemplateRepository;
	@Autowired
	GridFsTemplate gridFsTemplate;
	@Autowired
	GridFSBucket gridFSBucket;
	@Autowired
	RabbitTemplate rabbitTemplate;
	@Autowired
	CmsSiteRepository cmsSiteRepository;


	/**
	 * @功能: 页面查询
	 * @作者: 高志红
	 */
	public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
		// 防止空指针异常
		if (queryPageRequest == null) {
			queryPageRequest = new QueryPageRequest();
		}

		// 自定义条件查询
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
		// 条件值对象
		CmsPage cmsPage = new CmsPage();
		// 站点ID为查询条件
		if (!StringUtils.isEmpty(queryPageRequest.getSiteId())) {
			cmsPage.setSiteId(queryPageRequest.getSiteId());
		}
		// 模板ID为查询条件
		if (!StringUtils.isEmpty(queryPageRequest.getTemplateId())) {
			cmsPage.setTemplateId(queryPageRequest.getTemplateId());
		}
		// 页面别名为查询条件
		if (!StringUtils.isEmpty(queryPageRequest.getPageAliase())) {
			cmsPage.setPageAliase(queryPageRequest.getPageAliase());
		}
		// 定义条件对象
		Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);

		// 分页参数
		if (page <= 0) {
			// 页码从0开始 符合用户的习惯
			page = 1;
		}
		// 页码从0开始符合DAO接口
		page = page - 1;
		if (size <= 0) {
			size = 10;
		}
		Pageable pageable = PageRequest.of(page, size);
		// 实现自定义条件分页查询
		Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
		QueryResult queryResult = new QueryResult();
		queryResult.setList(all.getContent()); // 数据列表
		queryResult.setTotal(all.getTotalElements()); // 数据总记录数
		return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
	}

	/**
	 * @功能: 新增页面
	 * @作者: 高志红
	 */
	public CmsPageResult add(CmsPage cmsPage) {
		if (cmsPage == null) {
			// 抛出非法参数异常
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
		}

		// 校验页面名称， 站点id, 页面webpath的唯一性
		CmsPage cmsPageByPageNameAndSiteIdAndPageWebPath = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());

		if (cmsPageByPageNameAndSiteIdAndPageWebPath != null) {
			// 抛出页面名称已存在异常
			ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
		}

		// 调用DAO新增页面
		cmsPage.setPageId(null);
		cmsPageRepository.save(cmsPage);
		return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
	}

	/**
	 * @功能: 根据页面的ID查询页面
	 * @作者: 高志红
	 */
	public CmsPage findById(String id) {
		Optional<CmsPage> optional = cmsPageRepository.findById(id);
		if (optional.isPresent()) {
			CmsPage cmsPage = optional.get();
			return cmsPage;
		} else {
			return null;
		}
	}

	/**
	 * @功能: 修改页面
	 * @作者: 高志红
	 */
	public CmsPageResult edit(String id, CmsPage cmsPage) {
		// 根据ID从数据库查询页面信息
		CmsPage cmsPage1 = this.findById(id);
		if (cmsPage1 == null) {
			// 抛出页面为空异常
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
		}
		// 准备更新数据
		// 设置要修改的数据
		cmsPage1.setTemplateId(cmsPage.getTemplateId());
		//更新所属站点
		cmsPage1.setSiteId(cmsPage.getSiteId());
		//更新页面别名
		cmsPage1.setPageAliase(cmsPage.getPageAliase());
		//更新页面名称
		cmsPage1.setPageName(cmsPage.getPageName());
		//更新访问路径
		cmsPage1.setPageWebPath(cmsPage.getPageWebPath());
		//更新物理路径
		cmsPage1.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
		// 更新DataUrl
		cmsPage1.setDataUrl(cmsPage.getDataUrl());
		// 提交修改
		cmsPageRepository.save(cmsPage1);
		return new CmsPageResult(CommonCode.SUCCESS, cmsPage1);
	}

	/**
	 * @功能: 根据ID删除页面
	 * @作者: 高志红
	 */
	public ResponseResult delete(String id) {
		// 先查询一下
		Optional<CmsPage> optional = cmsPageRepository.findById(id);
		if (!optional.isPresent()) {
			// 抛出页面为空异常
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
		}
		cmsPageRepository.deleteById(id);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	/**
	 * @功能: 根据ID查询cmsConfig
	 * @作者: 高志红
	 */
	public CmsConfig getConfigById(String id) {
		Optional<CmsConfig> optional = cmsConfigRespository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}
	}

	// 页面静态化方法
	// 1、填写页面DataUrl 在编辑cms页面信息界面填写DataUrl，将此字段保存到cms_page集合中。
	// 2、静态化程序获取页面的DataUrl
	// 3、静态化程序远程请求DataUrl获取数据模型。
	// 4、静态化程序获取页面的模板信息
	// 5、执行页面静态化
	public String getPageHtml(String pageId) {
		// 获取数据模型
		Map model = getModelByPageId(pageId);
		if (model == null) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
		}
		// 获取页面模板
		String template = getTemplateByPageId(pageId);
		if (StringUtils.isEmpty(template)) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
		}
		// 执行静态化
		String html = generateHtml(template, model);
		return html;
	}

	/**
	 * @功能: 获取数据模型
	 * @作者: 高志红
	 */
	private Map getModelByPageId(String id) {
		// 取出页面的信息
		CmsPage cmsPage = this.findById(id);
		if (cmsPage == null) {
			ExceptionCast.cast(CmsCode.CMS_PATA_NOTEXISTS);
		}
		// 取出页面的dataUrl
		String dataUrl = cmsPage.getDataUrl();
		if (StringUtils.isEmpty(dataUrl)) {
			// 页面的dataUrl为空
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
		}
		// 通过RestTemplate请求dataUrl获取数据
		ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
		Map body = forEntity.getBody();
		Map<String, Object> map = new HashMap<>();
//		map.put("model", body);
		map.putAll(body);
		return map;
	}

	/**
	 * @功能: 获取页面模板
	 * @作者: 高志红
	 */
	public String getTemplateByPageId(String id) {
		System.out.println(gridFSBucket);
		// 取出页面的信息
		CmsPage cmsPage = this.findById(id);
		if (cmsPage == null) {
			ExceptionCast.cast(CmsCode.CMS_PATA_NOTEXISTS);
		}
		// 获取页面的模板ID
		String templateId = cmsPage.getTemplateId();
		if (StringUtils.isEmpty(templateId)) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
		}
		// 查询模板信息
		Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
		if (optional.isPresent()) {
			CmsTemplate cmsTemplate = optional.get();
			// 获取模板文件ID
			String templateFileId = cmsTemplate.getTemplateFileId();
			// 从FridFS中取模板文件内容
			// 根据文件ID查询文件
			GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
			// 打开一个下载流
			GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
			// 创建GridFsResource对象，获取流
			GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
			// 从流中取数据
			try {
				String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
				return content;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @功能: 执行静态化
	 * @作者: 高志红
	 */
	private String generateHtml(String templateContent, Map<String, Object> model) {
		// 定义配置类
		Configuration configuration = new Configuration(Configuration.getVersion());
		// 创建模板加载器
		StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
		stringTemplateLoader.putTemplate("template", templateContent);
		// 向configuration配置模板加载器
		configuration.setTemplateLoader(stringTemplateLoader);
		// 获取模板
		try {
			Template template = configuration.getTemplate("template");
			// 调用API进行静态化
			String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 页面发布
	public ResponseResult post(String pageId) {
		// 执行页面静态化
		String pageHtml = this.getPageHtml(pageId);

		// 将页面静态化文件存储到GridFs中
		CmsPage cmsPage = saveHtml(pageId, pageHtml);

		// 向MQ发送消息
		sendPostPage(pageId);

		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 保存html到GridFS
	private CmsPage saveHtml(String pageId, String htmlContent) {
		// 得到页面的信息
		CmsPage cmsPage = this.findById(pageId);
		if (cmsPage == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		// 将htmlContent内容转成输入流
		ObjectId objectId = null;
		try {
			InputStream inputStream = IOUtils.toInputStream(htmlContent, "utf-8");
			// 将html文件的内容保存到GridFs
			objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 将html文件ID更新到cmsPage中
		cmsPage.setHtmlFileId(objectId.toHexString());
		cmsPageRepository.save(cmsPage);
		return cmsPage;
	}

	// 向MQ发送消息
	private void sendPostPage(String pageId) {
		// 得到页面信息
		CmsPage cmsPage = this.findById(pageId);

		// 创建消息对象
		Map<String, String> map = new HashMap<>();
		map.put("pageId", pageId);

		// 转成JSON串
		String json = JSON.toJSONString(map);
		rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTPAGE, cmsPage.getSiteId(), json);
	}

	// 保存页面 - 有则更新 没有则添加
	public CmsPageResult save(CmsPage cmsPage) {
		// 判断页面是否存在
		CmsPage cmsPage1 = cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(cmsPage.getPageName(), cmsPage.getSiteId(), cmsPage.getPageWebPath());
		if (cmsPage1 != null) {
			// 进行更新
			return this.edit(cmsPage1.getPageId(), cmsPage);
		}
		return this.add(cmsPage);
	}

	// 一建发布
	public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
		// 将页面信息存储倒cms_page集合中
		CmsPageResult save = this.save(cmsPage);
		if (!save.isSuccess()) {
			ExceptionCast.cast(CommonCode.FAIL);
		}

		// 得到页面ID
		CmsPage cmsPageSave = save.getCmsPage();
		String pageId = save.getCmsPage().getPageId();

		// 执行页面发布（先静态化，保存GridFs, 向MQ发送消息）
		ResponseResult post = this.post(pageId);
		if (!post.isSuccess()) {
			ExceptionCast.cast(CmsCode.CMS_POST_FAIL);
		}

		// 拼接页面
		// 站点ID
		String siteId = cmsPageSave.getSiteId();
		CmsSite cmsSite = this.findCmsSiteById(siteId);
		if (cmsSite == null) {
			ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
		}
		String pageUrl = cmsSite.getSiteDomain() + cmsSite.getSiteWebPath() + cmsPage.getPageWebPath() + cmsPage.getPageName();
		return new CmsPostPageResult(CommonCode.SUCCESS, pageUrl);
	}

	// 根据站点ID查询站点信息
	public CmsSite findCmsSiteById(String siteId) {
		Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
}