package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class MediaProcessTask {
	@Autowired
	MediaFileRepository mediaFileRepository;
	@Value("${xc-service-manage-media.ffmpeg-path}")
	String ffmpegPath;
	@Value("${xc-service-manage-media.video-location}")
	String serverPath;


	// 接收视频处理的消息进行视频处理
	@RabbitListener(queues = "${xc-service-manage-media.mq.queue-media-video-processor}", containerFactory = "customContainerFactory")
	public void receiveMediaProcessTask(String msg) {
		// 1. 解析消息内容，得到mediaId
		Map map = JSON.parseObject(msg, Map.class);
		String mediaId = (String) map.get("mediaId");
		// 2. 拿着mediaId从数据库查询文件按信息
		Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
		if (!mediaFileOptional.isPresent()) {
			return;
		}
		MediaFile mediaFile = mediaFileOptional.get();
		// 拿到文件类型
		String fileType = mediaFile.getFileType();
		if (!fileType.equals("avi")) {
			// 更新处理状态为无需处理
			mediaFile.setProcessStatus("303004");
			mediaFileRepository.save(mediaFile);
			return;
		} else {
			// 更新处理状态为处理中
			mediaFile.setProcessStatus("303001");
			mediaFileRepository.save(mediaFile);
		}
		// 3. 使用工具类将avi文件生成mp4
		// 要处理的视频文件的路径
		String video_path = serverPath + mediaFile.getFilePath() + mediaFile.getFileName();
		// 生成的MP4文件名称
		String mp4_name = mediaFile.getFileId() + ".mp4";
		// 生成的MP4所在路径
		String mp4folder_path = serverPath + mediaFile.getFilePath();
		// 创建工具类对象
		Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpegPath, video_path, mp4_name, mp4folder_path);
		String result = mp4VideoUtil.generateMp4();
		if (result == null || !result.equals("success")) {
			// 处理失败
			mediaFile.setProcessStatus("303003");
			// 记录失败信息
			MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
			mediaFileProcess_m3u8.setErrormsg(result);
			mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
			mediaFileRepository.save(mediaFile);
			return;
		}
		// 4. 将MP4生成m3u8和ts文件
		// mp4视频文件路径
		String mp4_video_path = serverPath + mediaFile.getFilePath() + mp4_name;
		// m3u8_name文件名称
		String m3u8_name = mediaFile.getFileId() + ".m3u8";
		// m3u8文件所在目录
		String m3u8folder_path = serverPath + mediaFile.getFilePath() + "hls/";
		HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpegPath, mp4_video_path, m3u8_name, m3u8folder_path);
		String tsResult = hlsVideoUtil.generateM3u8();
		if (tsResult == null || !tsResult.equals("success")) {
			// 处理失败
			mediaFile.setProcessStatus("303003");
			// 记录失败信息
			MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
			mediaFileProcess_m3u8.setErrormsg(tsResult);
			mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
			mediaFileRepository.save(mediaFile);
			return;
		}
		// 处理成功
		// 获取ts文件列表
		List<String> ts_list = hlsVideoUtil.get_ts_list();
		MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
		mediaFileProcess_m3u8.setTslist(ts_list);
		mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
		mediaFile.setProcessStatus("303002");
		// 保存fileUrl(此URL就是视频播放的相对路径)
		String fileUrl = mediaFile.getFilePath() + "hls/" + m3u8_name;
		mediaFile.setFileUrl(fileUrl);
		mediaFileRepository.save(mediaFile);
	}
}
