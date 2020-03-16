package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/9 - 15:51
 */
@Service
public class MediaUploadService {
	@Autowired
	MediaFileRepository mediaFileRepository;
	@Value("${xc-service-manage-media.upload-location}")
	String upload_location;
	@Value("${xc-service-manage-media.mq.routingkey-media-video}")
	String routingkey;
	@Autowired
	RabbitTemplate rabbitTemplate;


	// 文件上传前的注册，检查文件是否存在

	/**
	 * 根据文件md5得到文件路径
	 * 规则：
	 * 一级目录：md5的第一个字符
	 * 二级目录：md5的第二个字符
	 * 三级目录：md5
	 * 文件名：md5+文件扩展名
	 */
	public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
		// 1. 检查文件在磁盘上是否存在
		// 文件所属目录的路径
		String fileFolderPath = this.getFileFolderPath(fileMd5);
		// 文件的路径
		String filePath = this.getFilePath(fileMd5, fileExt);
		File file = new File(filePath);
		boolean exists = file.exists();

		// 2. 检测文件信息在mongodb中是否存在
		Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(fileMd5);
		if (exists && mediaFileOptional.isPresent()) {
			// 文件存在
			ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
		}
		// 文件不存在则作一些准备工作
		File fileFolder = new File(fileFolderPath);
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 得到文件所属目录的路径
	private String getFileFolderPath(String fileMd5) {
		return upload_location + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/";
	}

	// 得到文件的路径
	private String getFilePath(String fileMd5, String fileExt) {
		return upload_location + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + fileMd5 + "." + fileExt;
	}

	// 得到块文件的所属目录
	private String getChunkFilePath(String fileMd5) {
		return upload_location + fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk/";
	}

	// 检验分块是否存在
	public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
		// 检查分块文件是否存在
		// 得到分块文件的所在目录
		String chunkFilePath = this.getChunkFilePath(fileMd5);
		File chunkFile = new File(chunkFilePath + chunk);
		if (chunkFile.exists()) {
			// 块文件存在
			return new CheckChunkResult(CommonCode.SUCCESS, true);
		} else {
			return new CheckChunkResult(CommonCode.SUCCESS, false);
		}
	}

	// 上传分块
	public ResponseResult uploadchunk(MultipartFile file, String fileMd5, Integer chunk) {
		// 检测分块目录，如果不存在则要自动创建
		// 得到分块目录
		String chunkFilePath = this.getChunkFilePath(fileMd5);
		// 得到分块文件路径
		File chunkFileFolder = new File(chunkFilePath);
		if (!chunkFileFolder.exists()) {
			chunkFileFolder.mkdirs();
		}
		// 得到上传文件的输入流
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		try {
			inputStream = file.getInputStream();
			outputStream = new FileOutputStream(new File(chunkFilePath + chunk));
			IOUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 合并分块
	public ResponseResult mergechunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
		// 合并所有的分块
		// 得到分块文件的目录
		String chunkFilePath = this.getChunkFilePath(fileMd5);
		File chunkFile = new File(chunkFilePath);
		// 分块文件列表
		File[] files = chunkFile.listFiles();
		List<File> fileList = Arrays.asList(files);
		// 创建一个合并文件
		String filePath = this.getFilePath(fileMd5, fileExt);
		File mergeFile = new File(filePath);
		// 合并分块
		mergeFile = this.mergeFile(fileList, mergeFile);
		if (mergeFile == null) {
			// 合并文件失败
			ExceptionCast.cast(MediaCode.MERGE_FILE_FAIL);
		}
		// 校验文件的MD5值和前端传入的MD5一致
		boolean checkFukeMd5 = this.checkFileMd5(mergeFile, fileMd5);
		if (!checkFukeMd5) {
			// 校验文件失败
			ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);
		}
		// 将文件的信息写入mongodb
		MediaFile mediaFile = new MediaFile();
		mediaFile.setFileId(fileMd5);
		mediaFile.setFileOriginalName(fileName);
		mediaFile.setFileName(fileMd5 + "." + fileExt);
		// 文件路径保存相对路径
		mediaFile.setFilePath(fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/");
		// 文件的大小
		mediaFile.setFileSize(fileSize);
		mediaFile.setUploadTime(new Date());
		mediaFile.setMimeType(mimetype);
		mediaFile.setFileType(fileExt);
		// 上传状态
		mediaFile.setFileStatus("301002");
		// 保存
		mediaFileRepository.save(mediaFile);
		// 向MQ发送视频处理消息
		sendProcesVideoMsg(mediaFile.getFileId());
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 合并文件
	private File mergeFile(List<File> chunkFIleList, File mergeFile) {
		try {
			// 如果合并的文件已存在则删除，否则创建新文件
			if (mergeFile.exists()) {
				mergeFile.delete();
			} else {
				mergeFile.createNewFile();
			}
			// 对块文件进行排序
			Collections.sort(chunkFIleList, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
						return 1;
					} else {
						return -1;
					}
				}
			});
			// 创建一个写对象
			RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
			byte[] b = new byte[1024];
			for (File chunkFile : chunkFIleList) {
				RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
				int len = -1;
				while ((len = raf_read.read(b)) != -1) {
					raf_write.write(b, 0, len);
				}
				raf_read.close();
			}
			raf_write.close();
			return mergeFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 检验文件
	private boolean checkFileMd5(File mergeFile, String md5) {
		// 创建文件输入流
		try {
			FileInputStream inputStream = new FileInputStream(mergeFile);
			// 得到文件的MD5
			String md5hex = DigestUtils.md5Hex(inputStream);
			// 和传入的md5比较
			if (md5.equalsIgnoreCase(md5hex)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// 发送视频处理消息
	public ResponseResult sendProcesVideoMsg(String mediaId) {
		// 查询数据库mediaFile
		Optional<MediaFile> optionalMediaFile = mediaFileRepository.findById(mediaId);
		if (!optionalMediaFile.isPresent()) {
			ExceptionCast.cast(CommonCode.FAIL);
		}
		// 构造消息内容
		Map<String, String> map = new HashMap<>();
		map.put("mediaId", mediaId);
		String jsonString = JSON.toJSONString(map);
		// 向MQ发送视频处理消息
		try {
			rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK, routingkey, jsonString);
			return new ResponseResult(CommonCode.SUCCESS);
		} catch (AmqpException e) {
			e.printStackTrace();
			return new ResponseResult(CommonCode.FAIL);
		}
	}
}
