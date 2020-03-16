package com.xuecheng.api.config.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 14:15
 */
@Api(value = "数据字典查询接口", description = "数据字典的查询")
public interface SysDicthinaryControllerApi {
	@ApiOperation(value="数据字典查询接口")
	public SysDictionary findByType(String type);
}
