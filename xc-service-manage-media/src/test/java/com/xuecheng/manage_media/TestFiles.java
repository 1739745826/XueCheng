package com.xuecheng.manage_media;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/9 - 12:58
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFiles {
	// 测试文件分块
	@Test
	public void test() throws IOException {
		// 源文件
		File sourceFile = new File("Z:\\XueCheng\\xcEduUI\\video\\cba.mp4");
		// 块文件目录
		String chunkFileFolder = "Z:\\XueCheng\\xcEduUI\\video\\chunks\\";
		// 先定义块文件的大小
		long chunkFileSize = 1 * 1024 * 1024;
		// 块数
		long chunkFileNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);
		// 创建一个读文件的对象
		RandomAccessFile raf_reader = new RandomAccessFile(sourceFile, "r");
		// 缓冲区
		byte[] b = new byte[1024];
		for (int i = 0; i < chunkFileNum; i++) {
			File chunkFile = new File(chunkFileFolder + i);
			int len = -1;
			// 创建向块文件的写对象
			RandomAccessFile raf_write = new RandomAccessFile(chunkFile, "rw");
			while ((len = raf_reader.read(b)) != -1) {
				raf_write.write(b, 0, len);
				// 如果文件的大小达到1M就开始写下一块
				if (chunkFile.length() >= chunkFileSize) {
					break;
				}
			}
			raf_write.close();
		}
		raf_reader.close();
	}

	// 测试文件合并
	@Test
	public void testMergeFile() throws IOException {
		// 块文件的目录
		String chunkFileFolderPath = "Z:\\XueCheng\\xcEduUI\\video\\chunks\\";
		// 块文件目录对象
		File chunkfileFolder = new File(chunkFileFolderPath);
		// 块文件列表
		File[] files = chunkfileFolder.listFiles();
		// 将块文件排序，按名称升序
		List<File> fileList = (List<File>) Arrays.asList(files);
		Collections.sort(fileList, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
					return 1;
				}
				return -1;
			}
		});
		// 合并文件
		File mergeFile = new File("Z:\\XueCheng\\xcEduUI\\video\\cba2.mp4");
		boolean newFile = mergeFile.createNewFile();
		// 创建写对象
		RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");
		// 缓存区
		byte[] b = new byte[1024];
		for (File file : fileList) {
			// 创建一个读块文件的对象
			RandomAccessFile raf_reader = new RandomAccessFile(file, "r");
			int len = -1;
			while ((len = raf_reader.read(b)) != -1) {
				raf_write.write(b, 0, len);
			}
			raf_reader.close();
		}
		raf_write.close();
	}
}
