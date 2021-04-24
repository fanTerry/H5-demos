package com.esportzoo.esport.controller.follow;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.connect.request.FollowRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.BaseOrderField;
import com.esportzoo.esport.constants.CmsContentStatus;
import com.esportzoo.esport.constants.CmsRecommend;
import com.esportzoo.esport.constants.CmsTypeDefineConstant;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.CmsContent;
import com.esportzoo.esport.domain.NewsListVo;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CmsContentManager;
import com.esportzoo.esport.option.CmsContentParam;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description: 用户关注相关页面
 *
 * @author: Haitao.Li
 *
 * @create: 2019-08-19 11:08
 **/
@Controller
@RequestMapping("follow")
@Api(value = "用户关注相关页面", tags = {"用户关注相关接口"})
public class FollowController extends BaseController {

	 private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

	public static final String logPrefix = "查询关注用户发布资讯列表:";

	@Autowired
	CmsContentManager cmsContentManager;

	@Autowired
	@Qualifier("cmsContentServiceClient")
	CmsContentServiceClient cmsContentServiceClient;


	@RequestMapping(value = "/cmsList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询关注用户发布资讯列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询关注用户发布资讯列表", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> getCmsList(HttpServletRequest request, FollowRequest followRequest) {
		long startTime = System.currentTimeMillis();
		logger.info(logPrefix + "----开始请求，查询关注用户发布资讯列表接口---");
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer != null) {
			followRequest.setUserId(userConsumer.getId());
		}
		if (followRequest == null) {
			logger.info(logPrefix + "用户：【{}】，参数错误", userConsumer.getId());
			return CommonResponse.withErrorResp("参数错误");
		}

		try {
			if (userConsumer != null) {
				followRequest.setUserId(userConsumer.getId());
			}else {
				followRequest.setShowTop(Boolean.FALSE);
			}
			// 查询所有用户的资讯列表 标记
			followRequest.setShowFollowUserCms(false);
			followRequest.setShowUserCms(true);
			List<NewsListVo> cmsContentList = cmsContentManager.getFollowerCmsContentList(followRequest);
			logger.info("用户[{}]关注资讯列表排重之前 【{}】", followRequest.getUserId(),cmsContentList.size());
			HashSet<Long> ids = new HashSet<>();
			ArrayList<NewsListVo> al = new ArrayList();
			for (NewsListVo newsListVo : cmsContentList) {
				if (!ids.contains(newsListVo.getAricleId())) {
					al.add(newsListVo);
					ids.add(newsListVo.getAricleId());
				}
			}
			jsonObject.put("cmsContentList", al);
			//加载置顶内容 userId可为空
			List<NewsListVo> topCmsList=new ArrayList<>();
			if (followRequest.getShowTop()) {
				topCmsList = getTopCmsList(followRequest);
			}
			jsonObject.put("topCmsContentList", topCmsList);
			logger.info("请求结束------执行消耗时间：{}ms--------", DateUtil.spendMs(startTime));
		} catch (Exception e) {
			logger.info(logPrefix + "用户id：{}", userConsumer.getId(), e);
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE, ResponseConstant.SYSTEM_ERROR_MESG);
		}

		return CommonResponse.withSuccessResp(jsonObject);

	}


	@RequestMapping(value = "/recommendList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询发现页推荐资讯列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询发现页推荐资讯列表", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> getRecommendList(HttpServletRequest request, FollowRequest followRequest) {
		logger.info(logPrefix + "----开始请求，查询关注用户发布资讯列表接口---");
		JSONObject jsonObject = new JSONObject();

		DataPage<CmsContent> page = new DataPage<>();
		page.setPageSize(followRequest.getPageSize());
		page.setPageNo(followRequest.getPageNo());
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer!=null){
			followRequest.setUserId(userConsumer.getId());
		}
		try {

			CmsContentParam param = new CmsContentParam();
			List<Integer> typeList = Lists.newArrayList();
			//查询全部文章和视频资讯
			typeList.add(CmsTypeDefineConstant.USER_ARTICLE_SHORT.getIndex());
			typeList.add(CmsTypeDefineConstant.USER_VIDEO.getIndex());
			param.setTypeList(typeList);
			param.setStatus(CmsContentStatus.ISSUE.getIndex());
			param.setShowTop(false);
			param.setIsRecommend((short) CmsRecommend.POPULAR.getIndex()); //推荐
			param.setClientType(followRequest.getClientType());
			param.setAgentId(followRequest.getAgentId());
			param.setBiz(followRequest.getBiz());
			LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
			hashMap.put(BaseOrderField.UPDATE_TIME, BaseOrderField.DESC);
			param.setOrderField(hashMap);
			if (followRequest.getLastUpdateTime() != null) {
				param.setEndUpdateTime(DateUtil.calendar(Long.valueOf(followRequest.getLastUpdateTime())).getTime());
			}
			PageResult<CmsContent> pageResult = cmsContentServiceClient.queryCmsContentPageForShortArticle(param, page);
			if (!pageResult.isSuccess()) {
				logger.error(logPrefix + "参数param 【{}】，异常信息：{}", JSON.toJSONString(param), pageResult.getErrorMsg());
				return CommonResponse.withErrorResp("查询数据错误");
			}
			List<CmsContent> cmsContentList = pageResult.getPage().getDataList();
			logger.info(logPrefix + "查询实际返回记录数 【{}】", cmsContentList.size());

			List<NewsListVo> listVos = cmsContentManager.convertCmsToNews(cmsContentList);
			logger.info(logPrefix + "过滤后的返回记录数 【{}】", listVos.size());
			//设置点赞和关注

			if (followRequest.getUserId()==null){
				cmsContentManager.setFollowedAndUserUp(null, listVos);

			}else {
				cmsContentManager.setFollowedAndUserUp(userConsumer.getId(), listVos);
			}


			logger.info("用户关注资讯列表排重之前 【{}】", cmsContentList.size());
			HashSet<Long> ids = new HashSet<>();
			ArrayList<NewsListVo> al = new ArrayList();
			for (NewsListVo newsListVo : listVos) {
				if (!ids.contains(newsListVo.getAricleId())) {
					al.add(newsListVo);
					ids.add(newsListVo.getAricleId());
				}
			}
			jsonObject.put("cmsContentList", al);
			List<NewsListVo> topCmsList=new ArrayList<>();
			if (followRequest.getShowTop()) {
				topCmsList = getTopCmsList(followRequest);
			}
			jsonObject.put("topCmsContentList", topCmsList);
			return CommonResponse.withSuccessResp(jsonObject);

		} catch (Exception e) {
			logger.error(logPrefix + "查询发现页推荐资讯列表，参数param 【{}】，异常信息：{}", JSON.toJSONString(followRequest), e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询异常");
		}
	}

	private List<NewsListVo> getTopCmsList (FollowRequest followRequest){
		//加载置顶内容 userId可为空
		CmsContentParam param = new CmsContentParam();
		param.setAgentId(followRequest.getAgentId());
		param.setBiz(followRequest.getBiz());
		param.setClientType(followRequest.getClientType());
		return cmsContentManager.getTopContentList(followRequest.getUserId(), param);

	}


}
