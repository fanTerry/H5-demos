package com.esportzoo.esport.controller.cms;

import cn.hutool.core.bean.BeanUtil;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.CmsContentRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.CacheType;
import com.esportzoo.esport.constants.CmsTypeDefineConstant;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.CmsContent;
import com.esportzoo.esport.domain.NewsListVo;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.manager.CmsContentManager;
import com.esportzoo.esport.vo.cms.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("cmsContent")
@Api(value = "内容相关接口", tags = {"内容相关接口"})
public class CmsContentController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CmsContentServiceClient cmsContentServiceClient;

	@Value("${video.domain}")
	private String videoUrlDomain;

	@Autowired
	private CachedManager cachedManager;

	@Autowired
	CmsContentManager cmsContentManager;

	@ApiOperation(value = "获取内容详情接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/detail/{cmsContentId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<CmsContentDetailVo> getCmsContentDetailById(
			@ApiParam(required = true, name = "内容id") @PathVariable("cmsContentId") Long cmsContentId, BaseRequest baseRequest,
			HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据id获取cmsContent详情,未获取到登录用户信息，cmsContentId={}", cmsContentId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			CmsContentDetailParam param = new CmsContentDetailParam();
			param.setUsrId(userConsumer.getId());
			param.setContentId(cmsContentId);
			param.setAgentId(baseRequest.getAgentId());
//			param.setBiz(baseRequest.getBiz())https://m.esportzoo.com/homeDetail/32364?agentId=100104&biz=2&loginflag=1
			param.setClientType(baseRequest.getClientType());
			ModelResult<CmsContentDetailVo> modelResult = cmsContentServiceClient.getCmsContentDetail(param);
			if (modelResult == null) {
				logger.info("根据id获取cmsContent详情，调用接口错误modelResult==null，cmsContentId={}", cmsContentId);
				return CommonResponse.withErrorResp("调用接口错误modelResult==null");
			}
			if (!modelResult.isSuccess()) {
				logger.info("根据id获取cmsContent详情，调用接口错误errorMsg={}，cmsContentId={}", modelResult.getErrorMsg(), cmsContentId);
				return CommonResponse.withErrorResp("调用接口错误:" + modelResult.getErrorMsg());
			}
			CmsContentDetailVo cmsContentDetailVo = modelResult.getModel();
			if (cmsContentDetailVo == null) {
				logger.info("根据id获取cmsContent详情，调用接口错误cmsContentDetailVo==null，cmsContentId={}", cmsContentId);
				return CommonResponse.withErrorResp("调用接口错误cmsContentDetailVo==null");
			}
			filterVideoUrl(cmsContentDetailVo);


			cachedManager.cacheSet(CacheType.CMS_CONTENT.getIndex(), cmsContentDetailVo.getId() + "", userConsumer.getId() + "");
			Long viewNum = cachedManager.getCacheSetSize(CacheType.CMS_CONTENT.getIndex(), cmsContentDetailVo.getId() + "");
			int idNum = cmsContentDetailVo.getId().intValue() % 100;
			cmsContentDetailVo.setViews(cmsContentDetailVo.getComments() * 2 + viewNum.intValue() + idNum);
//			cmsContentDetailVo.setViews(cmsContentDetailVo.getComments() * 2 +cmsContentDetailVo.getViews() + idNum);
			return CommonResponse.withSuccessResp(cmsContentDetailVo);
		} catch (Exception e) {
			logger.info("根据id获取cmsContent详情，发生异常，cmsContentId={}，exception={}", cmsContentId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@ApiOperation(value = "获取内容详情接口可不登录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/detail/nologin/{cmsContentId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<CmsContentDetailVo> getCmsContentDetailNoLoginById(
			@ApiParam(required = true, name = "内容id") @PathVariable("cmsContentId") Long cmsContentId, HttpServletRequest request,
			BaseRequest baseRequest) {
		try {
			logger.info("获取内容详情接口可不登录,cmsContentId={}", cmsContentId);
			CmsContentDetailParam param = new CmsContentDetailParam();
			param.setContentId(cmsContentId);
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer != null) {
				param.setUsrId(userConsumer.getId());
			}
			param.setAgentId(baseRequest.getAgentId());
//			param.setBiz(baseRequest.getBiz());
			param.setClientType(baseRequest.getClientType());
			ModelResult<CmsContentDetailVo> modelResult = cmsContentServiceClient.getCmsContentDetail(param);
			if (modelResult == null) {
				logger.info("根据id获取cmsContent详情,无需登录，调用接口错误modelResult==null，cmsContentId={}", cmsContentId);
				return CommonResponse.withErrorResp("调用接口错误modelResult==null");
			}
			if (!modelResult.isSuccess()) {
				logger.info("根据id获取cmsContent详情,无需登录，调用接口错误errorMsg={}，cmsContentId={}", modelResult.getErrorMsg(), cmsContentId);
				return CommonResponse.withErrorResp("调用接口错误:" + modelResult.getErrorMsg());
			}
			CmsContentDetailVo cmsContentDetailVo = modelResult.getModel();
			if (cmsContentDetailVo == null) {
				logger.info("根据id获取cmsContent详情,无需登录，调用接口错误cmsContentDetailVo==null，cmsContentId={}", cmsContentId);
				return CommonResponse.withErrorResp("调用接口错误cmsContentDetailVo==null");
			}
			filterVideoUrl(cmsContentDetailVo);

			if (userConsumer != null) {
				cachedManager.cacheSet(CacheType.CMS_CONTENT.getIndex(), cmsContentDetailVo.getId() + "", userConsumer.getId() + "");
			}
			Long viewNum = cachedManager.getCacheSetSize(CacheType.CMS_CONTENT.getIndex(), cmsContentDetailVo.getId() + "");
			int idNum = cmsContentDetailVo.getId().intValue() % 100;
			cmsContentDetailVo.setViews(cmsContentDetailVo.getComments() * 2 + viewNum.intValue() + idNum);
			//			cmsContentDetailVo.setViews(cmsContentDetailVo.getComments() * 2 +cmsContentDetailVo.getViews() + idNum);
			return CommonResponse.withSuccessResp(cmsContentDetailVo);
		} catch (Exception e) {
			logger.info("根据id获取cmsContent详情,无需登录，发生异常，cmsContentId={}，exception={}", cmsContentId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}


	@RequestMapping(value = "/ups", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "点赞或取消点赞接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "点赞或取消点赞接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> upsOrCancel(@ApiParam(required = true, value = "1=点赞 0=取消点赞") int type,
			@ApiParam(required = true, value = "内容id") Long id, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("点赞或取消点赞接口,未获取到登录用户信息，type={}，id={}", type, id);
				return CommonResponse.newCommonResponse("5555", "未获取到登录用户信息");
			}
			UpContenParam upContenParam = new UpContenParam();
			upContenParam.setUserId(userConsumer.getId());
			upContenParam.setContentId(id);
			upContenParam.setType(type);
			ModelResult<Boolean> modelResult = cmsContentServiceClient.upOrCancelUp(upContenParam);
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


	@RequestMapping(value = "/favorites", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "收藏或取消收藏接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "收藏或取消收藏接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> favoritesOrCancel(@ApiParam(required = true, value = "1=收藏 0=取消收藏") int status,
			@ApiParam(required = true, value = "内容id") Long id, @ApiParam(required = false, value = "收藏夹id,可以为空") Long typeId,
			HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("收藏或取消收藏接口,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			UserFavoritesParam param = new UserFavoritesParam();
			param.setUserId(userConsumer.getId());
			param.setStatus(status);
			param.setContentId(id);
			param.setTypeId(typeId);
			ModelResult<Boolean> modelResult = cmsContentServiceClient.favorites(param);
			if (!modelResult.isSuccess()) {
				logger.info("收藏或取消收藏接口，调用接口返回错误，status={}，id={}，typeId={}，errMsg={}", status, id, typeId, modelResult.getErrorMsg());
				return CommonResponse.withErrorResp(modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("收藏或取消收藏接口，发生异常，status={}，id={}，typeId={}， exception={}", status, id, typeId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/addViews", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "增加文章阅读次数接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "增加文章阅读次数接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Long> addViews(@ApiParam(required = true, value = "内容id") Long id, HttpServletRequest request) {
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			logger.info("增加文章阅读次数接口,未获取到登录用户信息");
			return CommonResponse.withErrorResp("未获取到登录用户信息");
		}
		try {
			ModelResult<CmsContent> result = cmsContentServiceClient.queryCmsContentById(id);
			if (!result.isSuccess()) {
				logger.error("更新文章阅读次数,根据ID查询文章出错，文章ID={}", id);
				return CommonResponse.withErrorResp(result.getErrorMsg());
			}
			CmsContent cmsContent = result.getModel();
//			cachedManager.cacheSet(CacheType.CMS_CONTENT.getIndex(), cmsContent.getId() + "", userConsumer.getId() + "");
			ModelResult<Boolean> modelResult = cmsContentServiceClient.addViews(userConsumer.getId(),cmsContent);
			if (!modelResult.isSuccess() || modelResult.getModel()==null){
				return CommonResponse.withErrorResp("更新文章阅读次数失败");
			}
//			return CommonResponse.withSuccessResp(cachedManager.getCacheSetSize(CacheType.CMS_CONTENT.getIndex(), cmsContent.getId() + ""));
			return CommonResponse.withSuccessResp(cmsContent.getViews().longValue()+1);
		} catch (Exception e) {
			logger.error("更新文章阅读次数，发生异常，文章ID={}， exception={}", id, e.getMessage());
			return CommonResponse.withErrorResp("更新文章阅读次数发生异常");
		}
	}

	private void filterVideoUrl(CmsContentDetailVo cmsContentDetailVo){
		if (cmsContentDetailVo.getTypeId() == CmsTypeDefineConstant.ADMIN_VIDEO.getIndex()
				|| cmsContentDetailVo.getTypeId() == CmsTypeDefineConstant.USER_VIDEO.getIndex()
				|| cmsContentDetailVo.getTypeId() == CmsTypeDefineConstant.GRAB_VIDEO.getIndex()) {
			if (StringUtils.isNotEmpty(cmsContentDetailVo.getContent())) {
				if (cmsContentDetailVo.getContent().indexOf("esportzoo.com") == -1) {
					cmsContentDetailVo.setContent(videoUrlDomain + cmsContentDetailVo.getContent());
				}
			}
		}

	}


	@RequestMapping(value = "/getCmsListByUserUp", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询用户点赞文章", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询用户点赞文章", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<NewsListVo>> getCmsListByUserUp(CmsContentRequest cmsContentRequest, HttpServletRequest request) {
		DataPage<NewsListVo> newsListVoDataPage = new DataPage<>();
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("查询用户点赞文章,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			DataPage<CmsContent> page = new DataPage<>();
			page.setPageNo(cmsContentRequest.getPageNo());
			page.setPageSize(cmsContentRequest.getPageSize());
			CmsContentQueryVo queryVo = new CmsContentQueryVo();
			BeanUtil.copyProperties(cmsContentRequest,queryVo);
			queryVo.setUserId(userConsumer.getId());
			PageResult<CmsContent> pageResult = cmsContentServiceClient.queryCmsContentPageByUserUp(queryVo, page);
			if (!pageResult.isSuccess()) {
				logger.info("查询用户点赞文章,查询失败，失败信息：{}", pageResult.getErrorMsg());
				return CommonResponse.withErrorResp(pageResult.getErrorMsg());
			}
			DataPage<CmsContent> cmsContentDataPage = pageResult.getPage();
			if (cmsContentDataPage.getDataList() != null ) {
				List<CmsContent> cmsContentList = cmsContentDataPage.getDataList();
				List<NewsListVo> newsListVos = cmsContentManager.convertCmsToNews(cmsContentList);
				cmsContentManager.setFollowedAndUserUp(userConsumer.getId(), newsListVos);
				newsListVoDataPage.setPageSize(cmsContentDataPage.getPageSize());
				newsListVoDataPage.setPageNo(cmsContentDataPage.getPageNo());
				newsListVoDataPage.setTotalCount(cmsContentDataPage.getTotalCount());
				newsListVoDataPage.setDataList(newsListVos);
				return CommonResponse.withSuccessResp(newsListVoDataPage);
			}
		} catch (Exception e) {
			logger.info("查询用户点赞文章出现异常：异常信息：【{}】", e.getMessage());
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询用户点赞文章出现异常");
		}
		return CommonResponse.withErrorResp("查询用户点赞文章失败");
	}


}
