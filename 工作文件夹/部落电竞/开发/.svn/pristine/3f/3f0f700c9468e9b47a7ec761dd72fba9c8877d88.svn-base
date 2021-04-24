package com.esportzoo.esport.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.annotation.EsportMethodLog;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.cms.CmsCommentServiceClient;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.client.service.cms.UserFollowServiceClient;
import com.esportzoo.esport.client.service.cms.UserUpServiceClient;
import com.esportzoo.esport.client.service.common.CmsTypeDefineServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.connect.request.CmsContentRequest;
import com.esportzoo.esport.connect.request.FollowRequest;
import com.esportzoo.esport.connect.response.CmsMsgResponse;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.cms.*;
import com.esportzoo.esport.domain.*;
import com.esportzoo.esport.option.CmsContentParam;
import com.esportzoo.esport.option.QueryCmsContentParam;
import com.esportzoo.esport.service.cms.CmsContentTopicService;
import com.esportzoo.esport.service.consumer.UserConsumerService;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import com.esportzoo.esport.vo.cms.CmsContentQueryVo;
import com.esportzoo.esport.vo.cms.FollowedUserVo;
import com.esportzoo.esport.vo.cms.UserFollowQueryVo;
import com.esportzoo.esport.vo.cms.UserUpQueryVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CmsContentManager {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	@Qualifier("cmsContentServiceClient")
	CmsContentServiceClient cmsContentServiceClient;

	@Autowired
	@Qualifier("cmsTypeDefineServiceClient")
	CmsTypeDefineServiceClient cmsTypeDefineServiceClient;

	@Autowired
	private CachedManager cachedManager;

	@Autowired
	@Qualifier("userFollowServiceClient")
	private UserFollowServiceClient userFollowServiceClient;


	@Value("${video.domain}")
	private String videoUrlDomain;

	@Value("${expert.res.domain}")
	private String imageDomain;

	@Autowired
	private UserConsumerService userConsumerService;

	public static final String logPrefix = "查询资讯列表：";

	public static final String FOLLWERS_USERID_KEY = "follwer_userList:{userId}_key";

	@Autowired
	private CmsContentTopicService cmsContentTopicService;

	@Autowired
	RedisClient redisClient;

	@Autowired
	@Qualifier("userConsumerServiceClient")
	UserConsumerServiceClient userConsumerServiceClient;

	@Autowired
	@Qualifier("userUpServiceClient")
	UserUpServiceClient userUpServiceClient;

	@Autowired
	private CommonManager commonManager;

	@Autowired
	@Qualifier("cmsCommentServiceClient")
	CmsCommentServiceClient cmsCommentServiceClient;


	@EsportMethodLog(paramLog = false)
	public List<NewsListVo> getCmsContentList(CmsContentRequest cmsContentRequest,Long userId) {

		CmsContentParam condition = new CmsContentParam();
		DataPage<CmsContent> page = new DataPage<>();
		List<NewsListVo> newsListVoList = Lists.newArrayList();
		try {
			TimeInterval timer = cn.hutool.core.date.DateUtil.timer();
			condition.setStatus(CmsContentStatus.ISSUE.getIndex());
			SysConfigProperty sysConfigProperty = commonManager
					.getSysConfigByKey(SysConfigPropertyKey.VIDEO_SHOW_SWITCH, cmsContentRequest.getClientType(), cmsContentRequest.getAgentId());
			String value = sysConfigProperty.getValue();
			if (StringUtils.isNotBlank(value) && value.trim().equals("0")) {//视频是关闭的
				/*只查长文章*/
				condition.setTypeList(CmsTypeDefineConstant.getLongArticleIndexList());
			}
			CmsChannel channel = CmsChannel.valueOf(cmsContentRequest.getContentType());
//			if (channel == null) {
//				logger.error("无法匹配栏目类型，ContentType={}", cmsContentRequest.getContentType());
//				return newsListVoList;
//			}
			List<Integer> typeList = Lists.newArrayList();
			if(channel == null){
				/** 查询资讯 */
				typeList.add(CmsTypeDefineConstant.GRAB_ARTICLE_LONG.getIndex());
				typeList.add(CmsTypeDefineConstant.ADMIN_ARTICLE_LONG.getIndex());
				condition.setTypeList(typeList);
			} else if  (channel.getIndex() == CmsChannel.Recommend.getIndex()) {
				/** 如果是查询推荐，则直接取推荐标记位查询 */
				condition.setIsRecommend((short) CmsRecommend.POPULAR.getIndex());
				typeList.add(CmsTypeDefineConstant.GRAB_VIDEO.getIndex());
				typeList.add(CmsTypeDefineConstant.GRAB_ARTICLE_LONG.getIndex());
				typeList.add(CmsTypeDefineConstant.ADMIN_ARTICLE_LONG.getIndex());
				typeList.add(CmsTypeDefineConstant.ADMIN_VIDEO.getIndex());
				condition.setTypeList(typeList);
			} else {
				/** 如果是查询视频栏目，则直接取视频类型 */
				typeList.add(CmsTypeDefineConstant.GRAB_VIDEO.getIndex());
				typeList.add(CmsTypeDefineConstant.ADMIN_VIDEO.getIndex());
				condition.setTypeList(typeList);
				//查询单个游戏视频
				if (cmsContentRequest.getVideoTypeIndex() != null && CmsChannel.getAllChannelIndexList().contains(cmsContentRequest.getVideoTypeIndex())) {
					CmsChannel channelGame = CmsChannel.valueOf(cmsContentRequest.getVideoTypeIndex());
					condition.setChannelId(channelGame.getChanneId());
				}
			}

			if (cmsContentRequest.getPageNo() != null) {
				page.setPageNo(cmsContentRequest.getPageNo());
			}
			if (cmsContentRequest.getPageSize() != null) {
				page.setPageSize(cmsContentRequest.getPageSize());
			}
			TimeInterval queryTimer = cn.hutool.core.date.DateUtil.timer();
			LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
			hashMap.put(BaseOrderField.ISSUE_TIME, BaseOrderField.DESC);
			condition.setOrderField(hashMap); //按照发布时间排序
			PageResult<CmsContent> pageResult = cmsContentServiceClient.queryCmsContentPage(condition, page);
			logger.info(logPrefix + "queryCmsContentPage-统计分页查询方法执行的毫秒数：{}", queryTimer.interval());

			if (!pageResult.isSuccess()) {
				logger.error("查询分页咨询内容出错，错误信息:{}", pageResult.getErrorMsg());
				return newsListVoList;
			}
			List<CmsContent> dataList = pageResult.getPage().getDataList();
			/*if (dataList!=null && dataList.size()>0) {
				SysConfigProperty sysConfigProperty = commonManager.getSysConfigByKey(SysConfigPropertyKey.VIDEO_SHOW_SWITCH,cmsContentRequest.getClientType());
				String value = sysConfigProperty.getValue();
				if (StringUtils.isNotBlank(value) && value.trim().equals("0")) {//视频是关闭的
					dataList = dataList.stream().filter(e ->  !CmsTypeDefineConstant.getVideoIndexList().contains(e.getTypeId())).collect(Collectors.toList());
				}
			}*/
			TimeInterval queryTimer1 = cn.hutool.core.date.DateUtil.timer();
			newsListVoList = convertCmsToNews(dataList);
			//点赞关注
			setFollowedAndUserUp(userId, newsListVoList);
			logger.info(logPrefix + "convertCmsToNews-统计方法执行的毫秒数：{}", queryTimer1.interval());

			long interval = timer.interval();
			logger.info(logPrefix + "getCmsContentList-统计方法执行的毫秒数：{}", interval);
			timer.intervalRestart();//返回花费时间，并重置开始时间
		} catch (Exception e) {
			logger.error("查询资讯内容出错，出错信息：{}", e);
			e.printStackTrace();
		}
		return newsListVoList;

	}

	@EsportMethodLog(paramLog = false)
	public List<NewsListVo> convertCmsToNews(List<CmsContent> dataList) throws Exception {
		CmsTypeDefine cmsTypeDefine = new CmsTypeDefine();
		ModelResult<List<CmsTypeDefine>> modelResult = cmsTypeDefineServiceClient.queryCmsTypeDefine(cmsTypeDefine);
		if (!modelResult.isSuccess() && modelResult.getModel().size() == 0) {
			logger.error("查询视频类型出错或者查询到0条，错误信息：{}", modelResult.getErrorMsg());
		}
		List<CmsTypeDefine> allDefineType = modelResult.getModel();
		/** 视频 */
		List<Long> listVideo = allDefineType.stream().filter(e -> e.getCategory().equals(CmsCategoryType.video.getIndex())).map(e -> e.getId())
				.collect(Collectors.toList());
		/** 文章 */
		List<Long> listArticleLong = allDefineType.stream().filter(e -> e.getCategory().equals(CmsCategoryType.article_long.getIndex()))
				.map(e -> e.getId()).collect(Collectors.toList());
		List<Long> listArticleShort = allDefineType.stream().filter(e -> e.getCategory().equals(CmsCategoryType.article_short.getIndex()))
				.map(e -> e.getId()).collect(Collectors.toList());

		List<NewsListVo> listVos = Lists.newArrayList();
		if (dataList != null && dataList.size() > 0) {
			listVos = Lists.newArrayList();
			for (CmsContent cmsVo : dataList) {
				NewsListVo newVo = new NewsListVo();
				newVo.setAricleId(cmsVo.getId());
				//设置类型 CmsCategoryType 长文、短文、视频、投票
				for (CmsTypeDefine typeDefine : allDefineType) {
					if (typeDefine.getId().intValue() == cmsVo.getTypeId().intValue()) {
						newVo.setType(typeDefine.getCategory());
					}
				}
				//作者
				newVo.setTag(cmsVo.getAuthor());
				if (cmsVo.getIssueUserId() != null) {
					ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient
							.queryConsumerById(cmsVo.getIssueUserId(), new UserConsumerQueryOption());
					if (userConsumerModelResult != null && userConsumerModelResult.isSuccess() && userConsumerModelResult.getModel() != null) {
						//作者头像
						newVo.setAuthorImg(userConsumerModelResult.getModel().getIcon());
					}
				}
				//如果是短文
				if (listArticleShort.contains(cmsVo.getTypeId().longValue())) {//用户发布的短文
					//设置 内容文字、内容图片转集合、标题图片
					newVo.setDetail(cmsVo.getContent());
					newVo.setImageSrc(cmsVo.getContentImg());
					if (StringUtils.isNotBlank(cmsVo.getContentImg())) {
						newVo.setShortArticleList(Arrays.asList(cmsVo.getContentImg().split(",")));
					}
					newVo.setTitleImg(cmsVo.getTitleImg());
				} else {
					//不是短文
					newVo.setDetail(cmsVo.getTitle());
				}
				newVo.setIssueUserId(cmsVo.getIssueUserId());
				// newVo.setSeeNum(cmsVo.getViews().longValue());
				/* 从缓存取 */
				long idNum = cmsVo.getId() % 100;
				TimeInterval timeInterval = new TimeInterval();
				Long cacheSetSize = cachedManager.getCacheSetSize(CacheType.CMS_CONTENT.getIndex(), cmsVo.getId() + "");
				//logger.info("cacheSetSize 方法执行的时间【{}】",timeInterval.intervalPretty());
				newVo.setSeeNum(cmsVo.getComments().longValue() * 2 + cacheSetSize + idNum);

				newVo.setCommentNum(cmsVo.getUps());
				newVo.setDiscussNum(cmsVo.getComments().longValue());

				if (cmsVo.getUps() < 0) {
					logger.info(logPrefix + "文章ID：【{}】，点赞数小于0异常 【{}】", newVo.getAricleId(), cmsVo.getUps());
					newVo.setUpFlag(false);
					newVo.setUps(0L);
				} else {
					newVo.setUps(cmsVo.getUps().longValue());
				}
				newVo.setTitleImg(cmsVo.getTitleImg());
				newVo.setIsTop(cmsVo.getIsTop());
				// if (cmsVo.getTitleImg()!=null &&
				// cmsVo.getTitleImg().contains("upload") ){
				// cmsVo.setTitleImg(imageDomain+cmsVo.getTitleImg());
				// }
				if (listArticleLong.contains(cmsVo.getTypeId().longValue())) {
					newVo.setImageSrc(cmsVo.getTitleImg());
				} else if (listVideo.contains(cmsVo.getTypeId().longValue())) {
					/* 优先取下载地址 */
					if (cmsVo.getYunVideoStatus() == YunVideoStatus.SUCCESS.getIndex() && StringUtils.isNotEmpty(cmsVo.getYunVideoUrl())) {
						newVo.setImageSrc(cmsVo.getYunVideoUrl());
					} else if (cmsVo.getTypeId().intValue() == CmsTypeDefineConstant.USER_VIDEO.getIndex()) {
						newVo.setImageSrc(cmsVo.getContent());
					} else {
						newVo.setImageSrc(videoUrlDomain + cmsVo.getContent());
					}

				}
				if (!newVo.getImageSrc().contains("https")) {
					newVo.setImageSrc(newVo.getImageSrc().replace("http", "https"));
				}
				String publishTimeStr = "";
				if (cmsVo.getIssueTime() == null) {
					publishTimeStr = DateUtil.dateToString(cmsVo.getIssueTime(), "yyyy-MM-dd HH:mm:ss");
				} else {
					publishTimeStr = DateUtil.dateToString(cmsVo.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
				}
				newVo.setUpdateTime(cmsVo.getUpdateTime());
				newVo.setPublishTimeStr(publishTimeStr);
				listVos.add(newVo);
			}
			return listVos;
		}
		return listVos;
	}

	@EsportMethodLog(paramLog = false)
	public List<NewsListVo> getFollowerCmsContentList(FollowRequest request) {
		List<NewsListVo> newsListVos = Lists.newArrayList();
		QueryCmsContentParam queryCmsContentParam = new QueryCmsContentParam();
		queryCmsContentParam.setClientType(request.getClientType());
		queryCmsContentParam.setAgentId(request.getAgentId());
		queryCmsContentParam.setBiz(request.getBiz());
		DataPage<CmsContent> page = new DataPage<>();
		PageResult<CmsContent> pageResult = null;
		try {
			page.setPageSize(request.getPageSize());
			page.setPageNo(request.getPageNo());

			if (null != request.getCmsTopicId()) {
				/** 话题内页的查询，携带话题ID */
				logger.info(logPrefix + "查询话题资讯列表接收到的参数 【{}】", JSONObject.toJSONString(request));
				CmsContentQueryVo cmsContentQueryVo = new CmsContentQueryVo();
				cmsContentQueryVo.setAgentId(request.getAgentId());
				cmsContentQueryVo.setBiz(request.getBiz());
				cmsContentQueryVo.setClientType(request.getClientType());
				cmsContentQueryVo.setCmsTopicId(request.getCmsTopicId());
				cmsContentQueryVo.setContentType(request.getContentType());
				cmsContentQueryVo.setUserId(request.getUserId());
				if (request.getLastUpdateTime() != null) {
					cmsContentQueryVo.setEndUpdateTime(cn.hutool.core.date.DateUtil.calendar(Long.valueOf(request.getLastUpdateTime())).getTime());

				}
				logger.info(logPrefix + "话题ID：【{}】，话题内页查询，参数param 【{}】", request.getCmsTopicId(), JSON.toJSONString(cmsContentQueryVo));
				pageResult = cmsContentServiceClient.queryCmsContentPageByTopicId(cmsContentQueryVo, page);

			} else if (request.getShowUserCms()) {
				Long userId = request.getUserId();
				//用户未登录
				if (null == userId) {

					/** 发现关注页，用户未登录状态 */
					CmsContentQueryVo param = new CmsContentQueryVo();
					param.setClientType(request.getClientType());
					param.setAgentId(request.getAgentId());
					param.setBiz(request.getBiz());
					if (request.getLastUpdateTime() != null) {
						param.setEndUpdateTime(cn.hutool.core.date.DateUtil.calendar(Long.valueOf(request.getLastUpdateTime())).getTime());
					}
					logger.info(logPrefix + "当前关注页查询，用户未登录，查询参数param 【{}】", JSON.toJSONString(param));
					pageResult = cmsContentServiceClient.userNoLoginShowTopicCms(param, page);
				} else {
					/** 发现关注页，登录状态 */
					queryCmsContentParam.setNowUserId(userId);
					if (request.getShowTop()) {
						//展示置顶内容时，列表内容不再展示置顶内容
						queryCmsContentParam.setShowTop(false);
					}
					LinkedHashMap<String, String> param = new LinkedHashMap<>();
					param.put(BaseOrderField.UPDATE_TIME, BaseOrderField.DESC);
					queryCmsContentParam.setOrderField(param);
					if (request.getLastUpdateTime() != null) {
						queryCmsContentParam
								.setEndUpdateTime(cn.hutool.core.date.DateUtil.calendar(Long.valueOf(request.getLastUpdateTime())).getTime());

					}
					logger.info(logPrefix + "用户：【{}】，当前关注页用户已登录，参数param 【{}】", userId, JSON.toJSONString(queryCmsContentParam));
					pageResult = cmsContentServiceClient.queryCmsContentPageByFollower(queryCmsContentParam, page);
				}

			}
			if (!pageResult.isSuccess()) {
				logger.error(logPrefix + "关注用户资讯列表，用户ID：【{}】，参数param 【{}】", request.getUserId(), JSON.toJSONString(queryCmsContentParam));
				return newsListVos;
			}
			List<CmsContent> cmsContentList = pageResult.getPage().getDataList();
			//logger.info(logPrefix + "查询实际返回记录数 【{}】", cmsContentList.size());

			newsListVos = convertCmsToNews(cmsContentList);
			//logger.info(logPrefix + "过滤后的返回记录数 【{}】", newsListVos.size());
			//设置点赞和关注
			setFollowedAndUserUp(request.getUserId(), newsListVos);
		} catch (Exception e) {
			logger.error(logPrefix + "关注用户资讯列表，用户ID：【{}】，错误信息 【{}】", request.getUserId(), e);
			e.printStackTrace();
			return newsListVos;
		}
		return newsListVos;

	}

	/**
	 * 查询置顶的话题内容
	 * @return
	 */
	public List<NewsListVo> getTopContentList(Long userId, CmsContentParam param) {
		List<NewsListVo> result = null;
		try {
			List<Integer> typeList = Lists.newArrayList();
			param.setStatus(CmsContentStatus.ISSUE.getIndex());
			typeList.add(CmsTypeDefineConstant.USER_ARTICLE_SHORT.getIndex());
			typeList.add(CmsTypeDefineConstant.USER_VIDEO.getIndex());
			param.setTypeList(typeList);
			param.setShowTop(true);
			LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
			hashMap.put(BaseOrderField.IS_TOP, BaseOrderField.DESC);
			param.setOrderField(hashMap);
			ModelResult<List<CmsContent>> modelResult = cmsContentServiceClient.queryList(param);
			if (modelResult.isSuccess() && null != modelResult.getModel()) {
				result = convertCmsToNews(modelResult.getModel());
			} else {
				logger.info("查询置顶话题出错：{}", modelResult.getErrorMsg());
			}
			//点赞关注
			setFollowedAndUserUp(userId, result);
		} catch (Exception e) {
			logger.error("查询置顶话题错误：{}");
		}
		return result;
	}


	/**
	 * 设置点赞和关注
	 * @param userId
	 * @param newsListVos
	 */
	public void setFollowedAndUserUp(Long userId, List<NewsListVo> newsListVos) {
		for (NewsListVo newsListVo : newsListVos) {

			try {
				if (newsListVo.getIssueUserId() == null) {
					logger.error(logPrefix + "文章ID【{}】,当前文章发布者ID为空", newsListVo.getAricleId());
					continue;
				}
				ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient
						.queryConsumerById(newsListVo.getIssueUserId(), new UserConsumerQueryOption());
				if (!userConsumerModelResult.isSuccess() || userConsumerModelResult.getModel() == null) {
					logger.error(logPrefix + "用户【{}】不存在", newsListVo.getIssueUserId());
					continue;
				}
				UserConsumer follower = userConsumerModelResult.getModel();

				FollowedUserVo followedUserVo = new FollowedUserVo();
				followedUserVo.setUserNickName( follower.getNickName() );
				followedUserVo.setUserIcon( follower.getIcon() );
				followedUserVo.setFans( follower.getFans() );
				followedUserVo.setFollowStatus( FollowStatus.CANCEL.getIndex() );
				//查询当前用户是否关注文章作者
				if (userId != null) {

					UserFollowQueryVo userFollowQueryVo = new UserFollowQueryVo();
					//当前用户
					userFollowQueryVo.setUserId( userId );
					userFollowQueryVo.setObjectType( FollowObjectType.USER.getIndex() );
					userFollowQueryVo.setStatus( FollowStatus.FOLLOW.getIndex() );
					//关注对象，即文章发布者
					userFollowQueryVo.setObjectId( newsListVo.getIssueUserId() );
					ModelResult<List<UserFollow>> listModelResult = userFollowServiceClient.queryList( userFollowQueryVo );
					if (listModelResult != null && listModelResult.isSuccess()) {
						List<UserFollow> userFollowList = listModelResult.getModel();
						if (userFollowList != null && userFollowList.size() > 0) {
							followedUserVo.setFollowStatus( FollowStatus.FOLLOW.getIndex() );
						}
					}
				}

				newsListVo.setFollowedUser(followedUserVo);

				/** 获取点赞标记 */
				if (userId != null && newsListVo.getUps() > 0) {
					UserUpQueryVo userUpQueryVo = new UserUpQueryVo();
					userUpQueryVo.setUserId(userId);
					userUpQueryVo.setObjectTypeList(UserUpObjectType.getContentIndexList());
					userUpQueryVo.setObjectId(newsListVo.getAricleId());
					userUpQueryVo.setStatus(1);
					ModelResult<Integer> upCount = userUpServiceClient.queryCount(userUpQueryVo);
                    if (upCount != null && upCount.isSuccess() && upCount.getModel() != null ) {
                        newsListVo.setUpFlag(upCount.getModel() > 0);
                    }
				}
				/** 设置用户是否评论标记 */
				if (userId != null && newsListVo.getDiscussNum() > 0){
					CmsCommentParam cmsCommentParam = new CmsCommentParam();
					cmsCommentParam.setUserId(userId);
					cmsCommentParam.setObjectId(newsListVo.getAricleId());
					cmsCommentParam.setStatus(AuditStatus.AUDIT_SECC.getIndex());
					ModelResult<Integer> commentCount = cmsCommentServiceClient.queryCount(cmsCommentParam);
                    if (commentCount != null && commentCount.isSuccess() && commentCount.getModel() != null ) {
                        newsListVo.setDiscussFlag(commentCount.getModel() > 0);
                    }
				}
			} catch (Exception e) {
				logger.error("NewsListVo 设置关注和点赞错误，userId:{},文章Id:{}", newsListVo.getAricleId());
			}
		}
	}


	/**
	 * 获取用户记录:我的发布;我的收藏;我的点赞;我的评论
	 * @param followRequest
	 * @param dataPage
	 * @param userConsumer
	 * @return
	 */
	public DataPage<CmsMsgResponse> getUserRecord(FollowRequest followRequest, DataPage dataPage, UserConsumer userConsumer) {
		DataPage<CmsMsgResponse> resultPage = new DataPage<>();
		CmsContentQueryVo queryVo = new CmsContentQueryVo();
		queryVo.setUserId(userConsumer.getId());
		BeanUtil.copyProperties(followRequest, queryVo, CopyOptions.create().ignoreNullValue());
		try {
			/** 我发布的文章*/
			if (followRequest.getCmsMsgType() == CmsMsgType.ISSUE.getIndex()) {
				List<Integer> typeList = Lists.newArrayList();
				//查询全部文章和视频资讯
				typeList.add(CmsTypeDefineConstant.USER_ARTICLE_SHORT.getIndex());
				typeList.add(CmsTypeDefineConstant.USER_VIDEO.getIndex());
				queryVo.setTypeList(typeList);
				queryVo.setStatus(CmsContentStatus.ISSUE.getIndex());
				PageResult<CmsUserMsgResult> pageResult = cmsContentServiceClient.queryPageByUserId(queryVo, dataPage);
				if (!pageResult.isSuccess()) {
					logger.error(logPrefix + "用户发布的短文失败，用户ID：【{}】，参数param 【{}】，失败信息：【{}】", followRequest.getUserId(), JSON.toJSONString(queryVo),
							pageResult.getErrorMsg());
				}
				cmsContentResult(followRequest, userConsumer, resultPage, pageResult);

				/** 我的收藏*/
			} else if (followRequest.getCmsMsgType() == CmsMsgType.FAVORITE.getIndex()) {
				PageResult<CmsUserMsgResult> pageResult = cmsContentServiceClient.queryPageByUserFavorite(queryVo, dataPage);
				if (!pageResult.isSuccess()) {
					logger.info("查询用户收藏文章,查询失败，失败信息：{}", pageResult.getErrorMsg());
				}
				cmsContentResult(followRequest, userConsumer, resultPage, pageResult);


				/** 我的点赞*/
			} else if (followRequest.getCmsMsgType() == CmsMsgType.UP.getIndex()) {
				PageResult<CmsUserMsgResult> pageResult = cmsContentServiceClient.queryPageByUserUp(queryVo, dataPage);
				if (!pageResult.isSuccess()) {
					logger.info("查询用户点赞文章,查询失败，失败信息：{}", pageResult.getErrorMsg());
				}
				cmsContentResult(followRequest, userConsumer, resultPage, pageResult);

				/** 我的评论*/
			} else if (followRequest.getCmsMsgType() == CmsMsgType.COMMENT.getIndex()) {
				/**审核通过的文章*/
				queryVo.setStatus(CmsCommentStatus.VERIFIED.getIndex());
				PageResult<CmsUserMsgResult> pageResult = cmsContentServiceClient.queryPageByUserComment(queryVo, dataPage);
				if (!pageResult.isSuccess()) {
					logger.info("查询用户评论文章,查询失败，失败信息：{}", pageResult.getErrorMsg());
				}
				cmsContentResult(followRequest, userConsumer, resultPage, pageResult);


			} else {
				logger.info(logPrefix + "cmsMsgType", followRequest.getCmsMsgType());
			}


		} catch (Exception e) {
			logger.info(logPrefix + "用户id：{}", userConsumer.getId(), e);
		}
		return resultPage;
	}

	private void cmsContentResult(FollowRequest followRequest, UserConsumer userConsumer, DataPage<CmsMsgResponse> resultPage,
			PageResult<CmsUserMsgResult> pageResult) throws Exception {
		DataPage<CmsUserMsgResult> cmsContentDataPage = pageResult.getPage();
		if (cmsContentDataPage.getDataList() != null) {
			List<CmsUserMsgResult> cmsContentList = cmsContentDataPage.getDataList();
			List<CmsMsgResponse> newsListVos = convertCmsUserToNews(cmsContentList, followRequest.getCmsMsgType());
			if (followRequest.getCmsMsgType() == CmsMsgType.COMMENT.getIndex()) {
				for (CmsMsgResponse cmsMsgResponse : newsListVos) {
					cmsMsgResponse.setIssueUserId(userConsumer.getId());
				}
			}
			setMsgAndUserUp(userConsumer.getId(), newsListVos);
			resultPage.setPageSize(cmsContentDataPage.getPageSize());
			resultPage.setPageNo(cmsContentDataPage.getPageNo());
			resultPage.setTotalCount(cmsContentDataPage.getTotalCount());
			resultPage.setDataList(newsListVos);
		}
	}


	/**
	 * 设置点赞和关注
	 * @param userId
	 * @param cmsMsgResponses
	 */
	public void setMsgAndUserUp(Long userId, List<CmsMsgResponse> cmsMsgResponses) {
		for (CmsMsgResponse cmsMsgResponse : cmsMsgResponses) {
			try {
				if (cmsMsgResponse.getIssueUserId() == null) {
					logger.error(logPrefix + "文章ID【{}】,当前文章发布者ID为空", cmsMsgResponse.getAricleId());
					continue;
				}
				ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient
						.queryConsumerById(cmsMsgResponse.getIssueUserId(), new UserConsumerQueryOption());
				if (!userConsumerModelResult.isSuccess() || userConsumerModelResult.getModel() == null) {
					logger.error(logPrefix + "用户【{}】不存在", cmsMsgResponse.getIssueUserId());
					continue;
				}
				UserConsumer follower = userConsumerModelResult.getModel();
				FollowedUserVo followedUserVo = new FollowedUserVo();
				followedUserVo.setUserNickName(follower.getNickName());
				followedUserVo.setUserIcon(follower.getIcon());
				followedUserVo.setFollowStatus(FollowStatus.FOLLOW.getIndex());
				followedUserVo.setFans(follower.getFans());
				cmsMsgResponse.setFollowedUser(followedUserVo);

				/** 获取点赞标记 */
				if (userId != null) {
					UserUpQueryVo userUpQueryVo = new UserUpQueryVo();
					userUpQueryVo.setUserId(userId);
					userUpQueryVo.setObjectTypeList(UserUpObjectType.getContentIndexList());
					userUpQueryVo.setObjectId(cmsMsgResponse.getAricleId());
					userUpQueryVo.setStatus(1);
					ModelResult<Integer> upCount = userUpServiceClient.queryCount(userUpQueryVo);
					if (upCount != null && upCount.isSuccess() && upCount.getModel() != null) {
						cmsMsgResponse.setUpFlag(upCount.getModel() > 0);
					}
				}
			} catch (Exception e) {
				logger.error("NewsListVo 设置关注和点赞错误，userId:{},文章Id:{}", cmsMsgResponse.getAricleId());
			}
		}
	}


	public List<CmsMsgResponse> convertCmsUserToNews(List<CmsUserMsgResult> dataList, Integer cmsMsgType) throws Exception {
		CmsTypeDefine cmsTypeDefine = new CmsTypeDefine();
		ModelResult<List<CmsTypeDefine>> modelResult = cmsTypeDefineServiceClient.queryCmsTypeDefine(cmsTypeDefine);
		if (!modelResult.isSuccess() && modelResult.getModel().size() == 0) {
			logger.error("查询视频类型出错或者查询到0条，错误信息：{}", modelResult.getErrorMsg());
		}
		List<CmsTypeDefine> allDefineType = modelResult.getModel();
		/** 视频 */
		List<Long> listVideo = allDefineType.stream().filter(e -> e.getCategory().equals(CmsCategoryType.video.getIndex())).map(e -> e.getId())
				.collect(Collectors.toList());
		/** 文章 */
		List<Long> listArticleLong = allDefineType.stream().filter(e -> e.getCategory().equals(CmsCategoryType.article_long.getIndex()))
				.map(e -> e.getId()).collect(Collectors.toList());
		List<Long> listArticleShort = allDefineType.stream().filter(e -> e.getCategory().equals(CmsCategoryType.article_short.getIndex()))
				.map(e -> e.getId()).collect(Collectors.toList());

		List<CmsMsgResponse> listVos = Lists.newArrayList();
		if (dataList != null && dataList.size() > 0) {
			listVos = Lists.newArrayList();
			for (CmsUserMsgResult cmsVo : dataList) {

				CmsMsgResponse newVo = new CmsMsgResponse();
				newVo.setAricleId(cmsVo.getId());
				newVo.setMsgType(cmsMsgType);
				newVo.setFavorites(cmsVo.getFavorites());
				newVo.setVideoSignId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
				/**为评论时:是2级时，去查父评论信息*/
				if (cmsMsgType == CmsMsgType.COMMENT.getIndex()) {
					newVo.setCurrentComment(cmsVo.getMsgContent());
					newVo.setCmsObjectUserId(cmsVo.getIssueUserId());
					newVo.setCmsObjectUserName(cmsVo.getIssueUser());
					cmsVo.setIssueTime(cmsVo.getCreateTime());
					if (cmsVo.getCommentLevel() == CmsCommentLevel.CMS_COMMENT.getIndex()) {
						Long id = cmsVo.getParentCommentId();
						ModelResult<CmsComment> cmsCommentModelResult = cmsCommentServiceClient.queryCmsCommentById(id);
						if (cmsCommentModelResult != null && cmsCommentModelResult.getModel() != null) {
							CmsComment cmsComment = cmsCommentModelResult.getModel();
							newVo.setBeComment(cmsComment.getContent());
							newVo.setObjectUserId(cmsComment.getUserId());
							newVo.setObjectUserName(cmsComment.getUserName());
						}
					}
				}
				/** 为收藏时*/
				if (cmsMsgType == CmsMsgType.FAVORITE.getIndex()) {
					newVo.setFavoritesFlag(true);
					cmsVo.setCreateTime(cmsVo.getUpdateTime());
				}

				//设置类型 CmsCategoryType 长文、短文、视频、投票
				for (CmsTypeDefine typeDefine : allDefineType) {
					if (typeDefine.getId().intValue() == cmsVo.getTypeId().intValue()) {
						newVo.setType(typeDefine.getCategory());
					}
				}
				//作者
				newVo.setTag(cmsVo.getAuthor());
				if (cmsVo.getIssueUserId() != null) {
					ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient
							.queryConsumerById(cmsVo.getIssueUserId(), new UserConsumerQueryOption());
					if (userConsumerModelResult != null && userConsumerModelResult.isSuccess() && userConsumerModelResult.getModel() != null) {
						//作者头像
						newVo.setAuthorImg(userConsumerModelResult.getModel().getIcon());
					}
				}
				//如果是短文
				if (listArticleShort.contains(cmsVo.getTypeId().longValue())) {//用户发布的短文
					//设置 内容文字、内容图片转集合、标题图片
					newVo.setDetail(cmsVo.getContent());
					newVo.setImageSrc(cmsVo.getContentImg());
					if (StringUtils.isNotBlank(cmsVo.getContentImg())) {
						newVo.setShortArticleList(Arrays.asList(cmsVo.getContentImg().split(",")));
					}
					newVo.setTitleImg(cmsVo.getTitleImg());
				} else {
					//不是短文
					newVo.setDetail(cmsVo.getTitle());
				}
				newVo.setIssueUserId(cmsVo.getIssueUserId());
				/* 从缓存取 */
				long idNum = cmsVo.getId() % 100;
				TimeInterval timeInterval = new TimeInterval();
				Long cacheSetSize = cachedManager.getCacheSetSize(CacheType.CMS_CONTENT.getIndex(), cmsVo.getId() + "");
				newVo.setSeeNum(cmsVo.getComments().longValue() * 2 + cacheSetSize + idNum);

				newVo.setCommentNum(cmsVo.getUps());
				newVo.setDiscussNum(cmsVo.getComments().longValue());
				newVo.setUps(cmsVo.getUps().longValue());
				newVo.setTitleImg(cmsVo.getTitleImg());
				newVo.setIsTop(cmsVo.getIsTop());
				if (listArticleLong.contains(cmsVo.getTypeId().longValue())) {
					newVo.setImageSrc(cmsVo.getTitleImg());
				} else if (listVideo.contains(cmsVo.getTypeId().longValue())) {
					/* 优先取下载地址 */
					if (cmsVo.getYunVideoStatus() == YunVideoStatus.SUCCESS.getIndex() && StringUtils.isNotEmpty(cmsVo.getYunVideoUrl())) {
						newVo.setImageSrc(cmsVo.getYunVideoUrl());
					} else if (cmsVo.getTypeId().intValue() == CmsTypeDefineConstant.USER_VIDEO.getIndex()) {
						newVo.setImageSrc(cmsVo.getContent());
					} else {
						newVo.setImageSrc(videoUrlDomain + cmsVo.getContent());
					}
				}
				String publishTimeStr = "";
				if (cmsVo.getIssueTime() == null) {
					publishTimeStr = DateUtil.dateToString(cmsVo.getIssueTime(), "yyyy-MM-dd HH:mm:ss");
				} else {
					publishTimeStr = DateUtil.dateToString(cmsVo.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
				}
				newVo.setPublishTimeStr(publishTimeStr);
				listVos.add(newVo);
			}
			return listVos;
		}
		return listVos;
	}
}
