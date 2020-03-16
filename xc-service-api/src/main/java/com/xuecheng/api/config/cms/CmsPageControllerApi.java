package com.xuecheng.api.config.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.*;

/**
 * 标题：页面查询接口
 * 作者：何处是归程
 * 时间：2020/2/25 - 13:53
 */
@Api(value = "cms页面管理接口", description = "cms页面管理接口，提供页面的增、删、改、查")
public interface CmsPageControllerApi {
	// 页面查询
	@ApiOperation("分页查询页面列表")
	public QueryResponseResult findList(Integer page, Integer size, QueryPageRequest queryPageRequest);

	// 新增页面
	@ApiOperation("新增页面")
	public CmsPageResult add(CmsPage cmsPage);

	// 根据页面的ID查询页面信息
	@ApiOperation("根据页面的ID查询页面信息")
	public CmsPage findById(String id);

	// 修改页面
	@ApiOperation("修改页面")
	public CmsPageResult edit(String id, CmsPage cmsPage);

	// 删除页面
	@ApiOperation("删除页面")
	public ResponseResult delelte(String id);

	// 页面发布
	@ApiOperation("页面发布")
	public ResponseResult post(String pageId);

	// 保存页面
	@ApiOperation("保存页面")
	public ResponseResult save(CmsPage cmsPage);

	// 一键发布页面
	@ApiOperation("一键发布")
	public CmsPostPageResult postPageQuick(CmsPage cmsPage);
}
