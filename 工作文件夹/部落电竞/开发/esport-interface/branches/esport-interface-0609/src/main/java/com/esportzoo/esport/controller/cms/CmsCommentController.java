package com.esportzoo.esport.controller.cms;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.cms.CmsCommentServiceClient;
import com.esportzoo.esport.connect.request.cms.PublishCommentRequest;
import com.esportzoo.esport.connect.request.cms.QueryConmentListRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.AuditStatus;
import com.esportzoo.esport.constants.cms.CmsCommentLevel;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.CmsComment;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.vo.cms.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("cmsComment")
@Api(value = "评论相关接口", tags = { "评论相关接口" })
public class CmsCommentController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CmsCommentServiceClient cmsCommentServiceClient;

	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ApiOperation(value = "发布评论接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "发布评论接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<CmsCommentVo> publishComment(PublishCommentRequest publishCommentRequest, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("发布评论接口,未获取到登录用户信息,publishCommentRequest={}", JSON.toJSONString(publishCommentRequest));
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			PublishCommentParam publishCommentParam = new PublishCommentParam();
			BeanUtils.copyProperties(publishCommentParam, publishCommentRequest);
			publishCommentParam.setUsrId(userConsumer.getId());
			ModelResult<CmsComment> modelResult = cmsCommentServiceClient.publishComment(publishCommentParam);
			if (!modelResult.isSuccess()) {
				logger.info("发布评论接口,调用接口返回错误,errorMsg={},publishCommentRequest={}", modelResult.getErrorMsg(), JSON.toJSONString(publishCommentRequest));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}

			CmsComment cmsComment = modelResult.getModel();
			if (cmsComment == null) {
				logger.info("发布评论接口,调用接口返回错误,cmsComment==null,publishCommentRequest={}", JSON.toJSONString(publishCommentRequest));
				return CommonResponse.withErrorResp("调用接口返回错误,cmsComment==null");
			}
			if (null != publishCommentRequest.getCommentRootId()) {
				cmsComment = cmsCommentServiceClient.queryCmsCommentById(publishCommentRequest.getCommentRootId()).getModel();
			}
			CmsCommentVo vo = new CmsCommentVo();
			vo.setUsrId(cmsComment.getUserId());
			vo.setUsrName(cmsComment.getUserName());
			vo.setUsrIcon(cmsComment.getUserAvatar());
			vo.setCommentId(cmsComment.getId());
			vo.setContent(cmsComment.getContent());
			vo.setCommentTimeStr(DateUtil.dateToString(cmsComment.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setFloor(cmsComment.getCommentFloor());
			vo.setUps(cmsComment.getUps());
			vo.setShares(cmsComment.getShares());
			vo.setReplies(cmsComment.getReplies());
			vo.setCurrentUserCreate(Boolean.FALSE);
			if (userConsumer.getId().equals(cmsComment.getUserId())){
				vo.setCurrentUserCreate(Boolean.TRUE);
			}
			vo.setSubCommentList(new ArrayList<SubCommentVo>());
			if (null != publishCommentRequest.getCommentRootId()) {
				List<CmsComment> subList = cmsCommentServiceClient.queryCmsCommentByParentCommentId(cmsComment.getId(), AuditStatus.AUDIT_SECC.getIndex()).getModel();
				List<SubCommentVo> subTargetList = new ArrayList<SubCommentVo>();
				if (subList != null && subList.size() > 0) {
					subList = subList.stream().sorted((e1, e2) -> e2.getCommentFloor() - e1.getCommentFloor()).collect(Collectors.toList());
					for (CmsComment subComment : subList) {
						SubCommentVo subVo = new SubCommentVo();
						subVo.setUsrId(subComment.getUserId());
						subVo.setUsrName(subComment.getUserName());
						subVo.setUsrIcon(subComment.getUserAvatar());
						subVo.setContent(subComment.getContent());
						subTargetList.add(subVo);
					}
				}
				vo.setSubCommentList(subTargetList);
			}
			return CommonResponse.withSuccessResp(vo);
		} catch (Exception e) {
			logger.info("发布评论接口,发生异常,exception={},publishCommentRequest={}", e.getMessage(), JSON.toJSONString(publishCommentRequest), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/list", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "分页查询评论列表接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "分页查询评论列表接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<CommentPageQueryResult> pageQueryCommentList(QueryConmentListRequest queryConmentListRequest, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			ModelResult<CommentPageQueryResult> modelResult=null;
			if (userConsumer == null) {
				logger.info("分页查询评论列表接口,未获取到登录用户信息,queryConmentListRequest={}", JSON.toJSONString(queryConmentListRequest));
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			CommentPageQueryParam param = new CommentPageQueryParam();
			BeanUtils.copyProperties(param, queryConmentListRequest);
			param.setUsrId(userConsumer.getId());
			if (null!=queryConmentListRequest.getCmsCommentId()){
				modelResult=cmsCommentServiceClient.querySecondaryCommentList(param);
			}else {
				modelResult = cmsCommentServiceClient.pageQueryCommentList(param);
			}
			if (!modelResult.isSuccess()) {
				logger.info("分页查询评论列表接口,调用接口返回错误,errorMsg={},queryConmentListRequest={}", modelResult.getErrorMsg(), JSON.toJSONString(queryConmentListRequest));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("分页查询评论列表接口,发生异常,exception={},queryConmentListRequest={}", e.getMessage(), JSON.toJSONString(queryConmentListRequest), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/list/nologin", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "分页查询评论列表接口无需登录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "分页查询评论列表接口无需登录", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<CommentPageQueryResult> pageQueryCommentListNoLogin(QueryConmentListRequest queryConmentListRequest, HttpServletRequest request) {
		ModelResult<CommentPageQueryResult> modelResult=null;
		try {
			logger.info("分页查询评论列表接口无需登录,接收到请求");
			CommentPageQueryParam param = new CommentPageQueryParam();
			BeanUtils.copyProperties(param, queryConmentListRequest);
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer != null) {
				param.setUsrId(userConsumer.getId());
			}
			if (null!=queryConmentListRequest.getCmsCommentId()){
				modelResult=cmsCommentServiceClient.querySecondaryCommentList(param);
			}else {
				modelResult = cmsCommentServiceClient.pageQueryCommentList(param);
			}
			if (!modelResult.isSuccess()) {
				logger.info("分页查询评论列表接口无需登录,调用接口返回错误,errorMsg={},queryConmentListRequest={}", modelResult.getErrorMsg(), JSON.toJSONString(queryConmentListRequest));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("分页查询评论列表接口无需登录,发生异常,exception={},queryConmentListRequest={}", e.getMessage(), JSON.toJSONString(queryConmentListRequest), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/upTopComment", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询点赞置顶评论接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询点赞置顶评论接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<CmsCommentVo>> pageQueryCommentList(@ApiParam(required=true,value="内容id")Long contentId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("查询点赞置顶评论接口,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			CommentUpTopQueryParam param = new CommentUpTopQueryParam();
			param.setUsrId(userConsumer.getId());
			param.setContentId(contentId);
			param.setCommentLevel(CmsCommentLevel.CMS_CONTENT.getIndex());
			param.setStatus(AuditStatus.AUDIT_SECC.getIndex());
			param.setUpsLimit(3);
			param.setLimit(5);
			ModelResult<List<CmsCommentVo>> modelResult = cmsCommentServiceClient.queryUpTopComment(param);
			if (!modelResult.isSuccess()) {
				logger.info("查询点赞置顶评论接口,调用接口返回错误,errorMsg={},param={}", modelResult.getErrorMsg(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("查询点赞置顶评论接口,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/upTopComment/nologin", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询点赞置顶评论接口无需登录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询点赞置顶评论接口无需登录", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<CmsCommentVo>> pageQueryCommentListNoLogin(@ApiParam(required=true,value="内容id")Long contentId, HttpServletRequest request) {
		try {
			logger.info("查询点赞置顶评论接口无需登录,接收到请求");
			CommentUpTopQueryParam param = new CommentUpTopQueryParam();
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer != null) {
				param.setUsrId(userConsumer.getId());
			}
			param.setContentId(contentId);
			param.setCommentLevel(CmsCommentLevel.CMS_CONTENT.getIndex());
			param.setStatus(AuditStatus.AUDIT_SECC.getIndex());
			param.setUpsLimit(3);
			param.setLimit(5);
			ModelResult<List<CmsCommentVo>> modelResult = cmsCommentServiceClient.queryUpTopComment(param);
			if (!modelResult.isSuccess()) {
				logger.info("查询点赞置顶评论接口无需登录,调用接口返回错误,errorMsg={},param={}", modelResult.getErrorMsg(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			logger.info("查询点赞置顶评论接口无需登录,响应={}", JSON.toJSONString(CommonResponse.withSuccessResp(modelResult.getModel())));
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("查询点赞置顶评论接口无需登录,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/ups", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "点赞或取消点赞接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "点赞或取消点赞接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> upsOrCancel(@ApiParam(required = true, value = "1=点赞 0=取消点赞") int type, @ApiParam(required = true, value = "评论id") Long id,  HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("点赞或取消点赞接口,未获取到登录用户信息，type={}，id={}", type, id);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			UpCommentParam upCommentParam = new UpCommentParam();
			upCommentParam.setUserId(userConsumer.getId());
			upCommentParam.setCommentId(id);
			upCommentParam.setType(type);
			ModelResult<Boolean> modelResult = cmsCommentServiceClient.upOrCancelUp(upCommentParam);
			if (!modelResult.isSuccess()) {
				logger.info("点赞或取消点赞接口，调用接口返回错误，type={}，id={}, errMsg={}", type, id, modelResult.getErrorMsg());
				return CommonResponse.withErrorResp(modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("点赞或取消点赞接口，发生异常，type={}，id={}, exception={}", type, id, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}


	@RequestMapping(value = "/delUserComment", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "用户删除自己的评论接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "用户删除自己的评论接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Integer> delUserComment(@ApiParam(required = true, value = "评论id") Long commentId,  HttpServletRequest request) {
		logger.info("用户删除自己的评论接口,请求参数，commentId={}", commentId);
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("用户删除自己的评论接口,未获取到登录用户信息，id={}", commentId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			if(commentId==null) {
				logger.info("用户删除自己的评论接口,参数为空");
				return  CommonResponse.withErrorResp("参数为空");
			}
			//先查询是不是用户发布的评论
			ModelResult<CmsComment> result = cmsCommentServiceClient.queryCmsCommentById(commentId);
			if (!result.isSuccess()&&null==result.getModel()) {
				logger.info("用户删除自己的评论接口，调用接口返回错误，commentId={}, errMsg={}",  commentId, result.getErrorMsg());
				return CommonResponse.withErrorResp(result.getErrorMsg());
			}
			if (result.getModel().getUserId()==userConsumer.getId().intValue()){
				CmsComment cmsComment = new CmsComment();
				cmsComment.setId(commentId);
				cmsComment.setStatus(AuditStatus.NO_PASS.getIndex());
				ModelResult<Integer> modelResult = cmsCommentServiceClient.updateCmsCommentById(cmsComment);
				if (!modelResult.isSuccess()&&null==modelResult.getModel()) {
					logger.info("用户删除自己的评论接口，调用接口返回错误，commentId={}, errMsg={}",  commentId, modelResult.getErrorMsg());
					return CommonResponse.withErrorResp(modelResult.getErrorMsg());
				}
				return CommonResponse.withSuccessResp(modelResult.getModel());
			}else {
				return  CommonResponse.withErrorResp("你不是此评论的发布者，无法删除");
			}
		} catch (Exception e) {
			logger.info("用户删除自己的评论接口，发生异常，={}，commentId={}, exception={}", commentId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

}
