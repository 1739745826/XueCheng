package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.SysDicthinaryRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/1 - 14:28
 */
@Service
public class SysDicthinaryService {
	@Autowired
	SysDicthinaryRespository sysDicthinaryRespository;

	// 根据类型查询数据字典
	public SysDictionary findByType(String type) {
		if (type == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
			return null;
		}
		SysDictionary sysDictionary = sysDicthinaryRespository.findBydType(type);
		if (sysDictionary == null) {
			ExceptionCast.cast(CommonCode.UNAUTHORISE);
			return null;
		}
		return sysDictionary;
	}
}
