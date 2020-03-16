package com.xuecheng.manage_cms_client.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/27 - 18:46
 */
@Configuration
public class MongConfig {
	@Value("${spring.data.mongodb.database}")
	String db;

	@Bean
	public GridFSBucket getGridFSBucket(MongoClient mongoClient) {
		MongoDatabase database = mongoClient.getDatabase(db);
		GridFSBucket bucket = GridFSBuckets.create(database);
		return bucket;
	}
}
