package com.xuecheng.manage_media_process;

import com.xuecheng.framework.utils.Mp4VideoUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-07-12 9:11
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestProcessBuilder {
	// 使用processBuilder来调用第三方应用程序
	@Test
	public void testProcessBuilder() throws IOException {
		// 创建一个processBuilder对象
		ProcessBuilder processBuilder = new ProcessBuilder();
		// 设置第三方应用的命令
		processBuilder.command("ping", "127.0.0.1");
		// 将标准输入流和错误流合并
		processBuilder.redirectErrorStream(true);
		// 启动一个进程
		Process process = processBuilder.start();
		// 通过标准是输入流拿到正常和错误的信息
		InputStream inputStream = process.getInputStream();
		// 转成字符流
		InputStreamReader reader = new InputStreamReader(inputStream);
		// 缓冲
		char[] chars = new char[1024];
		int len = -1;
		while ((len = reader.read(chars)) != -1) {
			String string = new String(chars, 0, len);
			System.out.println(string);
		}
		inputStream.close();
		reader.close();
	}


	// 使用processBuilder来调用第三方应用程序
	@Test
	public void testFFmpeg() throws IOException {
		// 创建一个processBuilder对象
		ProcessBuilder processBuilder = new ProcessBuilder();
		// 设置第三方应用的命令
		List<String> command = new ArrayList<>();
		command.add("ffmpeg.exe");
		command.add("-i");
		command.add("C:\\Users\\hcsgc\\OneDrive\\图片\\璐\\lu.avi");
		command.add("‐y");//覆盖输出文件
		command.add("‐c:v");
		command.add("libx264");
		command.add("‐s");
		command.add("1280x720");
		command.add("‐pix_fmt");
		command.add("yuv420p");
		command.add("‐b:a");
		command.add("63k");
		command.add("‐b:v");
		command.add("753k");
		command.add("‐r");
		command.add("18");
		command.add("C:\\Users\\hcsgc\\OneDrive\\图片\\璐\\lu.mp4");
		processBuilder.command(command);
		// 将标准输入流和错误流合并
		processBuilder.redirectErrorStream(true);
		// 启动一个进程
		Process process = processBuilder.start();
		// 通过标准是输入流拿到正常和错误的信息
		InputStream inputStream = process.getInputStream();
		// 转成字符流
		InputStreamReader reader = new InputStreamReader(inputStream);
		// 缓冲
		char[] chars = new char[1024];
		int len = -1;
		while ((len = reader.read(chars)) != -1) {
			String string = new String(chars, 0, len);
			System.out.println(string);
		}
		inputStream.close();
		reader.close();
	}

	@Test
	public void testMp4VideoUtil() {
		String ffmpeg_path = "ffmpeg.exe";
		String video_patg = "C:\\Users\\hcsgc\\OneDrive\\图片\\璐\\lu.avi";
		String mp4_name = "lu.mp4";
		String mp4folder_path = "C:\\Users\\hcsgc\\OneDrive\\图片\\璐\\";
		Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path, video_patg, mp4_name, mp4folder_path);
		String result = mp4VideoUtil.generateMp4();
		System.out.println(result);
	}



    /*
    @Test
    public void testProcessBuilder() throws IOException {
     //创建ProcessBuilder对象
        ProcessBuilder processBuilder =new ProcessBuilder();
        //设置执行的第三方程序(命令)
//        processBuilder.command("ping","127.0.0.1");
        processBuilder.command("ipconfig");
//        processBuilder.command("java","-jar","f:/xc-service-manage-course.jar");
        //将标准输入流和错误输入流合并，通过标准输入流读取信息就可以拿到第三方程序输出的错误信息、正常信息
        processBuilder.redirectErrorStream(true);

        //启动一个进程
        Process process = processBuilder.start();
        //由于前边将错误和正常信息合并在输入流，只读取输入流
        InputStream inputStream = process.getInputStream();
        //将字节流转成字符流
        InputStreamReader reader = new InputStreamReader(inputStream,"gbk");
       //字符缓冲区
        char[] chars = new char[1024];
        int len = -1;
        while((len = reader.read(chars))!=-1){
            String string = new String(chars,0,len);
            System.out.println(string);
        }

        inputStream.close();
        reader.close();

    }

    //测试使用工具类将avi转成mp4
    @Test
    public void testProcessMp4(){
        //String ffmpeg_path, String video_path, String mp4_name, String mp4folder_path
        //ffmpeg的路径
        String ffmpeg_path = "D:\\Program Files\\ffmpeg-20180227-fa0c9d6-win64-static\\bin\\ffmpeg.exe";
        //video_path视频地址
        String video_path = "E:\\ffmpeg_test\\1.avi";
        //mp4_name mp4文件名称
        String mp4_name  ="1.mp4";
        //mp4folder_path mp4文件目录路径
        String mp4folder_path="E:/ffmpeg_test/";
        Mp4VideoUtil mp4VideoUtil = new Mp4VideoUtil(ffmpeg_path,video_path,mp4_name,mp4folder_path);
        //开始编码,如果成功返回success，否则返回输出的日志
        String result = mp4VideoUtil.generateMp4();
        System.out.println(result);
    }*/
}
