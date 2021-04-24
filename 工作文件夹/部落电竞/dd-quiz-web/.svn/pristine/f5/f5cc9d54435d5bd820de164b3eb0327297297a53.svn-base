package com.esportzoo.esport.manager.expert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.common.ClientAdPicServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertApplyServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertColumnArticleServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertServiceClient;
import com.esportzoo.esport.client.service.expert.RecOrderServiceClient;
import com.esportzoo.esport.connect.request.expert.ExpertRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.expert.RecExpertColumnArticleResponse;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.cms.expert.ExpertArticleStatus;
import com.esportzoo.esport.domain.*;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.util.Application;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import com.esportzoo.esport.vo.expert.RecExpertApplyQueryVo;
import com.esportzoo.esport.vo.expert.RecExpertColumnArticleQueryVo;
import com.esportzoo.esport.vo.expert.RecExpertQueryVo;
import com.esportzoo.esport.vo.expert.RecOrderQueryVo;
import com.esportzoo.leaguelib.client.service.prematch.MatchServiceClient;
import com.esportzoo.leaguelib.common.constants.VideoGame;
import com.esportzoo.leaguelib.common.domain.Match;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-09 16:04
 **/

@Component
public class ExpertManager {

	private transient static final Logger logger = LoggerFactory.getLogger(ExpertManager.class);


	@Autowired
	@Qualifier("clientAdPicServiceClient")
	private ClientAdPicServiceClient clientAdPicServiceClient;

	@Autowired
	@Qualifier("recExpertServiceClient")
	private RecExpertServiceClient recExpertServiceClient;

	@Autowired
	@Qualifier("recExpertColumnArticleServiceClient")
	private RecExpertColumnArticleServiceClient recExpertColumnArticleServiceClient;

	@Autowired
	@Qualifier("matchServiceClient")
	MatchServiceClient matchServiceClient;

	@Autowired
	@Qualifier("userConsumerServiceClient")
	UserConsumerServiceClient userConsumerServiceClient;

	@Autowired
	RecExpertApplyServiceClient recExpertApplyServiceClient;
	@Value("${expert.upload.path}")
	private String uploadPath;

	@Autowired
	@Qualifier("recOrderServiceClient")
	private RecOrderServiceClient recOrderServiceClient;

	@Value("${expert.res.host}")
	private String resPath;

	private static List<VideoGame> videoGames = VideoGame.getAllList();

	@Autowired
	Application  application;
	
	@Autowired
	private RedisClient redisClient;

	@Autowired
	private CachedManager cachedManager;

	public static final String CACHE_KEY_APPLY_NO_REPEATED_SUBMIT = "cache_key_apply_no_repeated_submit";


	/**
	 * @Description: 加载专家首页数据
	 * @param
	 * @Return CommonResponse<JSONObject>
	 */
	public CommonResponse<JSONObject> getIndexData(ExpertRequest expertRequest) {

		JSONObject jsonObject = new JSONObject();
		try {

			/** 加载首页轮播广告位 */
			ModelResult<List<ClientAdPic>> listAdResult = clientAdPicServiceClient.queryListByAdType(AdPicPlaceType.EXPERT_BANNER.getIndex());
			if (expertRequest.getClientType()==null){
				expertRequest.setClientType(ClientType.WXXCY.getIndex());
			}

			List<ClientAdPic> clientAdPicList = listAdResult.getModel();
			clientAdPicList= clientAdPicList.stream().filter(x -> x.getClientType().equals(expertRequest.getClientType()))
					.collect(Collectors.toList());
			jsonObject.put("adList", clientAdPicList);

			/** 加载热门推荐专家列表 */
			RecExpertQueryVo queryVo = new RecExpertQueryVo();
			DataPage<RecExpert> dataPage = new DataPage<>();
			dataPage.setPageSize(expertRequest.getPageSize());
			dataPage.setPageNo(expertRequest.getPageNo());
			queryVo.setNeedAttach(true);
			queryVo.setStatus(RecExpertStatus.EFFECTIVE.getIndex());
			queryVo.setIsRecommend(RecommendStatus.POPULAR.getIndex());
			PageResult<RecExpert> pageResult = recExpertServiceClient.queryPage(queryVo, dataPage);
			if (pageResult.isSuccess()) {
				List<RecExpert> recExpertList = pageResult.getPage().getDataList();
				/*排序*/
				if (CollectionUtil.isNotEmpty(recExpertList)){
					recExpertList = recExpertList.stream().sorted(Comparator.comparing(RecExpert::getOrderNumber)).collect(Collectors.toList());
					recExpertList.stream().forEach( recExpert -> {
						if (recExpert.getNickName().matches("^1[3|4|5|7|8][0-9]\\d{4,8}$")) {
							recExpert.setNickName(recExpert.getNickName().substring(0, 7) + "XXXX");
						}
					});
				}
				jsonObject.put("recExpertList", recExpertList);
			}else {
				logger.error("查询热门专家列表出错，错误信息：{}",pageResult.getErrorMsg());
				return CommonResponse.withErrorResp("查询热门专家列表出错");
			}
		} catch (Exception e) {
			logger.error("获取专家首页数据出现异常，异常信息：{}", e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("专家首页数据出现异常");
		}
		jsonObject.put("gameList", videoGames);
		return CommonResponse.withSuccessResp(jsonObject);
	}

	/**
	 * @Description: 分页查询专家推荐文章
	 * @param articleDataPage
	 * @param isAttachExpert	是否关联专家信息
	 * @Return
	 */
	public CommonResponse<List<RecExpertColumnArticleResponse>> getArticleList(DataPage<RecExpertColumnArticle> articleDataPage,
			RecExpertColumnArticleQueryVo articleQueryVo, boolean isAttachExpert, UserConsumer loginUser) {

		/** 加载推荐文章列表 */
		PageResult<RecExpertColumnArticle> articlePageResult = new PageResult<>();
		try {
			articlePageResult = recExpertColumnArticleServiceClient.queryPage(articleQueryVo, articleDataPage);
			if (articlePageResult.isSuccess()) {
				List<RecExpertColumnArticle> columnArticles = articlePageResult.getPage().getDataList();
				if (columnArticles!=null) {
					logger.info("查询文章列表，pageNo={},pageSize={},articleQueryVo={},返回的结果集大小={}", 
							articleDataPage.getPageNo(), articleDataPage.getPageSize(), JSON.toJSONString(articleQueryVo), columnArticles.size());
				}
				List<RecExpertColumnArticleResponse> responseList = convertData(columnArticles, isAttachExpert, loginUser, 1);
				if (responseList!=null) {
					logger.info("查询文章列表，转换后结果集大小={}", responseList.size());
				}
				return CommonResponse.withSuccessResp(responseList);
			}
		} catch (Exception e) {
			logger.error("查询专家文章列表出现异常，异常信息：{}", e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询专家文章异常");
		}
		return CommonResponse.withErrorResp(articlePageResult.getErrorMsg());
	}

	/**
	 * @Description: 分页查询用户已支付文章列表
	 * @param articleDataPage
	 * @param isAttachExpert	是否关联专家信息
	 * @Return
	 */
	public CommonResponse<List<RecExpertColumnArticleResponse>> getArticleListByPaidAndUser(DataPage<RecOrder> dataPage ,
			RecOrderQueryVo recOrderQueryVo, boolean isAttachExpert, UserConsumer loginUser) {

		/** 加载支付文章列表 */
		List<RecExpertColumnArticleResponse> responseList = Lists.newArrayList();
		try {
			/*查询对应的支付订单*/
			PageResult<RecOrder> pageResult = recOrderServiceClient.queryPage(recOrderQueryVo, dataPage);
			if (pageResult.isSuccess()) {
				List<RecOrder> recOrders = pageResult.getPage().getDataList();
				if (recOrders.size() > 0) {
					List<Long> articelIdList = recOrders.stream().map(RecOrder::getColumnArticleId).collect(Collectors.toList());
					/*去重*/
					 articelIdList = CollUtil.distinct(articelIdList);
					ModelResult<List<RecExpertColumnArticle>> listModelResult = recExpertColumnArticleServiceClient.selectByIdList(articelIdList);
					if (!listModelResult.isSuccess()){
						logger.error("查询用户已支付文章出错，出错信息：{}",listModelResult.getErrorMsg());
					}else if ( listModelResult.getModel().size()>0) {
						responseList = convertData(listModelResult.getModel(), isAttachExpert, loginUser, 2);
						return CommonResponse.withSuccessResp(responseList);
					}
					logger.info("该用户【{}】，根据已支付文章ID列表【{}】，没有查询到对应的文章",recOrderQueryVo.getUserId(),JSONObject.toJSONString(articelIdList));
				}
				logger.info("该用户【{}】，无已支付文章可查看",recOrderQueryVo.getUserId());
			}

		} catch (Exception e) {
			logger.error("查询专家文章列表出现异常，异常信息：{}", e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询专家文章异常");
		}
		return CommonResponse.withSuccessResp(responseList);
	}


	private List<RecExpertColumnArticleResponse> convertData(List<RecExpertColumnArticle> columnArticles, boolean isAttachExpert, 
			UserConsumer loginUser, int placeFlag) throws Exception {
		if (columnArticles != null) {
			List<RecExpertColumnArticleResponse> list = Lists.newArrayList();
			for (RecExpertColumnArticle article : columnArticles) {
				RecExpertColumnArticleResponse articleResponse = new RecExpertColumnArticleResponse();
				if ( article.getStatus().intValue() == ExpertArticleStatus.DELETE.getIndex() 
					|| article.getStatus().intValue() == ExpertArticleStatus.FORBID.getIndex()) {
					/*过滤禁用或者删除的文章*/
					continue;
				}
				BeanUtils.copyProperties(article, articleResponse);
				articleResponse.setId(article.getId());
				/** 获取关联的赛事 */
				String str = article.getMatchIdList();
				if (StringUtils.isNotBlank(str)) {
					String[] ids = str.split(",");
					if (ids.length > 0) {
						List<Long> idList = Lists.newArrayList();
						for (String matchId : ids) {
							if (StringUtils.isNotBlank(matchId)) {
								idList.add(Long.valueOf(matchId.trim()));
							}
						}
						ModelResult<List<Match>> listModelResult = matchServiceClient.queryMatchListByMatchIds(idList);
						List<Match> matchList = listModelResult.getModel();
						if (listModelResult.isSuccess() && matchList != null) {
							for (Match match : matchList) {
								match.setHomeTeamLogo(application.getMatchHomeTeamLogo(match));
								match.setAwayTeamLogo(application.getMatchAwayTeamLogo(match));
							}
							articleResponse.setMatchResultList(matchList);
						}
					}
				}

				/** 获取专家信息 */
				if (isAttachExpert) {
					UserConsumerQueryOption queryOption = new UserConsumerQueryOption();
					ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient.queryConsumerById(article.getUserId(), queryOption);
					if (!userConsumerModelResult.isSuccess() || null == userConsumerModelResult.getModel()) {
						logger.error("没有查询到相关的专家信息，用户ID：{}", article.getUserId());
						continue;
					}
					UserConsumer userConsumer = userConsumerModelResult.getModel();
					articleResponse.setAuthorAvatar(userConsumer.getIcon());
					ModelResult<RecExpert> result = recExpertServiceClient.queryByUserId(userConsumer.getId());
					if (result.isSuccess() && result.getModel()!=null){
						articleResponse.setAuthorName(result.getModel().getNickName());
					}
				}
				if (article.getIsFree()== ArticleFreeType.FREE.getIndex()){
					articleResponse.setViews(cachedManager.getCacheSetSize(CacheType.EPERT_ARTICEL.getIndex(),article.getId()+"").intValue());
				}else {
					articleResponse.setViews(article.getViews() == null ? 0 : article.getViews());
				}
				articleResponse.setPublishTime(DateUtil.getShortTime(DateUtil.getDateString2(article.getCreateTime().getTime())));
				
				/**当前登录用户是否已经支付了该文章*/
				if (loginUser != null) {
					RecOrderQueryVo recOrderQueryVo = new RecOrderQueryVo();
					recOrderQueryVo.setUserId(loginUser.getId());
					recOrderQueryVo.setColumnArticleId(article.getId());
					recOrderQueryVo.setPayStatus(ArticlePayStatus.PAY_SUCCESS.getIndex());
					ModelResult<Integer> mr = recOrderServiceClient.queryCount(recOrderQueryVo);
					if (mr.isSuccess() && mr.getModel()!=null && mr.getModel().intValue()>0) {
						articleResponse.setHasPayed(true);
					}
				}
				
				articleResponse.setPlaceFlag(placeFlag);
				
				list.add(articleResponse);
			}
			return list;
		}
		return null;

	}

	/**
	 * @Description: 专家申请，上传图片接口
	 * @param expertRequest
	 * @param files
	 * @Return CommonResponse<Boolean>
	 */
	public CommonResponse<Boolean> apply( ExpertRequest expertRequest, MultipartFile[] files){

		//防止重复提交
		if (redisClient.getObj(CACHE_KEY_APPLY_NO_REPEATED_SUBMIT + expertRequest.getUserId()) != null) {
			return CommonResponse.withErrorResp("勿要重复点击");
		}
		redisClient.setObj(CACHE_KEY_APPLY_NO_REPEATED_SUBMIT + expertRequest.getUserId(), 5, 1);
		RecExpertApplyQueryVo recExpertApplyQueryVo = new RecExpertApplyQueryVo();
		recExpertApplyQueryVo.setUserId(expertRequest.getUserId());
		ModelResult<List<RecExpertApply>> listModelResult = recExpertApplyServiceClient.queryList(recExpertApplyQueryVo);
		if (listModelResult.isSuccess() && listModelResult.getModel() != null && listModelResult.getModel().size() > 0) {
			RecExpertApply apply = listModelResult.getModel().get(0);
			if (apply.getStatus() == AuditStatus.AUDITING.getIndex()) {
				return CommonResponse.withErrorResp("您已提交申请,请等待审核");
			} else if (apply.getStatus() == AuditStatus.NO_PASS.getIndex()) {
				return CommonResponse.withErrorResp("您已审核不通过,请勿再提交");
			} else {
				return CommonResponse.withErrorResp("审核已通过,不要重复提交");
			}
		}

		/*校验专家名称,不可重复*/
		ModelResult<RecExpert> nicNameResult = recExpertServiceClient.queryByNickNameAndPassword(expertRequest.getExpertName().trim(), null);
		if (nicNameResult.isSuccess() && nicNameResult.getModel()!=null && StringUtils.isNotEmpty(nicNameResult.getModel().getNickName())){
			return CommonResponse.withErrorResp("专家号名称已存在,请重新输入");
		}

		if (files != null && files.length > 0) {
			try {
				for (int i = 0; i < files.length; i++) {

					String fileName = files[i].getOriginalFilename();
					if (StringUtils.isNotBlank(fileName)) {
						//创建输出文件对象
						String suffix = fileName.substring(fileName.length() - 4, fileName.length());
						File outFile = FileUtils.getFile(uploadPath + File.separator + UUID.randomUUID().toString().replaceAll("-", "") + suffix);
						//拷贝文件到输出文件对象
						FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
						expertRequest.setFrontImage(outFile.getPath().replace(uploadPath, resPath));
					}
				}
				UserConsumerQueryOption queryOption = new UserConsumerQueryOption();
				ModelResult<UserConsumer> modelResult = userConsumerServiceClient.queryConsumerById(expertRequest.getUserId(), queryOption);
				UserConsumer userConsumer = modelResult.getModel();
				if (!modelResult.isSuccess() || userConsumer == null) {
					logger.error("专家申请失败，获取不到用户信息，用户ID={}", expertRequest.getUserId());
					return CommonResponse.withErrorResp("申请专家失败");
				}


				RecExpertApply expertApply = new RecExpertApply();
				expertApply.setUserId(expertRequest.getUserId());
				expertApply.setNickName(expertRequest.getExpertName().trim());
				//暂时设置用户icon
				if (expertRequest.getExpertIcon()==null){
					expertApply.setExpertIcon(userConsumer.getIcon());
				}
				expertApply.setExpertDesc(expertRequest.getIntroduction());
				expertApply.setApplyReason(expertRequest.getReason());
				expertApply.setOperator(expertRequest.getRealName());
				expertApply.setIdcard(expertRequest.getCardNo());
				//图片为手持正面照
				expertApply.setIdcardFrontImage(expertRequest.getFrontImage());
				/*暂无反面图片*/
				expertApply.setIdcardBackImage("");
				expertApply.setStatus(AuditStatus.AUDITING.getIndex());
				expertApply.setCreateTime(Calendar.getInstance());
				ModelResult<Long> result = recExpertApplyServiceClient.save(expertApply);
				if (result.isSuccess() && result.getModel() > 0) {
					logger.info("用户「{}」提交专家申请成功，等待审核",userConsumer.getId());
					return CommonResponse.withSuccessResp(true);
				} else {
					logger.error("专家申请失败，失败信息：{}", result.getErrorMsg());
				}

			} catch (Exception e) {
				logger.error("申请专家异常，异常信息：{}", e);
				e.printStackTrace();
			}
		}
		return CommonResponse.withErrorResp("申请专家失败");
	}


}
