package com.xuecheng.order.dao;

import com.xuecheng.framework.domain.task.XcTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/3/16 - 10:09
 */
public interface XcTaskRepository extends JpaRepository<XcTask, String> {
	/**
	 * @功能: 查询某个时间之前的前n条任务
	 * @作者: 高志红
	 */
	Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

	/**
	 * @功能: 更新updateTime
	 * @作者: 高志红
	 */
	@Modifying
	@Query("update XcTask  t set t.updateTime = :updateTime where t.id = :id")
	public int updateTaskTime(@Param("id") String id, @Param("updateTime") Date updateTime);

	/**
	 * @功能: 使用乐观锁方式校验任务id和版本号是否匹配，匹配则版本号加1
	 * @作者: 高志红
	 */
	@Modifying
	@Query("update XcTask  t set t.version = :version + 1 where t.id = :id and t.version = :version")
	public int updateRaskCersion(@Param("id") String id, @Param("version") int version);
}
