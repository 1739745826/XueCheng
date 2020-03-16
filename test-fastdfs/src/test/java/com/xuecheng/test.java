package com.xuecheng;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/2 - 17:04
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class test {

	// 上传文件
	@Test
	public void testUpload() {
		try {
			// 加载fastdfs-client.properties
			ClientGlobal.initByProperties("fastdfs-client.properties");
			// 定义一个TrackerClient，用于请求TrackerServicer
			TrackerClient trackerClient = new TrackerClient();
			// 连接tracker
			TrackerServer trackerServer = trackerClient.getConnection();
			// 获取Stroage
			StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
			// 创建stroageClient
			StorageClient1 storageClient = new StorageClient1(trackerServer, storeStorage);
			// 向Stroage上传文件
			// 本地文件的路径
			String filePath = "C:\\Users\\hcsgc\\OneDrive\\图片\\科比\\001.jpg";
			String fileId = storageClient.upload_file1(filePath, "jpg", null);
			System.out.println(fileId); // group1/M00/00/00/wKgAaF5c0v-ALlHTAAIGSqPJCC4378.jpg
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//下载文件
	@Test
	public void testDownload() {
		try {
			// 加载fastdfs-client.properties
			ClientGlobal.initByProperties("fastdfs-client.properties");
			// 定义一个TrackerClient，用于请求TrackerServicer
			TrackerClient trackerClient = new TrackerClient();
			// 连接tracker
			TrackerServer trackerServer = trackerClient.getConnection();
			// 获取Stroage
			StorageServer storeStorage = trackerClient.getStoreStorage(trackerServer);
			// 创建stroageClient
			StorageClient1 storageClient = new StorageClient1(trackerServer, storeStorage);
			// 下载文件
			String fileid = "group1/M00/00/00/wKgAaF5c0v-ALlHTAAIGSqPJCC4378.jpg";
			byte[] bytes = storageClient.download_file1(fileid);
			// 使用输出流保存文件
			FileOutputStream fileOutputStream = new FileOutputStream(new File("Z:/logo.jpg"));
			fileOutputStream.write(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
