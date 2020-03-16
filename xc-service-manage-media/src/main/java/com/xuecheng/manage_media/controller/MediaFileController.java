package com.xuecheng.manage_media.controller;

import com.xuecheng.api.config.media.MediaFileControllerApi;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.manage_media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/10 - 16:04
 */
@RestController
@RequestMapping("/media/file")
public class MediaFileController implements MediaFileControllerApi {
	@Autowired
	MediaFileService mediaFileService;

	// 我的媒资文件查询列表
	@Override
	@GetMapping("/list/{page}/{size}")
	public QueryResponseResult<MediaFile> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryMediaFileRequest queryMediaFileRequest) {
		return mediaFileService.findList(page, size, queryMediaFileRequest);
	}
}
