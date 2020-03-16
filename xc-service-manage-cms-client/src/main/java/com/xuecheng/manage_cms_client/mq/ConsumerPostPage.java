package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 标题：监听MQ接收页面发布的消息
 * 作者：何处是归程
 * 时间：2020/2/29 - 10:36
 */
@Component
public class ConsumerPostPage {
	@Autowired
	PageService pageService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

	@RabbitListener(queues = {"${xuecheng.mq.queue}"})
	public void postPage(String msg) {
		// 解析消息
		Map map = JSON.parseObject(msg, Map.class);
		// 得到消息中的页面ID
		String pageId = (String) map.get("pageId");
		// 校验页面是否合法
		CmsPage cmsPage = pageService.findCmsPageById(pageId);
		if (cmsPage == null) {
			LOGGER.error("receive postpage asg, cmsPage is null, pageId:{}", pageId);
		}
		// 调用service方法将页面从GridFs中下载到服务器
		pageService.savePageToServerPath(pageId);
	}
}
