package com.xuecheng.api.config.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/3 - 8:55
 */
@Api(value = "上传文件接口", description = "上传文件")
public interface FileSystemControllerApi {
	// 上传文件
	@ApiOperation("上传文件")
	public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata);
}
