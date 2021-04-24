package com.esportzoo.esport.controller.follow;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.connect.request.FollowRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.NewsListVo;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CmsContentManager;
import com.esportzoo.esport.option.CmsContentParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
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
@Api(value = "用户关注相关页面", tags = { "用户关注相关接口" })
public class FollowController  extends BaseController {

	public static final String logPrefix ="查询关注用户发布资讯列表:";

	@Autowired
	CmsContentManager cmsContentManager;






	@RequestMapping(value = "/cmsList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询关注用户发布资讯列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询关注用户发布资讯列表", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> getCmsList(HttpServletRequest request, FollowRequest followRequest) {
		long startTime = System.currentTimeMillis();
		logger.info(logPrefix+"----开始请求，查询关注用户发布资讯列表接口---");
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer!=null){
			followRequest.setUserId(userConsumer.getId());
		}
		if (followRequest==null){
			logger.info(logPrefix+"用户：【{}】，参数错误",userConsumer.getId());
			return CommonResponse.withErrorResp("参数错误");
		}

		try {
			if (userConsumer!=null){
				followRequest.setUserId(userConsumer.getId());
			}
			// 查询所有用户的资讯列表 标记
			followRequest.setShowFollowUserCms(false);
			followRequest.setShowUserCms(true);
			List<NewsListVo> cmsContentList = cmsContentManager.getFollowerCmsContentList(followRequest);
			logger.info("用户关注资讯列表排重之前 【{}】", cmsContentList.size());
			HashSet<Long> ids = new HashSet<>();
			ArrayList<NewsListVo> al = new ArrayList();
			for (NewsListVo newsListVo : cmsContentList) {
				if (!ids.contains(newsListVo.getAricleId())){
				    al.add(newsListVo);
				    ids.add(newsListVo.getAricleId());
				}
			}
			jsonObject.put("cmsContentList", al);
			//加载置顶内容 userId可为空
			if (followRequest.getShowTop()){
				CmsContentParam param = new CmsContentParam();
				param.setAgentId(followRequest.getAgentId());
				param.setBiz(followRequest.getBiz());
				param.setClientType(followRequest.getClientType());
				jsonObject.put("topCmsContentList",cmsContentManager.getTopContentList(followRequest.getUserId(),param));
			}
			logger.info("请求结束------执行消耗时间：{}ms--------", DateUtil.spendMs(startTime));
		} catch (Exception e) {
			logger.info(logPrefix+"用户id：{}", userConsumer.getId(), e);
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);
		}

		return CommonResponse.withSuccessResp(jsonObject);

	}

	public static void main(String[] args) {
	}
}
