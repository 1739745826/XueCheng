package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/3 - 9:06
 */
@Service
public class FileSystemService {
	@Value("${xuecheng.fastdfs.tracker_servers}")
	String tracker_servers;
	@Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
	int network_timeout_in_seconds;
	@Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
	int connect_timeout_in_seconds;
	@Value("${xuecheng.fastdfs.charset}")
	String charset;
	@Autowired
	FileSystemRepository fileSystemRepository;

	// 上传文件
	public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata) {
		// 1. 将文件上传到fastDFS中，得到一个文件ID
		String fileId = fdfs_upload(multipartFile);
		if (StringUtils.isEmpty(fileId)) {
			ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_BUSINESSISNULL);
		}

		// 2. 将文件ID及其他文件信息存储到mongodb中
		FileSystem fileSystem = new FileSystem();
		fileSystem.setFileId(fileId);
		fileSystem.setFilePath(fileId);
		fileSystem.setFiletag(filetag);
		fileSystem.setBusinesskey(businesskey);
		fileSystem.setFileName(multipartFile.getOriginalFilename());
		fileSystem.setFileType(multipartFile.getContentType());
		if (StringUtils.isEmpty(metadata)) {
			try {
				Map map = JSON.parseObject(metadata, Map.class);
				fileSystem.setMetadata(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		fileSystemRepository.save(fileSystem);
		return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
	}

	// 上传文件到fastDFS
	private String fdfs_upload(MultipartFile multipartFile) {
		if (multipartFile == null) {
			ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
		}
		// 初始化fastDFS环境
		this.initFastDFS();
		// 创建trackerClient
		TrackerClient trackerClient = new TrackerClient();
		try {
			TrackerServer trackerServer = trackerClient.getConnection();
			// 得到storage服务器
			StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
			// 创建storageClient来上传文件
			StorageClient1 storageClient = new StorageClient1(trackerServer, storageServer);
			// 上传文件
			// 得到文件的字节
			byte[] multipartFileBytes = multipartFile.getBytes();
			// 得到文件的原始名称
			String originalFilename = multipartFile.getOriginalFilename();
			// 得到文件的扩展名
			String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
			String fileId = storageClient.upload_file1(multipartFileBytes, ext, null);
			return fileId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 初始化fastDFS环境
	private void initFastDFS() {
		try {
			ClientGlobal.initByTrackers(tracker_servers);
			ClientGlobal.setG_charset(charset);
			ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
			ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionCast.cast(FileSystemCode.FS_INIT_ERROR);
		}
	}
}