package com.esportzoo.esport.controller.user;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.connect.request.cms.CmsMsgRequest;
import com.esportzoo.esport.connect.response.CmsMsgResponse;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.user.UserMsgCacheConstant;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.UserMessageManager;
import com.esportzoo.esport.vo.cms.CmsMsgVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 消息相关接口
 * @author tingjun.wang
 * @date 2019/12/24 18:20下午
 */
@Controller
@RequestMapping("userMsg")
@Api(value = "用户消息", tags={"用户消息"})
public class UserMessageController extends BaseController{

	private transient Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserMessageManager userMessageManager;
	@Autowired
	RedisClient redisClient;

	@RequestMapping(value = "/cmsMsg", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "我的消息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<CmsMsgResponse>> cmsMsg(CmsMsgRequest cmsMsgRequest,HttpServletRequest request) {
		try {
			Long startTime =System.currentTimeMillis();
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null){
				return CommonResponse.withErrorResp("请先登录");
			}
			CmsMsgVo cmsMsgVo = new CmsMsgVo();
			cmsMsgVo.setObjectUserId(userConsumer.getId());
			cmsMsgVo.setClientType(cmsMsgRequest.getClientType());
			cmsMsgVo.setAgentId(cmsMsgRequest.getAgentId());
			cmsMsgVo.setBiz(cmsMsgRequest.getBiz());

			DataPage dataPage = new DataPage();
			Integer pageSize = cmsMsgRequest.getPageSize();
			if(pageSize > 20){
				pageSize = 20;
			}
			dataPage.setPageSize(pageSize);
			dataPage.setPageNo(cmsMsgRequest.getPageNo());

			DataPage<CmsMsgResponse> pageResult = userMessageManager.getCmsMsg(cmsMsgVo, dataPage,userConsumer);
			//清零用户未读消息数
			redisClient.set(UserMsgCacheConstant.USER_UNREAD_MESSAGE+userConsumer.getId(),"0");
			logger.info("cmsMsg所用时间：{}毫秒",System.currentTimeMillis()-startTime);
			return CommonResponse.withSuccessResp(pageResult);
		} catch (Exception e) {
			logger.info("我消息接口，发生异常，request：{}, exception={}", JSON.toJSON(request), e);
			return CommonResponse.withErrorResp("暂无记录");
		}
	}

}
