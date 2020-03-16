package com.xuecheng.api.config.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/9 - 15:19
 */
@Api(value = "媒资管理接口", description = "媒资管理接口，提供文件上传处理等接口")
public interface MediaUploadControllerApi {
	// 文件上传前的准备工作 校验文件是否存在
	@ApiOperation("文件上传注册")
	public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);

	// 检验分块是否存在
	@ApiOperation("校验分块文件是否存在")
	public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize);

	// 上传分块
	@ApiOperation("上传分块")
	public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk);

	// 合并分块
	@ApiOperation("合并分块")
	public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt);
}
