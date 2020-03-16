package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/3 - 9:04
 */
public interface FileSystemRepository extends MongoRepository<FileSystem, String> { }
