package com.esportzoo.esport.controller;

import com.esportzoo.esport.domain.UserConsumer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.common.ClientAdPicServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.CmsContentRequest;
import com.esportzoo.esport.connect.request.IndexRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.AdPicPlaceType;
import com.esportzoo.esport.domain.ClientAdPic;
import com.esportzoo.esport.domain.NewsListVo;
import com.esportzoo.esport.manager.CmsContentManager;
import com.esportzoo.esport.manager.IndexManager;

/**
 * 首页数据
 * 
 * @author: wujing
 * @date:2019年4月18日下午4:36:05
 */
@Controller
@Api(value = "首页数据接口", tags = { "首页的controller" })
public class IndexController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	IndexManager indexManager;
	@Autowired
	CmsContentManager cmsContentManager;
	@Autowired
	ClientAdPicServiceClient clientAdPicServiceClient;

	@RequestMapping(value = "/indexData", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "首页数据接口POST", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "首页数据POST", response = CommonResponse.class)
	public @ResponseBody CommonResponse<JSONObject> index(HttpServletRequest request, IndexRequest indexRequest) {
		CommonResponse<JSONObject> data = indexManager.getIndexData(indexRequest);
		return data;
	}

	@RequestMapping(value = "/newlist", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "资讯内容列表接口POST", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "资讯内容列表接口POST", response = CommonResponse.class)
	public @ResponseBody CommonResponse<List<NewsListVo>> newlist(HttpServletRequest request, CmsContentRequest cmsContentRequest) {
		UserConsumer userConsumer = getLoginUsr(request);
		Long userId=null;
		if (userConsumer!=null){
			userId=userConsumer.getId();
		}
		List<NewsListVo> contentList = cmsContentManager.getCmsContentList(cmsContentRequest,userId);
		return CommonResponse.withSuccessResp(contentList);
	}

	/** 根据轮播图类型列表,加载数据 */
	@RequestMapping(value = "/getAdList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "首页广告位数据接口POST", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "首页广告位数据POST", response = CommonResponse.class)
	public @ResponseBody CommonResponse<JSONObject> index(HttpServletRequest request, BaseRequest baseRequest, String typeStr) {
		JSONObject jsonObject = new JSONObject();
		try {
			logger.info("根据轮播图类型列表,传入参数:{}", typeStr);
			if (StringUtils.isBlank(typeStr)) {
				return CommonResponse.withErrorResp("传入接口参数为空");
			}

			List<Integer> typeList = Arrays.asList(typeStr.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
			if (typeList.isEmpty()) {
				return CommonResponse.withErrorResp("传入接口参数有误");
			}
			for (Integer type : typeList) {
				AdPicPlaceType picPlaceType = AdPicPlaceType.valueOf(type);
				if (picPlaceType == null) {
					return CommonResponse.withErrorResp("传入接口参数中类型有误");
				}
			}
			ModelResult<List<ClientAdPic>> listAdResult = clientAdPicServiceClient.queryListByAdTypeList(typeList);
			if (null == listAdResult || !listAdResult.isSuccess()) {
				return CommonResponse.withErrorResp("调用广告位接口异常");
			}
			List<ClientAdPic> clientAdPicList = listAdResult.getModel();
			clientAdPicList = clientAdPicList.stream().filter(x -> x.getClientType().equals(baseRequest.getClientType())).collect(Collectors.toList());
			for (Integer type : typeList) {
				List<ClientAdPic> bannerList = clientAdPicList.stream().filter(ad -> ad.getPosition().equals(type)).collect(Collectors.toList());
				jsonObject.put("adList" + type, bannerList);
			}
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.error("根据轮播图类型列表数据出错,出错信息:{}", e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("系统出错");
		}
	}

}