package com.esportzoo.esport.manager.expert;

import java.util.List;

import com.esportzoo.quiz.constants.QuizMatchStatus;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cn.hutool.core.bean.BeanUtil;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.expert.RecExpertColumnArticleServiceClient;
import com.esportzoo.esport.client.service.expert.RecOrderServiceClient;
import com.esportzoo.esport.connect.response.expert.ArticleDetailResponse;
import com.esportzoo.esport.connect.response.expert.MatchVo;
import com.esportzoo.esport.constants.ArticleFreeType;
import com.esportzoo.esport.constants.ArticlePayStatus;
import com.esportzoo.esport.constants.UserOperationParam;
import com.esportzoo.esport.domain.RecExpertColumnArticle;
import com.esportzoo.esport.manager.match.MatchManager;
import com.esportzoo.esport.vo.expert.ExpertArticleContent;
import com.esportzoo.esport.vo.expert.ExpertArticlePayParam;
import com.esportzoo.esport.vo.expert.ExpertArticlePayResult;
import com.esportzoo.esport.vo.expert.RecOrderQueryVo;
import com.esportzoo.leaguelib.common.constants.MatchStatus;

/**
 * 专家文章相关manager
 * @author: jing.wu
 * @date:2019年5月11日下午3:50:34
 */
@Component
public class ExpertArticleManager {

	private transient static final Logger logger = LoggerFactory.getLogger(ExpertArticleManager.class);

	@Autowired
	@Qualifier("recOrderServiceClient")
	private RecOrderServiceClient recOrderServiceClient;
	@Autowired
	private RecExpertColumnArticleServiceClient recExpertColumnArticleServiceClient;
	@Autowired
	private MatchManager matchManager;

	public ModelResult<ExpertArticlePayResult> toPay(ExpertArticlePayParam param, UserOperationParam userOperationParam) {
		return recOrderServiceClient.payArticle(param, userOperationParam);
	}
	
	public boolean isPay(Long userId, Long articleId) {
		RecOrderQueryVo vo = new RecOrderQueryVo();
		vo.setUserId(userId);
		vo.setColumnArticleId(articleId);
		vo.setPayStatus(ArticlePayStatus.PAY_SUCCESS.getIndex());
		ModelResult<Integer> modelResult = recOrderServiceClient.queryCount(vo);
		if (!modelResult.isSuccess()) {
			return false;
		}
		Integer count = modelResult.getModel();
		if (count!=null && count.intValue()>0) {
			return true;
		}
		return false;
	}
	
	public boolean isAllMatchFinish(List<MatchVo> matchList) {
		boolean res = false;
		int count = 0;
		if(matchList.isEmpty()){
			return res;
		}
		for (MatchVo matchVo : matchList) {
			if (matchVo.getStatus().intValue() == QuizMatchStatus.OVER.getIndex()) {
				count++;
			}
		}
		if (count == matchList.size()) {
			res = true;
		}
		return res;
	}
	
	
	public RecExpertColumnArticle getByArticleId(Long articleId) {
		ModelResult<RecExpertColumnArticle> modelResult = recExpertColumnArticleServiceClient.queryById(articleId);
		if (!modelResult.isSuccess()) {
			logger.info("查询文章接口返回错误errMsg={},articleId={]", modelResult.getErrorMsg(), articleId);
			return null;
		}
		return modelResult.getModel();
	}
	
	public ArticleDetailResponse converToArticleDetail(RecExpertColumnArticle article,boolean isUserPay ) {
		try {
			if (article==null) {
				return null;
			}
			ArticleDetailResponse detail = new ArticleDetailResponse();
			BeanUtil.copyProperties(article,detail );
			detail.setFreeFlag(article.getIsFree().intValue() == ArticleFreeType.FREE.getIndex());
			detail.setPublishTime(DateUtil.dateToString(article.getCreateTime().getTime(), "yyyy-MM-dd HH:mm"));
			detail.setMatchList(matchManager.getMatchVoListByMatchStr(article.getMatchIdList()));
			if(isAllMatchFinish(detail.getMatchList())){//已完成的可以查看
				detail.setCanView(true);
			}else {
				detail.setCanView(isUserPay);
			}
			if (detail.isCanView()){
				detail.setContent(this.convertToExpertArticleContent(article.getContent()));
			}
			return detail;
		} catch (Exception e) {
			logger.info("转换成文章详情出错，exception={}", e.getMessage(), e);
			return null;
		}
		
	}
	
	public ExpertArticleContent convertToExpertArticleContent(String content) {
		if (StringUtils.isBlank(content)) {
			return null;
		}
		return JSONObject.parseObject(content, ExpertArticleContent.class);
	}


}
