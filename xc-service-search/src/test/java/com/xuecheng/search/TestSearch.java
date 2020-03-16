package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/7 - 10:24
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {
	@Autowired
	RestHighLevelClient highLevelClient;
	@Autowired
	RestClient restClient;

	/**
	 * @功能: 搜索全部记录
	 * @作者: 高志红
	 */
	@Test
	public void queryAll() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置搜索方式 matchAllQuery - 搜索全部
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 分页查询
	 * @作者: 高志红
	 */
	@Test
	public void queryAllPage() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置分页参数 from - 开始记录下标 size - 每页显示的记录数
		int page = 0;
		int size = 1;
		int from = page * size;
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);
		// 设置搜索方式 matchAllQuery - 搜索全部
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 基于项的查询（精确查询）
	 * @作者: 高志红
	 */
	@Test
	public void termQuery() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置分页参数 from - 开始记录下标 size - 每页显示的记录数
		int page = 0;
		int size = 1;
		int from = page * size;
		searchSourceBuilder.from(from);
		searchSourceBuilder.size(size);
		// 设置搜索方式
		searchSourceBuilder.query(QueryBuilders.termQuery("name", "spring"));
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 根据ID查询
	 * @作者: 高志红
	 */
	@Test
	public void termQueryById() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置搜索方式
		String[] ids = new String[]{"1", "2"};
		searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 全文匹配
	 * @作者: 高志红
	 */
	@Test
	public void MatchQuery() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置搜索方式
		searchSourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发框架").minimumShouldMatch("80%"));
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 搜索多个域并可以设置某个域的权重
	 * @作者: 高志红
	 */
	@Test
	public void MultiMatchQuery() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置搜索方式
		searchSourceBuilder.query(
				QueryBuilders.multiMatchQuery("spring css", "name", "description")
						.minimumShouldMatch("50%")
						.field("name", 10));
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 布尔查询
	 * @作者: 高志红
	 */
	@Test
	public void BooleanQuery() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置搜索方式
		// 先定义一个multiMatchQueryBuilder
		MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description")
				.minimumShouldMatch("50%")
				.field("name", 10);
		// 再定义一个termQuery
		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");
		// 定义一个boolQuery
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(multiMatchQueryBuilder);
		boolQueryBuilder.must(termQueryBuilder);
		searchSourceBuilder.query(boolQueryBuilder);
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 过滤器
	 * @作者: 高志红
	 */
	@Test
	public void fulterQuery() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 设置搜索方式
		// 先定义一个multiMatchQueryBuilder
		MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description")
				.minimumShouldMatch("50%")
				.field("name", 10);
		// 定义一个boolQuery
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(multiMatchQueryBuilder);
		// 定义两个过滤器
		boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
		boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
		searchSourceBuilder.query(boolQueryBuilder);
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 排序
	 * @作者: 高志红
	 */
	@Test
	public void SortQuery() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 定义一个boolQuery
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		// 定义过滤器
		boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
		searchSourceBuilder.query(boolQueryBuilder);
		// 添加排序
		searchSourceBuilder.sort("studymodel", SortOrder.DESC);
		searchSourceBuilder.sort("price", SortOrder.ASC);
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			System.out.println(map);
		}
	}

	/**
	 * @功能: 高亮
	 * @作者: 高志红
	 */
	@Test
	public void Highlight() throws IOException, ParseException {
		// 搜索请求对象
		SearchRequest searchRequest = new SearchRequest("xc_course");
		// 指定类型
		searchRequest.types("doc");
		// 搜索源构建对象
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		// 定义一个MultiMatchQueryBuilder
		MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发框架", "name", "description");
		// 定义一个boolQuery
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		boolQueryBuilder.must(multiMatchQueryBuilder);
		// 定义过滤器
		boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(0).lte(100));
		searchSourceBuilder.query(boolQueryBuilder);
		// 添加排序
		searchSourceBuilder.sort("studymodel", SortOrder.DESC);
		searchSourceBuilder.sort("price", SortOrder.ASC);
		// 设置源字段过滤 第一个参数设置包括哪些字段，第二个参数设置不包括那些字段
		searchSourceBuilder.fetchSource(new String[]{"name", "description", "studymodel", "price", "timestamp"}, new String[]{});
		// 设置高亮
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.preTags("<tag>");
		highlightBuilder.postTags("</tag>");
		highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
		highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
		searchSourceBuilder.highlighter(highlightBuilder);
		// 向搜索请求对象中设置搜索源
		searchRequest.source(searchSourceBuilder);
		// 执行搜索
		SearchResponse searchResponse = highLevelClient.search(searchRequest);
		// 拿到搜索的结果
		SearchHits hits = searchResponse.getHits();
		// 搜索到的总记录数
		long totalHits = hits.getTotalHits();
		// 得到匹配的高的文档
		SearchHit[] searchHits = hits.getHits();
		// 日期格式化对象
		for (SearchHit searchHit : searchHits) {
			// 文档的主键
			searchHit.getId();
			// 原文档的内容
			Map<String, Object> map = searchHit.getSourceAsMap();
			String name = map.get("name").toString();
			String description = map.get("description").toString();
			String studymodel = map.get("studymodel").toString();
			Double price = (Double) map.get("price");
			String timestamp = (String) map.get("timestamp");
			// 取出name高亮字段
			Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
			if (highlightFields != null) {
				HighlightField highlightName = highlightFields.get("name");
				HighlightField highlightDescription = highlightFields.get("description");
				if (highlightName != null) {
					Text[] texts = highlightName.getFragments();
					StringBuffer sb = new StringBuffer();
					for (Text text : texts) {
						sb.append(text);
					}
					name = sb.toString();
				}
				if (highlightDescription != null) {
					Text[] texts = highlightDescription.getFragments();
					StringBuffer sb = new StringBuffer();
					for (Text text : texts) {
						sb.append(text);
					}
					description = sb.toString();
				}
			}
			System.out.println(name);
			System.out.println(description);
			/*
				Bootstrap<tag>开发</tag>
				Bootstrap是由Twitter推出的一个前台页面<tag>开发</tag><tag>框架</tag>，是一个非常流行的<tag>开发</tag><tag>框架</tag>，此<tag>框架</tag>集成了多种页面效果。此<tag>开发</tag><tag>框架</tag>包含了大量的CSS、JS程序代码，可以帮助<tag>开发</tag>者（尤其是不擅长页面<tag>开发</tag>的程序人员）轻松的实现一个不受浏览器限制的精美界面效果。
				Java编程基础
				java语言是世界第一编程语言，在软件<tag>开发</tag>领域使用人数最多。
				spring<tag>开发</tag>基础
				spring 在java领域非常流行，java程序员都在用。
			 */
		}
	}
}
