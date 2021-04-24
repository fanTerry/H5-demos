package com.esportzoo.esport.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.cms.CmsCommentServiceClient;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.client.service.common.CmsTypeDefineServiceClient;
import com.esportzoo.esport.connect.response.CmsMsgResponse;
import com.esportzoo.esport.constants.CmsCategoryType;
import com.esportzoo.esport.constants.CmsTypeDefineConstant;
import com.esportzoo.esport.constants.cms.CmsMsgType;
import com.esportzoo.esport.constants.cms.YunVideoStatus;
import com.esportzoo.esport.domain.*;
import com.esportzoo.esport.option.CmsContentParam;
import com.esportzoo.esport.vo.cms.CmsMsgVo;
import com.esportzoo.esport.vo.cms.FollowedUserVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tingjun.wang
 * @date 2019/12/25 11:22上午
 */
@Component
public class UserMessageManager {
	private transient Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CmsCommentServiceClient cmsCommentServiceClient;
	@Autowired
	private CmsContentServiceClient cmsContentServiceClient;
	@Autowired
	@Qualifier("cmsTypeDefineServiceClient")
	CmsTypeDefineServiceClient cmsTypeDefineServiceClient;

	@Autowired
	private CmsContentManager cmsContentManager;

	@Autowired
	private RedisClient redisClient;

	@Value("${video.domain}")
	private String videoUrlDomain;
	private static String cmsMsgCatchKey = "cms_msg_cache_";
	/**
	 * 资讯话题类消息
	 * */
	public DataPage<CmsMsgResponse> getCmsMsg(CmsMsgVo cmsMsgVo,DataPage dataPage,UserConsumer userConsumer){
		DataPage<CmsMsgResponse> resultPage = null;
		try {
			String cacheKey = cmsMsgCatchKey+cmsMsgVo.getObjectUserId()+"_"+dataPage.getPageNo();
			resultPage = redisClient.getObj(cacheKey);
			if (resultPage != null){
				return resultPage;
			}
			resultPage = new DataPage<>();

			PageResult<CmsMsgResult> pageResult = cmsCommentServiceClient.queryMsgByCondition(cmsMsgVo, dataPage);
			if (!pageResult.isSuccess() || pageResult.getPage() == null){
				logger.info("查询用户cmsMsg异常或为空，request:{},msg：{}",pageResult.getErrorMsg());
				return resultPage;
			}
			DataPage<CmsMsgResult> resultDataPage = pageResult.getPage();
			List<CmsMsgResult> msgResultList = resultDataPage.getDataList();
			if (CollectionUtil.isEmpty(msgResultList)){
				logger.info("查询用户cmsMsg为空");
				return resultPage;
			}

			CmsTypeDefine cmsTypeDefine = new CmsTypeDefine();
			ModelResult<List<CmsTypeDefine>> modelResult = cmsTypeDefineServiceClient.queryCmsTypeDefine(cmsTypeDefine);
			if (!modelResult.isSuccess() && modelResult.getModel().size() == 0) {
				logger.error("查询视频类型出错或者查询到0条，错误信息：{}", modelResult.getErrorMsg());
				return resultPage;
			}
			List<CmsTypeDefine> allDefineType = modelResult.getModel();

			//CmsTypeDefineConstant 对应的数据库
			Map<Integer, Integer>  defineTypeMap= allDefineType.stream().collect(Collectors.toMap(key -> key.getId().intValue(), value -> value.getCategory()));

			//被操作对象是评论时，该评论所属的文章或视频Id
			List<Long> cmtObjectIdList = new ArrayList<>();
			//被操作对象是评论时，该评论所属的文章或视频
			Map<String,CmsMsgResponse> cmtObjectMap = new HashMap<>();

			List<CmsMsgResponse> responseList = new ArrayList<>();
			String nickName = userConsumer.getNickName();
			for (CmsMsgResult cmsMsgResult : msgResultList) {
				CmsMsgResponse cmsMsgResponse = new CmsMsgResponse();
				cmsMsgResponse.setMsgType(cmsMsgResult.getMsgType());

				/* 创建该记录的用户信息 */
				FollowedUserVo followedUser = new FollowedUserVo();
				followedUser.setUserNickName(cmsMsgResult.getNickName());
				followedUser.setFans(cmsMsgResult.getFans());
				followedUser.setUserIcon(cmsMsgResult.getIcon());
				//此处 (因前端组件共用) 创建该记录的用户信息 应填在曾经的发布人信息位置上
				cmsMsgResponse.setIssueUserId(cmsMsgResult.getUserId());
				cmsMsgResponse.setTag(cmsMsgResult.getNickName());
				cmsMsgResponse.setFollowedUser(followedUser);


				//操作对想不是评论时为空
				cmsMsgResponse.setCurrentComment(cmsMsgResult.getContent());

				/* 该记录操作对象的信息 */
				Long cmtObjectId = cmsMsgResult.getCmtObjectId();
				cmsMsgResponse.setPublishTimeStr(DateUtil.dateToString(cmsMsgResult.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				//评论数收藏数和点赞数
				Integer msgType = cmsMsgResult.getMsgType().intValue();
				if (msgType == CmsMsgType.COMMENT.getIndex()){
					cmsMsgResponse.setDiscussNum(Long.valueOf(cmsMsgResult.getReplies()));
				}else if (msgType == CmsMsgType.UP.getIndex()){
					cmsMsgResponse.setUps(Long.valueOf(cmsMsgResult.getUps()));
					cmsMsgResponse.setCommentNum(cmsMsgResult.getUps());
					cmsMsgResponse.setUpFlag(true);
				}else {
					cmsMsgResponse.setFavorites(cmsMsgResult.getFavorites());
					cmsMsgResponse.setFavoritesFlag(true);
				}

				if (cmtObjectId == null){//正常的操作
					cmsMsgResponse.setCmsObjectUserId(userConsumer.getId());
					cmsMsgResponse.setCmsObjectUserName(nickName);
					cmsMsgResponse.setAricleId(cmsMsgResult.getObjectId());
					cmsMsgResponse.setTitleImg(cmsMsgResult.getTitleImg());

					Integer type = defineTypeMap.get(cmsMsgResult.getObjectType());
					if (type == null){
						continue;
					}
					cmsMsgResponse.setType(type);

					/* 标题，内容，视频地址的处理 */
					String imageSrc = null;
					String title = null;
					if (type.intValue() == CmsCategoryType.article_short.getIndex()){
						//短文
						title = cmsMsgResult.getAcceptContent();
						imageSrc = cmsMsgResult.getContentImg();
						if (StringUtils.isNotBlank(imageSrc)){
							cmsMsgResponse.setShortArticleList(Arrays.asList(imageSrc.split(",")));
						}
					}else if (type.intValue() == CmsCategoryType.article_long.getIndex()){
						//长文
						title = cmsMsgResult.getAcceptTitle();
						imageSrc = cmsMsgResult.getTitleImg();
					}else if (type.intValue() == CmsCategoryType.video.getIndex()){
						//视频
						title = cmsMsgResult.getAcceptTitle();
						if (cmsMsgResult.getYunVideoStatus() == YunVideoStatus.SUCCESS.getIndex() && StringUtils.isNotEmpty(cmsMsgResult.getYunVideoUrl())) {
							imageSrc = cmsMsgResult.getYunVideoUrl();
						} else if (cmsMsgResult.getObjectType().intValue() == CmsTypeDefineConstant.USER_VIDEO.getIndex()) {
							//用户发布视频
							imageSrc = cmsMsgResult.getAcceptContent();
						} else {
							imageSrc = videoUrlDomain + cmsMsgResult.getAcceptContent();
						}
					}
					if (cmsMsgResponse.getType()==CmsCategoryType.video.getIndex()){
						cmsMsgResponse.setVideoSignId(UUID.randomUUID().toString());
					}
					cmsMsgResponse.setImageSrc(imageSrc);
					cmsMsgResponse.setDetail(title);
				}else {
					//被操作对象是评论时，文章ID是cmtObjectId,
					cmsMsgResponse.setObjectUserId(userConsumer.getId());
					cmsMsgResponse.setObjectUserName(nickName);
					cmsMsgResponse.setBeComment(cmsMsgResult.getAcceptContent());
					cmtObjectIdList.add(cmtObjectId);
					cmtObjectMap.put(cmtObjectId+"_"+UUID.randomUUID().toString(),cmsMsgResponse);
				}



				//cmsMsgResponse.setAcceptNickName(nickName);
				responseList.add(cmsMsgResponse);
			}
			setCmtObject(cmtObjectIdList,cmtObjectMap,responseList);

			resultPage.setPageNo(resultDataPage.getPageNo());
			resultPage.setPageSize(resultDataPage.getPageSize());
			resultPage.setTotalCount(resultDataPage.getTotalCount());
			resultPage.setDataList(responseList);
			redisClient.setObj(cacheKey,60*2,resultPage);
			return resultPage;
		}catch (Exception e){
			logger.error("分页处理cmsMsg异常,userId:{},param:{},dataPage:{}",
					userConsumer.getId(),JSON.toJSONString(cmsMsgVo),JSON.toJSONString(dataPage),e);
			return resultPage;
		}

	}

	/** 查询cms对象，设置正确的属性 */
	private void setCmtObject(List<Long> cmtObjectIdList,Map<String,CmsMsgResponse> cmtObjectMap,List<CmsMsgResponse> responseList) throws Exception {
		if (CollectionUtil.isEmpty(cmtObjectIdList)){
			return;
		}
		logger.info("-setCmtObject-待处理的cmtObjectIdList：{}", JSON.toJSONString(cmtObjectIdList));
		CmsContentParam param = new CmsContentParam();
		param.setIds(cmtObjectIdList);
		ModelResult<List<CmsContent>> listModelResult = cmsContentServiceClient.queryList(param);
		List<CmsContent> list = listModelResult.getModel();
		if (!listModelResult.isSuccess()){
			logger.info("根据ID集合查询CmsContent异常,msg:{}",listModelResult.getErrorMsg());
			return;
		}
		if (CollectionUtil.isNotEmpty(list)){
			List<NewsListVo> newsListVos = cmsContentManager.convertCmsToNews(list);
			Map<Long, NewsListVo> newListMap = newsListVos.stream().collect(Collectors.toMap(key -> key.getAricleId(), value -> value));

			for (Map.Entry<String, CmsMsgResponse> map : cmtObjectMap.entrySet()) {
				String key = map.getKey();
				CmsMsgResponse cmsMsgResponse = map.getValue();

				Long aricleId = Long.valueOf(key.substring(0,key.indexOf("_")));
				NewsListVo newsListVo = newListMap.get(aricleId);
				/* 用户 */
				cmsMsgResponse.setCmsObjectUserId(newsListVo.getIssueUserId());
				cmsMsgResponse.setCmsObjectUserName(newsListVo.getTag());
				/*文章*/
				cmsMsgResponse.setAricleId(newsListVo.getAricleId());
				cmsMsgResponse.setTitleImg(newsListVo.getTitleImg());
				cmsMsgResponse.setType(newsListVo.getType());
				cmsMsgResponse.setShortArticleList(newsListVo.getShortArticleList());
				cmsMsgResponse.setImageSrc(newsListVo.getImageSrc());
				cmsMsgResponse.setDetail(newsListVo.getDetail());
				cmtObjectIdList.remove(newsListVo.getAricleId());
				if (cmsMsgResponse.getType()==CmsCategoryType.video.getIndex()){
					cmsMsgResponse.setVideoSignId(UUID.randomUUID().toString());
				}
			}
		}
		logger.info("-setCmtObject-处理后的cmtObjectIdList：{}", JSON.toJSONString(cmtObjectIdList));
	}


}
