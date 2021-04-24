package com.esportzoo.esport.controller.expert;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.expert.RecExpertColumnArticleServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertServiceClient;
import com.esportzoo.esport.client.service.expert.RecOrderServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.expert.QueryExpertArticleListRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.expert.ExpertArticleResponse;
import com.esportzoo.esport.connect.response.expert.ExpertArticleVo;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.cms.expert.ExpertArticleStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.RecExpertColumnArticle;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.match.MatchManager;
import com.esportzoo.esport.vo.expert.ExpertBaseInfoQueryParam;
import com.esportzoo.esport.vo.expert.ExpertBaseInfoVo;
import com.esportzoo.esport.vo.expert.RecExpertColumnArticleQueryVo;
import com.esportzoo.esport.vo.expert.RecOrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tingting.shen
 * @date 2019/05/10
 */
@Controller
@RequestMapping("recExpert")
@Api(value = "专家相关接口", tags = { "专家相关接口" })
public class RecExpertController  extends BaseController {
	
	@Autowired
	private RecExpertServiceClient recExpertServiceClient;
	@Autowired
	private RecExpertColumnArticleServiceClient recExpertColumnArticleServiceClient;
	@Autowired
	private RecOrderServiceClient recOrderServiceClient;
	@Autowired
	private MatchManager matchManager;
	
	@RequestMapping(value = "/baseInfo/{userId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "专家基础信息查询接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "专家基础信息查询接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ExpertBaseInfoVo> baseInfo(@ApiParam(required=true, name="专家用戶id") @PathVariable("userId") Long userId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("专家基础信息查询接口,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			ExpertBaseInfoQueryParam param = new ExpertBaseInfoQueryParam();
			param.setLoginUserId(userConsumer.getId());
			param.setUserId(userId);
			ModelResult<ExpertBaseInfoVo> modelResult = recExpertServiceClient.queryExpertBaseInfo(param);
			if (!modelResult.isSuccess()) {
				logger.info("专家基础信息查询接口,调用接口返回错误,errorMsg={},param={}", modelResult.getErrorMsg(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("专家基础信息查询接口,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/baseInfo/nologin/{userId}", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "专家基础信息查询接口无需登录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "专家基础信息查询接口无需登录", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ExpertBaseInfoVo> baseInfoNoLogin(@ApiParam(required=true, name="专家用戶id") @PathVariable("userId") Long userId, HttpServletRequest request) {
		try {
			ExpertBaseInfoQueryParam param = new ExpertBaseInfoQueryParam();
			param.setUserId(userId);
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer != null) {
				param.setLoginUserId(userConsumer.getId());
			}
			ModelResult<ExpertBaseInfoVo> modelResult = recExpertServiceClient.queryExpertBaseInfo(param);
			if (!modelResult.isSuccess()) {
				logger.info("专家基础信息查询接口无需登录,调用接口返回错误,errorMsg={},param={}", modelResult.getErrorMsg(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("专家基础信息查询接口无需登录,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/articleList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "分页查询专家文章列表接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "分页查询专家文章列表接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ExpertArticleResponse> pageQueryArticleList(QueryExpertArticleListRequest param, HttpServletRequest request,BaseRequest baseRequest) {
		try {
			UserConsumer loginUser = getLoginUsr(request);
			if (loginUser == null) {
				logger.info("分页查询专家文章列表接口,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			RecExpertColumnArticleQueryVo articleQueryVo = new RecExpertColumnArticleQueryVo();
			articleQueryVo.setUserId(param.getUserId());
			articleQueryVo.setStatus(ExpertArticleStatus.ENABLE.getIndex());
			if (null != baseRequest.getClientType() && baseRequest.getClientType().intValue() == ClientType.WXXCY.getIndex()) {
				/** 专家文章是否允许展示付费开关 */
				SysConfigProperty sysConfigPropertyByKey = getSysConfigByKey(SysConfigPropertyKey.EXPERT_ARTICLE_SHOW_NOTFREE_SWITCH, baseRequest.getClientType(),baseRequest.getAgentId());
				if (sysConfigPropertyByKey != null && StringUtils.isNotBlank(sysConfigPropertyByKey.getValue())) {
					String miniSwitch = sysConfigPropertyByKey.getValue();
					if ("0".equals(miniSwitch)) {// 文章只显示免费
						articleQueryVo.setIsFree(ArticleFreeType.FREE.getIndex());
					}
				}
			}
			DataPage<RecExpertColumnArticle> dataPage = new DataPage<>();
			dataPage.setPageNo(param.getPageNo());
			dataPage.setPageSize(param.getPageSize());
			PageResult<RecExpertColumnArticle> pageResult = recExpertColumnArticleServiceClient.queryPage(articleQueryVo, dataPage);
			if (!pageResult.isSuccess()) {
				logger.info("分页查询专家文章列表接口,调用接口返回错误,errorMsg={},param={}",pageResult.getErrorMsg(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp(pageResult.getErrorMsg());
			}
			DataPage<RecExpertColumnArticle> resultDataPage = pageResult.getPage();
			List<RecExpertColumnArticle> sourceList = resultDataPage.getDataList();
			List<ExpertArticleVo> targetList = new ArrayList<>();
			if (sourceList!=null && sourceList.size()>0) {
				for (RecExpertColumnArticle article : sourceList) {
					ExpertArticleVo vo = new ExpertArticleVo();
					vo.setUserId(param.getUserId());
					vo.setId(article.getId());
					vo.setTitle(article.getTitle());
					vo.setIsFree(article.getIsFree());
					vo.setPrice(article.getPrice());
					if (article.getIsFree() == ArticleFreeType.FREE.getIndex()) {
						vo.setViews(cachedManager.getCacheSetSize(CacheType.EPERT_ARTICEL.getIndex(), article.getId() + "").intValue());
					} else {
						//付费文章取数据的阅读数
						vo.setViews(article.getViews() == null ? 0 : article.getViews());
					}
					vo.setVideogameId(article.getVideogameId());
					vo.setPublishTime(DateUtil.dateToString(article.getCreateTime().getTime(), "yyyy-MM-dd HH:mm"));
					vo.setMatchList(matchManager.getMatchVoListByMatchStr(article.getMatchIdList()));
					/**当前登录用户是否已经支付了该文章*/
					if (loginUser != null) {
						RecOrderQueryVo recOrderQueryVo = new RecOrderQueryVo();
						recOrderQueryVo.setUserId(loginUser.getId());
						recOrderQueryVo.setColumnArticleId(article.getId());
						recOrderQueryVo.setPayStatus(ArticlePayStatus.PAY_SUCCESS.getIndex());
						ModelResult<Integer> mr = recOrderServiceClient.queryCount(recOrderQueryVo);
						if (mr.isSuccess() && mr.getModel()!=null && mr.getModel().intValue()>0) {
							vo.setHasPayed(true);
						}
					}
					targetList.add(vo);
				}
			}
			ExpertArticleResponse response = new ExpertArticleResponse();
			response.setTotalPages(resultDataPage.getTotalPages());
			response.setArticleList(targetList);
			return CommonResponse.withSuccessResp(response);
		} catch (Exception e) {
			logger.info("分页查询专家文章列表接口,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	} 
	
	
	@RequestMapping(value = "/articleList/nologin", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "分页查询专家文章列表接口无需登录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "分页查询专家文章列表接口无需登录", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ExpertArticleResponse> pageQueryArticleListNoLogin(QueryExpertArticleListRequest param, HttpServletRequest request) {
		try {
			UserConsumer loginUser = getLoginUsr(request);
			RecExpertColumnArticleQueryVo articleQueryVo = new RecExpertColumnArticleQueryVo();
			articleQueryVo.setUserId(param.getUserId());
			articleQueryVo.setStatus(ExpertArticleStatus.ENABLE.getIndex());
			DataPage<RecExpertColumnArticle> dataPage = new DataPage<>();
			dataPage.setPageNo(param.getPageNo());
			dataPage.setPageSize(param.getPageSize());
			PageResult<RecExpertColumnArticle> pageResult = recExpertColumnArticleServiceClient.queryPage(articleQueryVo, dataPage);
			if (!pageResult.isSuccess()) {
				logger.info("分页查询专家文章列表接口,调用接口返回错误,errorMsg={},param={}",pageResult.getErrorMsg(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp(pageResult.getErrorMsg());
			}
			DataPage<RecExpertColumnArticle> resultDataPage = pageResult.getPage();
			List<RecExpertColumnArticle> sourceList = resultDataPage.getDataList();
			List<ExpertArticleVo> targetList = new ArrayList<>();
			if (sourceList!=null && sourceList.size()>0) {
				for (RecExpertColumnArticle article : sourceList) {
					ExpertArticleVo vo = new ExpertArticleVo();
					vo.setUserId(param.getUserId());
					vo.setId(article.getId());
					vo.setTitle(article.getTitle());
					vo.setIsFree(article.getIsFree());
					vo.setPrice(article.getPrice());
					if (article.getIsFree() == ArticleFreeType.FREE.getIndex()) {
						vo.setViews(cachedManager.getCacheSetSize(CacheType.EPERT_ARTICEL.getIndex(), article.getId() + "").intValue());
					} else {
						//付费文章取数据的阅读数
						vo.setViews(article.getViews() == null ? 0 : article.getViews());
					}
					vo.setVideogameId(article.getVideogameId());
					vo.setPublishTime(DateUtil.dateToString(article.getCreateTime().getTime(), "yyyy-MM-dd HH:mm"));
					vo.setMatchList(matchManager.getMatchVoListByMatchStr(article.getMatchIdList()));
					/**当前登录用户是否已经支付了该文章*/
					if (loginUser != null) {
						RecOrderQueryVo recOrderQueryVo = new RecOrderQueryVo();
						recOrderQueryVo.setUserId(loginUser.getId());
						recOrderQueryVo.setColumnArticleId(article.getId());
						recOrderQueryVo.setPayStatus(ArticlePayStatus.PAY_SUCCESS.getIndex());
						ModelResult<Integer> mr = recOrderServiceClient.queryCount(recOrderQueryVo);
						if (mr.isSuccess() && mr.getModel()!=null && mr.getModel().intValue()>0) {
							vo.setHasPayed(true);
						}
					}
					targetList.add(vo);
				}
			}
			ExpertArticleResponse response = new ExpertArticleResponse();
			response.setTotalPages(resultDataPage.getTotalPages());
			response.setArticleList(targetList);
			return CommonResponse.withSuccessResp(response);
		} catch (Exception e) {
			logger.info("分页查询专家文章列表接口,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	} 
	
}
