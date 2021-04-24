package com.esportzoo.esport.controller.expert;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.expert.ExpertRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.expert.RecExpertColumnArticleResponse;
import com.esportzoo.esport.constants.ArticleFreeType;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.PayResultStatus;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.cms.expert.ExpertArticleStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.RecExpertColumnArticle;
import com.esportzoo.esport.domain.RecOrder;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.expert.ExpertManager;
import com.esportzoo.esport.util.SensitiveWordUtil;
import com.esportzoo.esport.vo.expert.RecExpertColumnArticleQueryVo;
import com.esportzoo.esport.vo.expert.RecOrderQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-09 16:02
 **/
@Controller
@RequestMapping("expert")
@Api(value = "专家信息相关接口", tags = { "专家相关接口" })
public class ExpertController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExpertManager expertManager;

	@Autowired
	private SensitiveWordUtil sensitiveWordUtil;

	@RequestMapping(value = "/expertIndexdata", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "专家首页数据接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "专家首页数据接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> legueIndexData(ExpertRequest expertRequest) {

		CommonResponse<JSONObject> commonResponse = expertManager.getIndexData(expertRequest);
		return commonResponse;
	}


	@RequestMapping(value = "/articleList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "专家文章接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "专家文章接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<RecExpertColumnArticleResponse>> getArticleList(ExpertRequest expertRequest,BaseRequest baseRequest, HttpServletRequest request) {
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		DataPage<RecExpertColumnArticle> articleDataPage = new DataPage<>();
		articleDataPage.setPageNo(expertRequest.getPageNo());
		articleDataPage.setPageSize(expertRequest.getPageSize());
		RecExpertColumnArticleQueryVo queryVo = new RecExpertColumnArticleQueryVo();
		queryVo.setStatus(ExpertArticleStatus.ENABLE.getIndex());
		if (null != baseRequest.getClientType() && baseRequest.getClientType().intValue() == ClientType.WXXCY.getIndex()) {
			/** 专家文章是否允许展示付费开关 */
			SysConfigProperty sysConfigPropertyByKey = getSysConfigByKey(SysConfigPropertyKey.EXPERT_ARTICLE_SHOW_NOTFREE_SWITCH, baseRequest.getClientType(),baseRequest.getAgentId());
			if (sysConfigPropertyByKey != null && StringUtils.isNotBlank(sysConfigPropertyByKey.getValue())) {
				String miniSwitch = sysConfigPropertyByKey.getValue();
				if ("0".equals(miniSwitch)) {// 文章只显示免费
					queryVo.setIsFree(ArticleFreeType.FREE.getIndex());
				}
			}
		}
		if (expertRequest.getMatchId()!=null){
			queryVo.setMatchId(expertRequest.getMatchId());
		}
		CommonResponse<List<RecExpertColumnArticleResponse>> commonResponse = expertManager.getArticleList(articleDataPage,queryVo,true, userConsumer);
		return commonResponse;
	}
	
	@RequestMapping(value = "/articleList/nologin", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "专家文章接口无需登录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "专家文章接口无需登录", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<RecExpertColumnArticleResponse>> getArticleListNoLogin(ExpertRequest expertRequest, HttpServletRequest request) {
		DataPage<RecExpertColumnArticle> articleDataPage = new DataPage<>();
		articleDataPage.setPageNo(expertRequest.getPageNo());
		articleDataPage.setPageSize(expertRequest.getPageSize());
		RecExpertColumnArticleQueryVo queryVo = new RecExpertColumnArticleQueryVo();
		queryVo.setStatus(ExpertArticleStatus.ENABLE.getIndex());
		if (expertRequest.getMatchId()!=null){
			queryVo.setMatchId(expertRequest.getMatchId());
		}
		UserConsumer userConsumer = getLoginUsr(request);
		CommonResponse<List<RecExpertColumnArticleResponse>> commonResponse = null;
		if (userConsumer == null) {
			commonResponse = expertManager.getArticleList(articleDataPage,queryVo,true, null);
		} else {
			commonResponse = expertManager.getArticleList(articleDataPage,queryVo,true, userConsumer);
		}
		return commonResponse;
	}

	@RequestMapping(value = "/paid/articleList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查看用户支付文章列表接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查看用户支付文章列表接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<RecExpertColumnArticleResponse>> getArticleListByPaidAndUser(HttpServletRequest request, ExpertRequest expertRequest) {

		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer.getId() == null) {
			logger.error("查询用户已支付文章列表，用户ID为空");
			return CommonResponse.withErrorResp("用户ID为空");
		}
		DataPage<RecOrder> recOrderDataPage = new DataPage<>();
		RecOrderQueryVo queryVo = new RecOrderQueryVo();
		recOrderDataPage.setPageNo(expertRequest.getPageNo());
		recOrderDataPage.setPageSize(expertRequest.getPageSize());
		queryVo.setUserId(userConsumer.getId());
		queryVo.setPayStatus(PayResultStatus.PAY_SUCCESS.getIndex());
		CommonResponse<List<RecExpertColumnArticleResponse>> commonResponse = expertManager
				.getArticleListByPaidAndUser(recOrderDataPage, queryVo, true, userConsumer);
		return commonResponse;
	}


	@RequestMapping(value = "/apply", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "专家申请接口", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@ApiResponse(code = 200, message = "专家申请接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> apply(HttpServletRequest request, @RequestParam("file") MultipartFile[] files,ExpertRequest expertRequest) {
		if (files==null || files.length == 0){
			logger.error("手持身份证照片为空,用户={}",expertRequest.getUserId());
			return CommonResponse.withErrorResp("图片上传失败");
		}
		if (checkParam(expertRequest)) {
			CommonResponse<Boolean> response = expertManager.apply(expertRequest, files);
			return response;
		}
		return CommonResponse.withErrorResp("申请失败");



	}

	/**
	 * @Description: 专家申请校验过滤参数
	 * @param expertRequest
	 * @Return boolean
	 */
	private boolean checkParam(ExpertRequest expertRequest){
		if (StringUtils.isBlank(expertRequest.getExpertName())){
			logger.error("专家称号不能为空");
			return false;
		}
		expertRequest.setExpertName( sensitiveWordUtil.replaceSensitiveWord(expertRequest.getExpertName(),'*'));

		if (StringUtils.isBlank(expertRequest.getCardNo())){
			logger.error("身份证号不能为空");
			return false;
		}

		if (StringUtils.isBlank(expertRequest.getRealName())){
			logger.error("真实名字不能为空");
			return false;
		}
		expertRequest.setRealName(sensitiveWordUtil.replaceSensitiveWord(expertRequest.getRealName(),'*'));

		if (StringUtils.isBlank(expertRequest.getReason())){
			logger.error("申请理由不能为空");
			return false;
		}
		expertRequest.setReason(sensitiveWordUtil.replaceSensitiveWord(expertRequest.getReason(),'*'));
		if (expertRequest.getUserId()==null){
			logger.error("用户id为空");
			return false;
		}

		return true;
	}


}
