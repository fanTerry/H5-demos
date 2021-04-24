package com.esportzoo.esport.controller.user;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizOrderServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizPlanServiceClient;
import com.esportzoo.esport.client.service.wallet.UserWalletRecServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.response.BaseResponse;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizUserInfoResponse;
import com.esportzoo.esport.constants.FeatureKey;
import com.esportzoo.esport.constants.quiz.QuizOrderStatus;
import com.esportzoo.esport.constants.quiz.QuizPlanWinStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserWalletRec;
import com.esportzoo.esport.domain.quiz.QuizOrder;
import com.esportzoo.esport.domain.quiz.QuizPlan;
import com.esportzoo.esport.manager.UserManager;
import com.esportzoo.esport.vo.quiz.QuizOrderQueryVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 查询用户星星数量
 * @Author lixisheng
 * @Date 2019/10/19 16:24
 */
@Controller
@RequestMapping("starNum")
public class StarNumController extends BaseController {

    private transient final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String logPrefix = "查询用户星星的相关接口-";

    @Autowired
    private UserWalletRecServiceClient userWalletRecServiceClient;

    @Autowired
    private UserManager userManager;
    /**
     * 查询用户星星数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryStarNum", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse queryStarNum(HttpServletRequest request, BaseRequest baseRequest) {
        QuizUserInfoResponse quizUserInfoResponse = new QuizUserInfoResponse();
        UserConsumer userConsumer = getLoginUsr(request);
        try {
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("用户未登录");
            }
            ModelResult<UserWalletRec> userWalletRecModelResult = userWalletRecServiceClient.queryWalletByUserId(userConsumer.getId());
            if (userWalletRecModelResult == null || !userWalletRecModelResult.isSuccess() || userWalletRecModelResult.getModel() == null) {
                logger.info(logPrefix + "用户id={},获取查询用户星星数量异常:{}", userConsumer.getId(), JSONObject.toJSONString(userWalletRecModelResult));
                return CommonResponse.withErrorResp("获取查询用户星星数量异常");
            }
            UserWalletRec userWalletRec = userWalletRecModelResult.getModel();
            quizUserInfoResponse.setRecScore(userWalletRec.getRecScore());
            quizUserInfoResponse.setGiftRecScore(userWalletRec.getGiftRecScore());
            quizUserInfoResponse.setAbleRecScore(userWalletRec.getAbleRecScore());
            //更新用户昵称和头像缓存
            quizUserInfoResponse.setNickName(userConsumer.getNickName());
            quizUserInfoResponse.setIcon(userConsumer.getIcon());
            return CommonResponse.withSuccessResp(quizUserInfoResponse);

        } catch (Exception e) {
            logger.error("查询用户星星出现异常，用户id:{},异常信息:{}", userConsumer.getId(), e.getMessage());
            return CommonResponse.withErrorResp("查询用户星星失败");
        }
    }

    @RequestMapping(value = "/queryWinPrize", method = RequestMethod.POST)
    @ResponseBody
    public CommonResponse queryWinPrizeByLoginTime(HttpServletRequest request, BaseRequest baseRequest){
         String prefix="查询用户当期时间与上一次登录时间之间中奖信息_";
        QuizUserInfoResponse quizUserInfoResponse=new QuizUserInfoResponse();
        try {
            UserConsumer userConsumer = getLoginUsr(request);
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("用户未登录");
            }
             quizUserInfoResponse = userManager.queryWinPrizeByLoginTime(userConsumer, baseRequest.getAgentId().intValue());
        } catch (Exception e) {
            logger.info(prefix+"发生异常[{}]", e.getMessage(),e);
        }

        return CommonResponse.withSuccessResp(quizUserInfoResponse);
    }


}
