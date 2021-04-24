//package com.esportzoo.esport.controller;
//
//import com.alibaba.fastjson.JSONObject;
//import com.esportzoo.common.appmodel.domain.result.ModelResult;
//import com.esportzoo.esport.client.service.game.AuthorizePartnerServiceClient;
//import com.esportzoo.esport.client.service.game.GameItemServiceClient;
//import com.esportzoo.esport.client.service.game.GameProviderServiceClient;
//import com.esportzoo.esport.connect.request.BaseRequest;
//import com.esportzoo.esport.connect.response.CommonResponse;
//import com.esportzoo.esport.connect.response.game.GameAutoLoginResponse;
//import com.esportzoo.esport.constant.ResponseConstant;
//import com.esportzoo.esport.constants.CommonStatus;
//import com.esportzoo.esport.constants.game.GameStatus;
//import com.esportzoo.esport.domain.UserConsumer;
//import com.esportzoo.esport.domain.game.GameItem;
//import com.esportzoo.esport.domain.game.GameProvider;
//import com.esportzoo.quiz.service.exception.BusinessException;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiResponse;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 游戏自动授权登陆
// * @Author zheng.lin
// * @Date 2020/4/16 17:15
// */
//@Controller
//public class GameLoginController extends BaseController {
//
//    private transient final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Autowired
//    private AuthorizePartnerServiceClient authorizePartnerServiceClient;
//
//    @Autowired
//    private GameProviderServiceClient gameProviderServiceClient;
//
//    @Autowired
//    private GameItemServiceClient gameItemServiceClient;
//
//    private static final String logprefix = "请求/game/autoLogin接口,";
//    private static final String v2Logprefix = "请求/v2/game/autoLogin接口,";
//
//    @RequestMapping(value = "/v2/game/autoLogin", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
//    @ApiOperation(value = "游戏自动授权登陆地址接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
//    @ApiResponse(code = 200, message = "游戏自动授权登陆地址POST", response = CommonResponse.class)
//    @ResponseBody
//    public CommonResponse<GameAutoLoginResponse> gameAutoLoginV2(@RequestParam String gameNo, BaseRequest baseRequest, HttpServletRequest request) {
//        logger.info("{}请求参数gameNo={},baseRequest={}",v2Logprefix, gameNo, JSONObject.toJSONString(baseRequest));
//        CommonResponse<GameAutoLoginResponse> autoLoginResponse = CommonResponse.newCommonResponse(ResponseConstant.RESP_SUCC_CODE, ResponseConstant.RESP_SUCC_MESG);
//        try {
//            GameItem gameItem = autoLoginValidateGameItem(gameNo);
//            GameProvider gameProvider = autoLoginValidateGameProvider(gameItem.getProviderId());
//            final String providerNo= gameProvider.getProviderNo();
//            UserConsumer userConsumer = getLoginUsr(request);
//            //生成授权码
//            ModelResult<String> authCodeResult = authorizePartnerServiceClient.getAuthorizationCode(providerNo, userConsumer.getId());
//            if (!authCodeResult.isSuccess() || StringUtils.isBlank(authCodeResult.getModel())){
//                logger.info("{},请求参数gameNo={},providerNo={},授权码生成错误",v2Logprefix, gameNo, providerNo);
//                return CommonResponse.withResp(ResponseConstant.AUTHCODE_GENERATE_ERROR_CODE, ResponseConstant.AUTHCODE_GENERATE_ERROR_MESG);
//            }
//            //组装游戏跳转地址，带上授权码code和渠道号
//            StringBuilder urlStrBuilder = new StringBuilder(gameItem.getGameUrl());
//            urlStrBuilder.append("?code=").append(authCodeResult.getModel())
//                    .append("&channel_no=").append(baseRequest.getAgentId());
//            String url = urlStrBuilder.toString();
//            logger.info("{},请求参数gameNo={},providerNo={},游戏跳转地址={}",v2Logprefix, gameNo, providerNo, url);
//            autoLoginResponse.setData(new GameAutoLoginResponse(url));
//        } catch (BusinessException be) {
//            logger.info("{}异常:{}-{}:",v2Logprefix,be.getCode(),be.getMessage());
//            autoLoginResponse.setCode(be.getCode());
//            autoLoginResponse.setMessage(be.getMessage());
//        } catch (Exception e) {
//            logger.info("{}异常:", v2Logprefix,e);
//            autoLoginResponse.setCode(ResponseConstant.RESP_ERROR_CODE);
//            autoLoginResponse.setMessage(ResponseConstant.RESP_ERROR_MESG);
//        }
//        return autoLoginResponse;
//    }
//
//    @RequestMapping(value = "/game/autoLogin", method = RequestMethod.GET)
//    public ModelAndView gameAutoLogin(@RequestParam String gameNo, BaseRequest baseRequest,
//                                        HttpServletRequest request, HttpServletResponse response) {
//        logger.info("{},请求参数gameNo={},baseRequest={}",logprefix, gameNo, JSONObject.toJSONString(baseRequest));
//        ModelAndView mav = new ModelAndView("common/error");
//        try {
//            GameItem gameItem = autoLoginValidateGameItem(gameNo);
//            GameProvider gameProvider = autoLoginValidateGameProvider(gameItem.getProviderId());
//            final String providerNo= gameProvider.getProviderNo();
//            UserConsumer userConsumer = getLoginUsr(request);
//            if (null == userConsumer) {
//                //为空
//                logger.info("{},用户未登陆",logprefix);
//                response.sendRedirect("http://daily-m.esportzoo.cn/login");
//                return null;
//            }
//            logger.info("{},当前登陆用户：{}-{}-{}",logprefix,userConsumer.getId(),userConsumer.getNickName(),userConsumer.getOpenId());
//            //生成授权码
//            ModelResult<String> authCodeResult = authorizePartnerServiceClient.getAuthorizationCode(providerNo, userConsumer.getId());
//            if (!authCodeResult.isSuccess() || StringUtils.isBlank(authCodeResult.getModel())){
//                logger.info("{},请求参数gameNo={},providerNo={},授权码生成错误", logprefix,gameNo, providerNo);
//                return mav;
//            }
//            //组装游戏跳转地址，带上授权码code和渠道号
//            StringBuilder urlStrBuilder = new StringBuilder(gameItem.getGameUrl());
//            urlStrBuilder.append("?code=").append(authCodeResult.getModel())
//                    .append("&channel_no=").append(baseRequest.getAgentId());
//            String url = urlStrBuilder.toString();
//            logger.info("{},请求参数gameNo={},providerNo={},游戏跳转地址={}",logprefix, gameNo, providerNo, url);
//            response.sendRedirect(url);
//            return null;
//        } catch (BusinessException be) {
//            logger.info("{}异常:{}-{}:",logprefix, be.getCode(),be.getMessage());
//        } catch (Exception e) {
//            logger.info("{}异常:",logprefix, e);
//        }
//        return mav;
//    }
//
//    /**
//     * 自动登陆参数校验游戏
//     * @param gameNo 游戏编号
//     * @return
//     */
//    private GameItem autoLoginValidateGameItem(String gameNo){
//        if (StringUtils.isBlank(gameNo)) {
//            throw new BusinessException("providerNo或gameNo参数为空");
//        }
//        ModelResult<GameItem> gameResult = gameItemServiceClient.queryByGameNo(gameNo);
//        GameItem game = gameResult.getModel();
//        if (gameResult.isSuccess() && (null != game)) {
//            if (game.getStatus().intValue() != GameStatus.PUBLISH.getIndex()) {
//                logger.info("请求/game/autoLogin接口,请求参数gameNo={},游戏未发布",gameNo);
//                throw new BusinessException(ResponseConstant.GAME_STATUS_INVALID_CODE, ResponseConstant.GAME_STATUS_INVALID_MESG);
//            }
//        } else {
//            logger.info("请求/game/autoLogin接口,请求参数gameNo={},游戏不存在", gameNo);
//            throw new BusinessException(ResponseConstant.GAME_NOT_EXIST_CODE, ResponseConstant.GAME_NOT_EXIST_MESG);
//        }
//        return game;
//    }
//
//    /**
//     * 自动登陆参数校验游戏供应商
//     * @param gameProviderId
//     * @return
//     */
//    private GameProvider autoLoginValidateGameProvider(Integer gameProviderId){
//        final Long providerId = Long.valueOf(gameProviderId);
//        ModelResult<GameProvider> providerResult = gameProviderServiceClient.queryById(providerId);
//        GameProvider gameProvider = providerResult.getModel();
//        if (providerResult.isSuccess() && (null != gameProvider)) {
//            if (gameProvider.getStatus().intValue() != CommonStatus.EFFECTIVE.getIndex()) {
//                logger.info("请求/game/autoLogin接口,请求参数providerNo={},游戏商状态无效", gameProvider.getProviderNo());
//                throw new BusinessException(ResponseConstant.PROVIDER_STATUS_INVALID_CODE, ResponseConstant.PROVIDER_STATUS_INVALID_MESG);
//            }
//        } else {
//            logger.info("请求/game/autoLogin接口,请求参数providerNo={},游戏商不存在", gameProvider.getProviderNo());
//            throw new BusinessException(ResponseConstant.PROVIDER_NOT_EXIST_CODE, ResponseConstant.PROVIDER_NOT_EXIST_MESG);
//        }
//        return gameProvider;
//    }
//
//}
