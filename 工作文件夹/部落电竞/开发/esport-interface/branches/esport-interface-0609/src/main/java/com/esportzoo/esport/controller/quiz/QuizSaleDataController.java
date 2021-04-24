package com.esportzoo.esport.controller.quiz;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.wallet.UserWalletLogRecServiceClient;
import com.esportzoo.esport.connect.request.quiz.QuizSaleRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.operate.ClientChannelInviteCode;
import com.esportzoo.esport.manager.UserCenterManager;
import com.esportzoo.esport.util.DesUtil;
import com.esportzoo.esport.vo.wallet.MarketingStatistics;
import com.esportzoo.esport.vo.wallet.MarketingStatisticsVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description: 销售客户数据管理
 *
 * @author: Haitao.Li
 *
 * @create: 2020-04-23 16:47
 **/

@Controller
@RequestMapping("quizSale")
public class QuizSaleDataController extends BaseController {

	private static final transient Logger logger = LoggerFactory.getLogger(QuizSaleDataController.class);

	public static final String logPrefix = "销售注管理数据_";

	@Autowired
	private UserWalletLogRecServiceClient userWalletLogRecServiceClient;

	@Autowired
	UserCenterManager userCenterManager;


	@RequestMapping(value = "/personData", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查看销售管理数据", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查看销售管理数据", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<MarketingStatistics>> personDataPage(QuizSaleRequest quizSaleRequest, HttpServletRequest request) {


		UserConsumer userConsumer = getLoginUsr(request);
		logger.info(logPrefix + "用户：【{}】，参数param 【{}】", userConsumer.getId(), JSON.toJSONString(quizSaleRequest));
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			ClientChannelInviteCode userInviteInfo = userCenterManager.getUserInviteInfo(userConsumer.getId());
			if (userInviteInfo == null) {
				return CommonResponse.withResp("3333", "无查看权限");
			}

			if (quizSaleRequest.getSortType() == null) {
				quizSaleRequest.setSortType(1);
			}
			MarketingStatisticsVo statisticsVo = new MarketingStatisticsVo();
			DataPage<MarketingStatistics> page = new DataPage();
			page.setPageSize(quizSaleRequest.getPageSize());
			page.setPageNo(quizSaleRequest.getPageNo());
			statisticsVo.setInviteCode(userInviteInfo.getInviteCode());

			statisticsVo.setStartTime(DateUtil.getDateByString("2019-06-01 00:00:00"));
			sortByType(statisticsVo, quizSaleRequest);


			ModelResult<DataPage<MarketingStatistics>> result = userWalletLogRecServiceClient.querySaleStatisticsPage(statisticsVo, page);
			if (result != null && result.isSuccess() && result.getModel() != null) {
				DataPage<MarketingStatistics> dataPage = result.getModel();
				List<MarketingStatistics> marketingStatistics = dataPage.getDataList();
				for (MarketingStatistics marketingStatistic : marketingStatistics) {
					if (StringUtils.isNotBlank(marketingStatistic.getPhone())){
						marketingStatistic.setPhone(DesUtil.decryptStr(marketingStatistic.getPhone()));
					}
					marketingStatistic.setScoreSum(marketingStatistic.getScoreSum().add(marketingStatistic.getLossProfitAddScoreSum()));
				}
				dataPage.setDataList(marketingStatistics);
				return CommonResponse.withSuccessResp(dataPage);
			}
			logger.error(logPrefix + "用户：【{}】，错误信息 【{}】", userConsumer.getId(), result.getErrorMsg());
			return CommonResponse.withErrorResp(result.getErrorMsg());
		} catch (Exception e) {
			logger.error(logPrefix + "用户：【{}】，参数param 【{}】", userConsumer.getId(), JSON.toJSONString(quizSaleRequest));
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询数据异常");
		}

	}

	@RequestMapping(value = "/personDataSum", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查看销售管理数据汇总", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查看销售管理数据汇总", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<MarketingStatistics> personDataSum(QuizSaleRequest quizSaleRequest, HttpServletRequest request) {

		UserConsumer userConsumer = getLoginUsr(request);

		try {
			ClientChannelInviteCode userInviteInfo = userCenterManager.getUserInviteInfo(userConsumer.getId());
			if (userInviteInfo == null) {
				return CommonResponse.withResp("3333", "无查看权限");
			}
			MarketingStatisticsVo statisticsVo = new MarketingStatisticsVo();
			statisticsVo.setInviteCode(userInviteInfo.getInviteCode());
			statisticsVo.setStartTime(DateUtil.getDateByString("2019-06-01 00:00:00"));
			ModelResult<MarketingStatistics> modelResult = userWalletLogRecServiceClient.querySumSaleStatistics(statisticsVo);
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				MarketingStatistics marketingStatistics = modelResult.getModel();
				return CommonResponse.withSuccessResp(marketingStatistics);
			}
			logger.error(logPrefix + "用户：【{}】，错误信息 【{}】", userConsumer.getId(), modelResult.getErrorMsg());
			return CommonResponse.withErrorResp(modelResult.getErrorMsg());
		} catch (Exception e) {
			logger.error(logPrefix + "用户：【{}】,查看销售管理数据汇总", userConsumer.getId());
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询数据异常");
		}


	}

	/**
	 * 按照，注册时间，充值额，投注额，等维度的正或反序，排序
	 * @param statisticsVo
	 * @param quizSaleRequest
	 */
	private void sortByType(MarketingStatisticsVo statisticsVo, QuizSaleRequest quizSaleRequest) {
		//按照注册时间进行排序

		StringBuffer orderBy = new StringBuffer();


		if (quizSaleRequest.getSortType() == 1) {
			orderBy.append("register_time");
		} else if (quizSaleRequest.getSortType() == 2) {
			orderBy.append("score_sum + loss_profit_add_score_sum");
		} else if (quizSaleRequest.getSortType() == 3) {
			orderBy.append("bet_amount");
		}

		if (quizSaleRequest.getSortFlag()) {
			orderBy.append("  desc");
		} else {
			orderBy.append("  asc");
		}

		statisticsVo.setOrderBy(orderBy.toString());
	}


}
