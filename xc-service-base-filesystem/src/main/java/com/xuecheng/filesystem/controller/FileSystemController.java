package com.xuecheng.filesystem.controller;

import com.xuecheng.api.config.filesystem.FileSystemControllerApi;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/3 - 9:35
 */
@RestController
@RequestMapping("/filesystem")
public class FileSystemController implements FileSystemControllerApi {
	@Autowired
	FileSystemService fileSystemService;

	// 上传文件
	@Override
	@PostMapping("/upload")
	public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
		return fileSystemService.upload(multipartFile, filetag, businesskey, metadata);
	}
}
