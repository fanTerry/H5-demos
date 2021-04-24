package com.esportzoo.esport.controller.cms;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.FollowRequest;
import com.esportzoo.esport.connect.request.cms.UserPublishContentRequest;
import com.esportzoo.esport.connect.response.CmsMsgResponse;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.CmsContentStatus;
import com.esportzoo.esport.constants.CmsTypeDefineConstant;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.CmsContent;
import com.esportzoo.esport.domain.NewsListVo;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CmsContentManager;
import com.esportzoo.esport.manager.CommonManager;
import com.esportzoo.esport.option.CmsContentParam;
import com.esportzoo.esport.vo.cms.UserPublishContentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 用户发布短文相关
 *
 * @author: Haitao.Li
 *
 * @create: 2019-08-23 14:24
 **/

@Controller
@RequestMapping("userContent")
@Api(value = "用户短文相关接口", tags = {"用户短文相关接口"})
public class UserContentController extends BaseController {

	public static final String logPrefix = "用户短文相关接口";

	@Autowired
	@Qualifier("cmsContentServiceClient")
	private CmsContentServiceClient cmsContentServiceClient;
	@Autowired
	private CommonManager commonManager;
	@Value("${shortcms.upload.path}")
	private String uploadPath;

	@Value("${shortcms.res.host}")
	private String resPath;

	@Autowired
	private CmsContentManager cmsContentManager;


	@RequestMapping(value = "/publish", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "发布", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@ApiResponse(code = 200, message = "发布", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> publishContent(HttpServletRequest request,
			@RequestParam(value = "files", required = false) CommonsMultipartFile[] files, UserPublishContentRequest publishRequest) {
		try {
			JSONObject jsonObject = new JSONObject();
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			logger.info(logPrefix + "当前发布用户【{}】，发布基础参数[{}]", userConsumer.getId(), JSONObject.toJSONString(publishRequest));

			Integer typeId = publishRequest.getTypeId();
			if (null == typeId || null == CmsTypeDefineConstant.valueOf(typeId)) {
				return CommonResponse.withErrorResp("要发布的类型不对");
			}
			if (StringUtils.isBlank(publishRequest.getContent())) {
				return CommonResponse.withErrorResp("请输入短文内容");
			}
			String uploadImage = "";
			if (files != null && files.length > 0) {
				logger.info("files.length=[{}]", files.length);
				uploadImage = commonManager.uploadImage(files, uploadPath, resPath, typeId);
				if (StringUtils.isEmpty(uploadImage)) {
					return CommonResponse.withErrorResp("上传图片或者视频失败");
				}
				logger.info(logPrefix + "发布用户【】，上传成功后的地址[{}]", userConsumer.getId(), uploadImage);
			}
			UserPublishContentVo vo = new UserPublishContentVo();
			vo.setNickName(userConsumer.getNickName());
			vo.setUserId(userConsumer.getId());
			vo.setTypeId(typeId);
			if (typeId.intValue() == CmsTypeDefineConstant.USER_VIDEO.getIndex()) {// 用户发布视频
				vo.setTitle(publishRequest.getContent());
				// 通过视频截取第一帧 保存为图片
				if (StringUtils.isNotBlank(uploadImage)) {
					vo.setContent(uploadImage);
				}
			} else {
				vo.setContent(publishRequest.getContent());
				vo.setImgUrls(uploadImage);
			}
			ModelResult<Long> modelResult = cmsContentServiceClient.userPublishCmsContent(vo);
			if (null == modelResult || !modelResult.isSuccess() || null == modelResult.getModel()) {
				return CommonResponse.withErrorResp("发布异常");
			}
			jsonObject.put("cmsId", modelResult.getModel());
			jsonObject.put("uploadImage", uploadImage);
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.info(logPrefix + "用户发布文章出现异常,异常信息:{}", e.getMessage(), e);
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE, ResponseConstant.SYSTEM_ERROR_MESG);

		}
	}

	@RequestMapping("updateVideoTitleImg")
	@ApiOperation(value = "处理视频封面", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "话题页列表", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<Long> updateVideoTitleImg(HttpServletRequest request, String videoUrl, Long cmsId, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null || null == cmsId) {
				return CommonResponse.withErrorResp("用户未登录或参数异常");
			}
			String img = "";
			if (StringUtils.isNotBlank(videoUrl)) {
				logger.info(logPrefix + "开始截取视频第一帧图片 cmsId={} videoUrl={} ", cmsId, videoUrl);
				img = commonManager.getTitleImgByVideo(videoUrl, uploadPath, resPath);
				logger.info(logPrefix + "用户发布文章视频第一帧图片:{}", img);
			} else {
				logger.info(logPrefix + "获取用户第一帧视频失败，cmsId={} videoUrl={} ", cmsId, videoUrl);
			}
			if (StringUtils.isNotBlank(img)) {
				CmsContent cmsContent = new CmsContent();
				cmsContent.setId(cmsId);
				cmsContent.setTitleImg(img);
				cmsContentServiceClient.updateCmsContentById(cmsContent);
				return CommonResponse.withSuccessResp("处理成功");
			}
			return CommonResponse.withErrorResp("处理异常");
		} catch (Exception e) {
			logger.info(logPrefix + "用户发布文章出现异常,异常信息:{}", e.getMessage(), e);
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE, ResponseConstant.SYSTEM_ERROR_MESG);

		}
	}


	@RequestMapping(value = "/getUserContent", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取用户发布的短文", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取用户发布的短文", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<NewsListVo>> getUserContent(HttpServletRequest request, FollowRequest followRequest) {
		logger.info("{}获取用户发布的短文接口,当前请求接口参数:{}", logPrefix, JSONObject.toJSONString(followRequest));
		DataPage<NewsListVo> userContentListDataPage = new DataPage<>();
		UserConsumer userConsumer = getLoginUsr(request);
		if (followRequest == null && followRequest.getUserId() == null) {
			logger.info(logPrefix + "用户：【{}】，参数错误", userConsumer.getId());
			return CommonResponse.withErrorResp("参数错误");
		}
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		try {
			DataPage<CmsContent> page = new DataPage<>();
			page.setPageSize(followRequest.getPageSize());
			page.setPageNo(followRequest.getPageNo());
			CmsContentParam param = new CmsContentParam();
			if (followRequest.getUserId() != null) {
				param.setCreateUserId( followRequest.getUserId().intValue() );
			}
			List<Integer> publishType = CmsTypeDefineConstant.getUserPublishType();
			param.setTypeList(publishType);
			param.setBiz(followRequest.getBiz());
			param.setClientType(followRequest.getClientType());
			param.setAgentId(followRequest.getAgentId());
			if (followRequest.getLastUpdateTime() != null) {
				param.setEndUpdateTime(DateUtil.calendar(Long.valueOf(followRequest.getLastUpdateTime())).getTime());
			}
			param.setStatus(CmsContentStatus.ISSUE.getIndex());
			PageResult<CmsContent> pageResult = cmsContentServiceClient.queryCmsContentPageByUserId(param, page);
			if (!pageResult.isSuccess()) {
				logger.error(logPrefix + "用户发布的短文，用户ID：【{}】，参数param 【{}】", followRequest.getUserId(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp(pageResult.getErrorMsg());
			}

			DataPage<CmsContent> userContentDataPage = pageResult.getPage();
			if (userContentDataPage.getDataList() != null) {
				List<CmsContent> cmsContentList = userContentDataPage.getDataList();
				List<NewsListVo> newsListVos = cmsContentManager.convertCmsToNews(cmsContentList);
				cmsContentManager.setFollowedAndUserUp(userConsumer.getId(), newsListVos);
				userContentListDataPage.setPageSize(userContentDataPage.getPageSize());
				userContentListDataPage.setPageNo(userContentDataPage.getPageNo());
				userContentListDataPage.setTotalCount(userContentDataPage.getTotalCount());
				userContentListDataPage.setDataList(newsListVos);
				return CommonResponse.withSuccessResp(userContentListDataPage);
			}
		} catch (Exception e) {
			logger.info(logPrefix + "用户id：{},异常信息：{}", userConsumer.getId(), e);
			e.printStackTrace();
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE, ResponseConstant.SYSTEM_ERROR_MESG);
		}
		return CommonResponse.withErrorResp("查询用户发布的短文失败");
	}


	@RequestMapping(value = "/getUserRecord", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取用户记录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取用户记录", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<CmsMsgResponse>> getUserRecord(HttpServletRequest request, FollowRequest followRequest) {
		logger.info("{}获取用户记录,当前请求接口参数:{}", logPrefix, JSONObject.toJSONString(followRequest));
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		if (followRequest.getCmsMsgType() == null) {
			logger.info(logPrefix + "用户：【{}】，参数为空", userConsumer.getId());
			return CommonResponse.withErrorResp("参数为空");
		}
		DataPage<CmsContent> dataPage = new DataPage<>();
		Integer pageSize = followRequest.getPageSize();
		if (pageSize > 20) {
			pageSize = 20;
		}
		dataPage.setPageSize(pageSize);
		dataPage.setPageNo(followRequest.getPageNo());

		DataPage<CmsMsgResponse> pageResult = cmsContentManager.getUserRecord(followRequest, dataPage, userConsumer);

		return CommonResponse.withSuccessResp(pageResult);
	}


}
