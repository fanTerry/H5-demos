package com.esportzoo.esport.controller.matchtool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.esport.client.service.tool.ToolMatchRoundServiceClient;
import com.esportzoo.esport.client.service.tool.ToolMatchServiceClient;
import com.esportzoo.esport.connect.request.matchtool.MatchToolHomeRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.tool.ToolMatchStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.matchtool.MatchToolManager;
import com.esportzoo.esport.vo.tool.ToolMatchQueryVo;
import com.esportzoo.esport.vo.tool.ToolMatchVo;

/**
 * 赛事工具首页相关controller
 * 
 * @author jing.wu
 * @version 创建时间：2019年11月18日 下午2:48:31
 */
@Controller
@RequestMapping("/matchtool")
@Api(value = "赛事工具相关接口", tags = { "赛事相关接口" })
public class MatchToolHomeController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String loggerPre = "赛事工具controller_";
	@Autowired
	private MatchToolManager matchToolManager;

	@Autowired
	private ToolMatchServiceClient toolMatchServiceClient;

	@Autowired
	private ToolMatchRoundServiceClient toolMatchRoundServiceClient;

	@ApiOperation(value = "查询首页赛事接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/homeMatch", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<JSONObject> queryHomeMatch(MatchToolHomeRequest homeRequest, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		logger.info("{}查询首页赛事接口,当前请求接口参数:{}", loggerPre, JSONObject.toJSONString(homeRequest));
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}查询首页赛事接口,未获取到登录用户信息", loggerPre);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			DataPage<ToolMatchVo> page = new DataPage<>();
			ToolMatchQueryVo queryVo = new ToolMatchQueryVo();
			if (homeRequest.getPageNo() == null || homeRequest.getPageSize() == null || homeRequest.getShowUserMacth() == null) {
				logger.error(loggerPre + "必要参数有误，homeRequest{}", JSON.toJSONString(homeRequest));
				return CommonResponse.withErrorResp("必要参数有误");
			}
			page.setPageNo(homeRequest.getPageNo());
			page.setPageSize(homeRequest.getPageSize());
			// 查询首页赛事信息，剔除无效赛事
			if (homeRequest.getStatus() == ToolMatchStatus.INVALID.getIndex()) {
				List<Integer> statusList = new ArrayList<>();
				statusList.add(ToolMatchStatus.NOTSTART.getIndex());
				statusList.add(ToolMatchStatus.RECRUITING.getIndex());
				statusList.add(ToolMatchStatus.INTHEGAME.getIndex());
				statusList.add(ToolMatchStatus.OVER.getIndex());
				statusList.add(ToolMatchStatus.OPENGAMEFAILED.getIndex());
				statusList.add(ToolMatchStatus.CLOSE.getIndex());
				queryVo.setStatusList(statusList);
			}
			PageResult<ToolMatchVo> pageResult = toolMatchServiceClient.queryHomeListPage(userConsumer.getId(), queryVo, page, homeRequest.getShowUserMacth());
			if (null == pageResult || !pageResult.isSuccess()) {
				return CommonResponse.withErrorResp("调用首页赛事接口异常");
			}
			jsonObject.put("homeResult", pageResult.getPage().getDataList());
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.info("{}查询首页赛事接口,发生异常,exception={}", loggerPre, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}


	@ApiOperation(value = "查询用户是否可以创建赛事接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/canCreateMatch", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<Boolean> queryCanCreateMatch(HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("{}查询用户是否可以创建赛事接口,未获取到登录用户信息", loggerPre);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			// 当前用户是否可以创建赛事信息
			Boolean createFlag = false;
			createFlag = matchToolManager.judgeCanCreateMatch(userConsumer.getId());
			if (null == createFlag ) {
				logger.info("{}调用首页赛事接口异常,当前用户:{}", loggerPre, userConsumer.getId());
				return CommonResponse.withErrorResp("调用首页赛事接口异常");
			}
			return CommonResponse.withSuccessResp(createFlag);
		}catch (Exception e) {
			logger.info("{}查询用户是否可以创建赛事接口,发生异常,exception={}", loggerPre, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
}
