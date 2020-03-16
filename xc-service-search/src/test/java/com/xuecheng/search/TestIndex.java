package com.xuecheng.search;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/6 - 19:08
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestIndex {
	@Autowired
	RestHighLevelClient highLevelClient;
	@Autowired
	RestClient restClient;

	/**
	 * @功能: 删除索引库
	 * @作者: 高志红
	 */
	@Test
	public void deleteIndex() throws IOException {
		// 创建删除索引对象
		DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xc_course");
		// 操作索引的客户端
		IndicesClient indices = highLevelClient.indices();
		// 执行删除索引
		DeleteIndexResponse delete = indices.delete(deleteIndexRequest);
		// 得到响应
		boolean falg = delete.isAcknowledged();
		System.out.println(falg);
	}

	/**
	 * @功能: 创建索引库并创建映射
	 * @作者: 高志红
	 */
	@Test
	public void createIndex() throws IOException {
		// 创建索引对象
		CreateIndexRequest createIndexRequest = new CreateIndexRequest("xc_course");
		// 设置参数
		createIndexRequest.settings(Settings.builder().put("number_of_shards", "1").put("number_of_replicas", "0"));
		// 指定映射
		createIndexRequest.mapping("doc", "{\n" +
				"    \"properties\": {\n" +
				"        \"name\": {\n" +
				"            \"type\": \"text\",\n" +
				"            \"analyzer\": \"ik_max_word\",\n" +
				"            \"search_analyzer\": \"ik_smart\"\n" +
				"        },\n" +
				"        \"description\": {\n" +
				"            \"type\": \"text\",\n" +
				"            \"analyzer\": \"ik_max_word\",\n" +
				"            \"search_analyzer\": \"ik_smart\"\n" +
				"        },\n" +
				"        \"pic\": {\n" +
				"            \"type\": \"text\",\n" +
				"            \"index\": false\n" +
				"        },\n" +
				"        \"studymodel\": {\n" +
				"            \"type\": \"text\"\n" +
				"        }\n" +
				"    }\n" +
				"}", XContentType.JSON);

		// 操作索引的客户端
		IndicesClient indices = highLevelClient.indices();
		// 执行创建按索引
		CreateIndexResponse create = indices.create(createIndexRequest);
		// 得到响应
		boolean falg = create.isAcknowledged();
		System.out.println(falg);
	}

	/**
	 * @功能: 添加文档
	 * @作者: 高志红
	 */
	@Test
	public void addDoc() throws IOException {
		// 文档内容
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("name", "spring cloud实战");
		jsonMap.put("description", "本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。");
		jsonMap.put("studymodel", "201001");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy‐MM‐dd HH:mm:ss");
		jsonMap.put("timestamp", dateFormat.format(new Date()));
		jsonMap.put("price", 5.6f);

		// 创建索引创建对象
		IndexRequest indexRequest = new IndexRequest("xc_course", "doc");
		// 文档内容
		indexRequest.source(jsonMap);
		// 通过client进行http请求
		IndexResponse indexResponse = highLevelClient.index(indexRequest);
		DocWriteResponse.Result result = indexResponse.getResult();
		System.out.println(result);
	}

	/**
	 * @功能: 查询文档内容
	 * @作者: 高志红
	*/
	@Test
	public void deleteDoc() throws IOException {
		// 查询请求对象
		GetRequest getRequest = new GetRequest("xc_course", "doc", "n5yjr3ABbJ8CA20PjL09");
		GetResponse getResponse = highLevelClient.get(getRequest);
		// 得到文档的内容
		Map<String, Object> sourceAsMap = getResponse.getSourceAsMap();
		System.out.println(sourceAsMap);
	}
	
	@Test
	public void test (){
		for (int i = 1; i <=5 ; i++) {
			for (int k = 1; k <=3 ; k++) {
				System.out.print("*");
			}
			System.out.println();
		}
	}
}
