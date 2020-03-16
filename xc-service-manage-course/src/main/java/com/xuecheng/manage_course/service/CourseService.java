package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.clienttest.CmsPaegClient;
import com.xuecheng.manage_course.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 标题：
 * 作者：何处是归程
 * 时间：2020/2/29 - 16:55
 */
@Service
public class CourseService {
	@Autowired
	TeachplanMapper teachplanMapper;
	@Autowired
	TeachplanRepository teachplanRepository;
	@Autowired
	CourseBaseRepository courseBaseRepository;
	@Autowired
	CourseMapper courseMapper;
	@Autowired
	CourseMarketRepository courseMarketRepository;
	@Autowired
	CoursePicRepository coursePicRepository;
	@Autowired
	CmsPaegClient cmsPaegClient;
	@Autowired
	CoursePubRepository coursePubRepository;
	@Autowired
	TeacherplanMediaRepostiory teacherplanMediaRepostiory;
	@Autowired
	TeacherplanMediaPubRepostiory teacherplanMediaPubRepostiory;

	// 查询课程计划
	public TeachplanNode selectList(String courseId) {
		return teachplanMapper.selectList(courseId);
	}

	// 添加课程计划
	@Transactional
	public ResponseResult addTeachplan(Teachplan teachplan) {
		if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		// 获取课程ID
		String courseid = teachplan.getCourseid();
		// 获取parentId
		String parentid = teachplan.getParentid();
		if (StringUtils.isEmpty(parentid)) {
			parentid = this.getTeachplanRoot(courseid);
		}
		// 父节点的级别
		Optional<Teachplan> optional = teachplanRepository.findById(parentid);
		Teachplan parentNode = optional.get();
		String grade = parentNode.getGrade();
		// 新节点
		Teachplan teachplanNew = new Teachplan();
		// 将页面提交的teachplan信息拷贝到teachplanNew对象中
		BeanUtils.copyProperties(teachplan, teachplanNew);
		teachplanNew.setParentid(parentid);
		teachplanNew.setCourseid(courseid);
		if (grade.equals("1")) {
			teachplanNew.setGrade("2"); // 级别，根据父节点的级别来设置
		} else {
			teachplanNew.setGrade("3"); // 级别，根据父节点的级别来设置
		}
		teachplanRepository.save(teachplanNew);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 查询课程的根节点 如果查询不到要手动添加根节点
	private String getTeachplanRoot(String courseId) {
		Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
		if (!optional.isPresent()) {
			return null;
		}
		// 课程信息
		CourseBase courseBase = optional.get();
		// 查询课程的根节点
		List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
		if (teachplanList == null || teachplanList.size() <= 0) {
			// 查询不到，要自动添加根节点
			Teachplan teachplan = new Teachplan();
			teachplan.setParentid("0");
			teachplan.setGrade("1");
			teachplan.setPname(courseBase.getName());
			teachplan.setCourseid(courseId);
			teachplan.setStatus("0");
			teachplanRepository.save(teachplan);
			return teachplan.getId();
		}
		// 返回根节点的ID
		return teachplanList.get(0).getId();
	}


	// 分页查询我的课程
	public QueryResponseResult findCourseListPage(String company_id, Integer page, Integer size, CourseListRequest courseListRequest) {
		if (page <= 0) {
			page = 0;
		}
		if (size <= 0) {
			size = 8;
		}
		if (courseListRequest == null) {
			courseListRequest = new CourseListRequest();
		}
		courseListRequest.setCompanyId(company_id);
		PageHelper.startPage(page, size);
		Page<CourseInfo> courseLise = courseMapper.findCourseListPage(courseListRequest);
		if (courseLise == null) {
			ExceptionCast.cast(CourseCode.COURSE_QUERY_ISNULL);
			return null;
		}
		QueryResult<CourseInfo> queryResult = new QueryResult<CourseInfo>();
		queryResult.setList(courseLise.getResult());
		queryResult.setTotal(courseLise.getTotal());
		return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
	}

	// 添加课程
	@Transactional
	public ResponseResult saveCourseListPage(CourseBase courseBase) {
		if (courseBase == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
			return null;
		}
		CourseBase save = courseBaseRepository.save(courseBase);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 根据ID查询课程基本信息
	public CourseBase findByCourseId(String courseId) {
		if (courseId == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
			return null;
		}
		Optional<CourseBase> optinal = courseBaseRepository.findById(courseId);
		if (!optinal.isPresent()) {
			ExceptionCast.cast(CourseCode.COURSE_QUERY_ISNULL);
			return null;
		} else {
			return optinal.get();
		}
	}

	// 修改课程基本信息
	@Transactional
	public ResponseResult updateCourse(String courseId, CourseBase courseBase) {
		if (courseBase == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
			return null;
		}
		// 查询
		CourseBase byCourseId = findByCourseId(courseId);
		if (byCourseId == null || courseId == null) {
			ExceptionCast.cast(CourseCode.COURSE_QUERY_ISNULL);
			return null;
		}
		byCourseId.setName(courseBase.getName());
		byCourseId.setMt(courseBase.getMt());
		byCourseId.setSt(courseBase.getSt());
		byCourseId.setGrade(courseBase.getGrade());
		byCourseId.setStudymodel(courseBase.getStudymodel());
		byCourseId.setUsers(courseBase.getUsers());
		byCourseId.setDescription(courseBase.getDescription());
		courseBaseRepository.save(byCourseId);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	//  "获取课程营销信息"
	public CourseMarket fingCourseMarketById(String id) {
		if (id == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
			return null;
		}
		Optional<CourseMarket> optional = courseMarketRepository.findById(id);
		if (!optional.isPresent()) {
			ExceptionCast.cast(CourseCode.COURSE_QUERY_ISNULL);
			return null;
		} else {
			return optional.get();
		}
	}

	// "修改课程营销信息"
	@Transactional
	public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket) {
		if (courseMarket == null || courseId == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
			return null;
		}
		CourseMarket courseMarketById = this.fingCourseMarketById(courseId);
		if (courseMarketById != null) {
			courseMarketById.setCharge(courseMarket.getCharge());
			courseMarketById.setStartTime(courseMarket.getStartTime());
			//课程有效期，开始时间          
			courseMarketById.setEndTime(courseMarket.getEndTime());
			// 课程有效期，结束时间        
			courseMarketById.setPrice(courseMarket.getPrice());
			courseMarketById.setQq(courseMarket.getQq());
			courseMarketById.setValid(courseMarket.getValid());
			courseMarketRepository.save(courseMarketById);
		} else {
			courseMarketById = new CourseMarket();
			BeanUtils.copyProperties(courseMarket, courseMarketById);
			courseMarketById.setId(courseId);
			courseMarketRepository.save(courseMarketById);
		}
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 向课程管理数据库添加课程与图片的关联信息
	@Transactional
	public ResponseResult addCoursePic(String courseId, String pic) {
		CoursePic coursePic = null;
		// 查询课程图片
		Optional<CoursePic> optional = coursePicRepository.findById(courseId);
		if (optional.isPresent()) {
			coursePic = optional.get();
		}
		if (coursePic == null) {
			coursePic = new CoursePic();
		}
		coursePic.setCourseid(courseId);
		coursePic.setPic(pic);
		coursePicRepository.save(coursePic);
		return new ResponseResult(CommonCode.SUCCESS);
	}

	// 查询课程图片
	public CoursePic findCoursePic(String courseId) {
		if (courseId == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		// 查询课程图片
		Optional<CoursePic> optional = coursePicRepository.findById(courseId);
		if (optional.isPresent()) {
			CoursePic coursePic = optional.get();
			return coursePic;
		}
		return null;
	}

	// 删除课程图片
	@Transactional
	public ResponseResult deleteCoursePic(String courseId) {
		if (courseId == null) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		long count = coursePicRepository.deleteByCourseid(courseId);
		if (count > 0) {
			return new ResponseResult(CommonCode.SUCCESS);
		}
		return new ResponseResult(CommonCode.FAIL);
	}

	// 查询课程的视图， 包括基本信息，图片，营销，课程计划
	public CourseView getCourseView(String id) {
		CourseView courseView = new CourseView();

		// 查询课程的基本信息
		Optional<CourseBase> courseBaseOptional = courseBaseRepository.findById(id);
		courseBaseOptional.ifPresent(courseView::setCourseBase);
		// 查询课程的图片
		Optional<CoursePic> coursePicOptional = coursePicRepository.findById(id);
		coursePicOptional.ifPresent(courseView::setCoursePic);
		// 查询课程营销
		Optional<CourseMarket> courseMarketOptional = courseMarketRepository.findById(id);
		courseMarketOptional.ifPresent(courseView::setCourseMarket);
		// 查询课程计划
		TeachplanNode teachplanNode = teachplanMapper.selectList(id);
		courseView.setTeachplanNode(teachplanNode);

		return courseView;
	}


	@Value("${course-publish.dataUrlPre}")
	private String publish_dataUrlPre;
	@Value("${course-publish.pagePhysicalPath}")
	private String publish_page_physicalpath;
	@Value("${course-publish.pageWebPath}")
	private String publish_page_webpath;
	@Value("${course-publish.siteId}")
	private String publish_siteId;
	@Value("${course-publish.templateId}")
	private String publish_templateId;
	@Value("${course-publish.previewUrl}")
	private String previewUrl2;

	// 课程预览
	public CoursePublishResult preview(String id) {
		// 查询课程
		CourseBase courseBaseById = this.findCourseBaseById(id);
		// 请求cms添加页面
		// 准备cmsPage的信息
		CmsPage cmsPage = new CmsPage();
		cmsPage.setSiteId(publish_siteId); // 站点id
		cmsPage.setDataUrl(publish_dataUrlPre + id); // 数据模型id
		cmsPage.setPageName(id + "html"); // 页面名称
		cmsPage.setPageAliase(courseBaseById.getName()); // 页面别名 就是课程名称
		cmsPage.setPagePhysicalPath(publish_page_physicalpath); // 页面的物理路径
		cmsPage.setPageWebPath(publish_page_webpath); // 页面的webpath
		cmsPage.setTemplateId(publish_templateId); // 页面模板ID

		// 远程调用cms
		CmsPageResult cmsPageResult = cmsPaegClient.save(cmsPage);
		if (!cmsPageResult.isSuccess()) {
			return new CoursePublishResult(CommonCode.FAIL, null);
		}

		CmsPage cmsPage1 = cmsPageResult.getCmsPage();
		String pageId = cmsPage1.getPageId();

		// 拼装页面预览的url
		String previewUrl = previewUrl2 + pageId;
		// 返回CoursePublishResult对象（当中包括页面预览的URL）
		return new CoursePublishResult(CommonCode.SUCCESS, previewUrl);
	}

	//根据id查询课程基本信息
	public CourseBase findCourseBaseById(String courseId) {
		Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
		if (baseOptional.isPresent()) {
			CourseBase courseBase = baseOptional.get();
			return courseBase;
		}
		ExceptionCast.cast(CourseCode.COURSE_QUERY_ISNULL);
		return null;
	}

	// 课程发布
	@Transactional
	public CoursePublishResult publish(String id) {
		// 查询课程
		CourseBase courseBaseById = this.findCourseBaseById(id);
		// 准备cmsPage的信息
		CmsPage cmsPage = new CmsPage();
		cmsPage.setSiteId(publish_siteId); // 站点id
		cmsPage.setDataUrl(publish_dataUrlPre + id); // 数据模型id
		cmsPage.setPageName(id + ".html"); // 页面名称
		cmsPage.setPageAliase(courseBaseById.getName()); // 页面别名 就是课程名称
		cmsPage.setPagePhysicalPath(publish_page_physicalpath); // 页面的物理路径
		cmsPage.setPageWebPath(publish_page_webpath); // 页面的webpath
		cmsPage.setTemplateId(publish_templateId); // 页面模板ID

		// 调用cms一键发布接口将课程详情页面发布倒服务器
		CmsPostPageResult cmsPostPageResult = cmsPaegClient.postPageQuick(cmsPage);
		if (!cmsPostPageResult.isSuccess()) {
			return new CoursePublishResult(CourseCode.COURSE_PUBLISH_VIEWERROR, null);
		}
		// 保存课程发布的状态为已发布
		CourseBase courseBase = this.savecCoursePubState(id);
		if (courseBase == null) {
			return new CoursePublishResult(CourseCode.COURSE_PUBLISH_VIEWERROR, null);
		}

		// 保存课程索引信息
		// 先创建一个CoursePub对象
		CoursePub coursePub = this.createCoursePub(id);

		// 将CoursePub对象保存到数据库
		CoursePub coursePub2 = this.saveCoursePub(id, coursePub);

		// 得到页面的URL
		String pageUrl = cmsPostPageResult.getPageUrl();
		// 向teachplanMediapub中保存课程媒资信息
		this.saveTeachplanMediaPub(id);
		return new CoursePublishResult(CommonCode.SUCCESS, pageUrl);
	}

	// 向teachplanMediaPub中保存课程媒资信息
	private void saveTeachplanMediaPub(String courseId) {
		// 先删除teachplanMediaPub中的数据
		teacherplanMediaPubRepostiory.deleteByCourseId(courseId);
		// 从teachplanMedia中查询
		List<TeachplanMedia> teachplanMediaList = teacherplanMediaRepostiory.findByCourseId(courseId);
		List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
		// 将teachplanMediaList中的数据放入teachplanMediaPubList中
		for (TeachplanMedia teachplanMedia : teachplanMediaList) {
			TeachplanMediaPub teachplanMediaPub = new TeachplanMediaPub();
			BeanUtils.copyProperties(teachplanMedia, teachplanMediaPub);
			// 添加时间戳
			teachplanMediaPub.setTimestamp(new Date());
			teachplanMediaPubList.add(teachplanMediaPub);
		}
		// 将teachplanMediaPubList插入到teachplanMediaPub表中
		teacherplanMediaPubRepostiory.saveAll(teachplanMediaPubList);
	}

	// 更改课程的状态为已发布
	private CourseBase savecCoursePubState(String courseId) {
		CourseBase courseBaseById = this.findCourseBaseById(courseId);
		courseBaseById.setStatus("202002");
		courseBaseRepository.save(courseBaseById);
		return courseBaseById;
	}

	//创建coursePub对象
	private CoursePub createCoursePub(String id) {
		CoursePub coursePub = new CoursePub();
		// 根据课程ID查询course_base
		Optional<CourseBase> optionalCourseBase = courseBaseRepository.findById(id);
		if (optionalCourseBase.isPresent()) {
			CourseBase courseBase = optionalCourseBase.get();
			// 将courseBase属性拷贝到coursePub中
			BeanUtils.copyProperties(courseBase, coursePub);
		}
		// 查询课程图片
		Optional<CoursePic> picOptional = coursePicRepository.findById(id);
		if (picOptional.isPresent()) {
			CoursePic coursePic = picOptional.get();
			BeanUtils.copyProperties(coursePic, coursePub);
		}
		// 课程营销信息
		Optional<CourseMarket> marketOptional = courseMarketRepository.findById(id);
		if (marketOptional.isPresent()) {
			CourseMarket courseMarket = marketOptional.get();
			BeanUtils.copyProperties(courseMarket, coursePub);
		}
		// 课程计划查询
		TeachplanNode teachplanNode = teachplanMapper.selectList(id);
		String jsonString = JSON.toJSONString(teachplanNode);
		// 将课程计划信息Json串保存到 course_pub中
		coursePub.setTeachplan(jsonString);
		return coursePub;
	}

	// 将CoursePub对象保存到数据库
	private CoursePub saveCoursePub(String id, CoursePub coursePub) {
		// 根据课程id查询coursePub
		CoursePub coursePubNew = null;
		Optional<CoursePub> optionalCoursePub = coursePubRepository.findById(id);
		if (optionalCoursePub.isPresent()) {
			coursePubNew = optionalCoursePub.get();
		} else {
			coursePubNew = new CoursePub();
		}
		// 将coursePub对象中的信息保存到coursePubNew中
		BeanUtils.copyProperties(coursePub, coursePubNew);
		coursePubNew.setId(id);
		// 时间戳 给logstach使用
		coursePubNew.setTimestamp(new Date());
		// 发布时间
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		String date = simpleDateFormat.format(new Date());
		coursePubNew.setPubTime(date);
		coursePubRepository.save(coursePubNew);
		return coursePubNew;
	}

	// 保存课程计划与媒资文件的关联
	public ResponseResult savemedia(TeachplanMedia teachplanMedia) {
		if (teachplanMedia == null || StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
		}
		// 校验课程计划是否是3级
		// 课程计划ID
		String teachplanId = teachplanMedia.getTeachplanId();
		// 查询到课程计划
		Optional<Teachplan> teachplanOptional = teachplanRepository.findById(teachplanId);
		if (!teachplanOptional.isPresent()) {
			ExceptionCast.cast(CommonCode.INVALLD_PARAM);
			return null;
		}
		// 查询到教学计划
		Teachplan teachplan = teachplanOptional.get();
		// 取出等级
		String grade = teachplan.getGrade();
		if (StringUtils.isEmpty(grade) || !grade.equals("3")) {
			ExceptionCast.cast(CourseCode.COURSE_MEDIS_THREE);
			return null;
		}
		// 根据teachplanMedia
		Optional<TeachplanMedia> teachplanMediaOptional = teacherplanMediaRepostiory.findById(teachplanId);
		TeachplanMedia one = null;
		if (teachplanMediaOptional.isPresent()) {
			one = teachplanMediaOptional.get();
		} else {
			one = new TeachplanMedia();
		}
		// 将one保存到数据库
		one.setCourseId(teachplan.getCourseid()); // 课程ID
		one.setMediaId(teachplanMedia.getMediaId()); // 文件ID
		one.setMediaFileOriginalName(teachplanMedia.getMediaFileOriginalName()); // 媒资文件的原始名称
		one.setMediaUrl(teachplanMedia.getMediaUrl());
		one.setTeachplanId(teachplanId);
		teacherplanMediaRepostiory.save(one);
		return new ResponseResult(CommonCode.SUCCESS);
	}
}
