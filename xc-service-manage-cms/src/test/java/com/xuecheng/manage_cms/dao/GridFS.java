package com.xuecheng.manage_cms.dao;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/27 - 18:20
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFS {
	@Autowired
	GridFsTemplate gridFsTemplate;

	@Autowired
	GridFSBucket gridFSBucket;

	/**
	 * @功能: 存文件
	 * @作者: 高志红
	 */
	@Test
	public void test() throws FileNotFoundException {
		FileInputStream fs = new FileInputStream(new File("z:/course.ftl"));
		ObjectId id = gridFsTemplate.store(fs, "course.ftl");
		System.out.println(id);
	}

	@Test
	public void test2() throws IOException {
		// 根据文件ID查询文件
		GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5e5799dd2a371a2e947c25e1")));
		// 打开一个下载流
		GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
		// 创建GridFsResource对象，获取流
		GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);
		// 从流中取数据
		String string = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
		System.out.println(string);
	}
}
