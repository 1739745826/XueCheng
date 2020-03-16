package com.xuecheng.manage_media.controller;

import com.xuecheng.api.config.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import jdk.management.resource.ResourceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/9 - 15:53
 */
@RestController
@RequestMapping("/media/upload")
public class MediaUploadController implements MediaUploadControllerApi {
	@Autowired
	MediaUploadService mediaUploadService;

	// 文件上传前的注册
	@Override
	@PostMapping("/register")
	public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
		return mediaUploadService.register(fileMd5, fileName, fileSize, mimetype, fileExt);
	}

	// 检验分块是否存在
	@Override
	@PostMapping("/checkchunk")
	public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
		return mediaUploadService.checkChunk(fileMd5, chunk, chunkSize);
	}

	// 上传分块
	@Override
	@PostMapping("/uploadchunk")
	public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
		return mediaUploadService.uploadchunk(file, fileMd5, chunk);
	}

	// 合并分块
	@Override
	@PostMapping("/mergechunks")
	public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
		return mediaUploadService.mergechunks(fileMd5, fileName, fileSize, mimetype, fileExt);
	}
}
