package com.esportzoo.esport.controller.hd;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.quiz.QuizOrderServiceClient;
import com.esportzoo.esport.connect.request.hd.QuizWinTopRequset;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.hd.Hd105GiftResponse;
import com.esportzoo.esport.connect.response.hd.Hd105GiftResult;
import com.esportzoo.esport.connect.response.hd.Hd105Response;
import com.esportzoo.esport.connect.response.hd.Hd105Result;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.quiz.DateType;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.hd.constants.HdCode;
import com.esportzoo.esport.hd.constants.HdEnable;
import com.esportzoo.esport.hd.constants.HdGiftStatus;
import com.esportzoo.esport.hd.entity.HdGift;
import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.entity.HdUserGift;
import com.esportzoo.esport.hd.gift.HdGiftServiceClient;
import com.esportzoo.esport.hd.gift.HdUserGiftServiceClient;
import com.esportzoo.esport.hd.info.HdInfoServiceClient;
import com.esportzoo.esport.util.DatesUtil;
import com.esportzoo.esport.vo.quiz.QuizWinTopResult;
import com.esportzoo.esport.vo.quiz.QuizWinTopVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author lixisheng
 * @Date 2020/4/17 17:51
 */
@Controller
@RequestMapping("quizTop")
@Api(value = "竞猜排行榜相关接口", tags = {"竞猜排行榜相关接口"})
public class Hd105Controller extends BaseController {

    private transient final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String logPrefix = "排行榜活动[105]相关接口-";

    //本周排行榜
    private final String WINTOP_WEEK_RESULT = "esport_wintop105_week_result_";
    //上一周排行榜
    private final String WINTOP_LASTWEEK_RESULT = "esport_wintop105_lastweek_result_";
    //本周礼品列表
    private final String HD105_GIFTLIST = "hd105_giftlist";
    //上周礼品列表
    private final String HD105_LASTWEEK_GIFTLIST = "hd105_lastweek_giftlist";
    //查询排行榜前几名
    private final Integer top = 30;

    @Autowired
    @Qualifier("hdInfoServiceClient")
    HdInfoServiceClient hdInfoServiceClient;

    @Autowired
    @Qualifier("hdGiftServiceClient")
    HdGiftServiceClient hdGiftServiceClient;

    @Autowired
    private QuizOrderServiceClient quizOrderServiceClient;
    @Autowired
    @Qualifier("hdUserGiftServiceClient")
    private HdUserGiftServiceClient hdUserGiftServiceClient;

    @Autowired
    private RedisClient redisClient;

    /**
     * 查询排行榜单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/winTop", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "查询排行榜", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "查询排行榜", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<Hd105Result> queryQuizWinTop(HttpServletRequest request, QuizWinTopRequset quizWinTopRequset) {
        UserConsumer userConsumer = getLoginUsr(request);
        try {
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("用户未登录");
            }
            QuizWinTopVo quizWinTopVo = new QuizWinTopVo();
            Hd105Result hd105Result = new Hd105Result();
            List<Hd105Response> hd105ResponsesList = new ArrayList<>();
            if (quizWinTopRequset.getDateType().equals(DateType.THIS_WEEK.getIndex())) {//本周排行榜
                quizWinTopVo.setStartTime(DatesUtil.getBeginDayOfWeek());//本周开始时间
                quizWinTopVo.setEndTime(DatesUtil.getEndDayOfWeek());//本周结束时间
                if (!redisClient.exists(WINTOP_WEEK_RESULT)) {
                    ModelResult<List<QuizWinTopResult>> listModelResult = quizOrderServiceClient.queryQuizWinTopList(quizWinTopVo);
                    if (listModelResult.isSuccess() && CollectionUtil.isNotEmpty(listModelResult.getModel())) {
                        //设置排行榜礼品
                        hd105ResponsesList = queryRankList(listModelResult.getModel(), quizWinTopRequset.getDateType());
                        if (CollectionUtil.isNotEmpty(hd105ResponsesList)) {
                            logger.info("{}查询本周排行榜，加入缓存key:{}", logPrefix, WINTOP_WEEK_RESULT);
                            redisClient.setObj(WINTOP_WEEK_RESULT, 3 * 60, hd105ResponsesList);
                        }
                    }
                } else {
                    hd105ResponsesList = redisClient.getObj(WINTOP_WEEK_RESULT);
                }
                //查询个人排行榜信息
                hd105Result = queryUserRank(hd105ResponsesList, userConsumer);
                hd105Result.setWinTop(hd105ResponsesList);
                hd105Result.setEndTime(DatesUtil.getEndDayOfWeek());
            } else {//上周排行榜
                quizWinTopVo.setStartTime(DatesUtil.getBeginDayOfLastWeek());
                quizWinTopVo.setEndTime(DatesUtil.getEndDayOfLastWeek());
                if (!redisClient.exists(WINTOP_LASTWEEK_RESULT)) {
                    ModelResult<List<QuizWinTopResult>> listModelResult = quizOrderServiceClient.queryQuizWinTopList(quizWinTopVo);
                    if (listModelResult.isSuccess() && CollectionUtil.isNotEmpty(listModelResult.getModel())) {
                        //设置排行榜礼品
                        hd105ResponsesList = queryRankList(listModelResult.getModel(), quizWinTopRequset.getDateType());
                        if (CollectionUtil.isNotEmpty(hd105ResponsesList)) {
                            logger.info("{}查询上周排行榜，加入缓存key:{}", logPrefix, WINTOP_LASTWEEK_RESULT);
                            redisClient.setObj(WINTOP_LASTWEEK_RESULT, 10 * 60 * 60, hd105ResponsesList);
                        }
                    }
                } else {
                    hd105ResponsesList = redisClient.getObj(WINTOP_LASTWEEK_RESULT);
                }
                //查询个人排行榜信息
                hd105Result = queryUserRank(hd105ResponsesList, userConsumer);
                hd105Result.setWinTop(hd105ResponsesList);
            }
            return CommonResponse.withSuccessResp(hd105Result);
        } catch (Exception e) {
            logger.error(logPrefix + "查询排行榜出现异常，用户id:{}", userConsumer.getId(), e);
            return CommonResponse.withErrorResp("查询排行榜失败");
        }
    }


    /**
     * 查询排行榜单礼品
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/winTopGift", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "查询排行榜礼品", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "查询排行榜礼品", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<Hd105GiftResult> queryQuizWinTopGift(HttpServletRequest request) {
        UserConsumer userConsumer = getLoginUsr(request);
        if (userConsumer == null) {
            return CommonResponse.withErrorResp("用户未登录");
        }
        try {
            Hd105GiftResult hd105GiftResult = getGift();
            return CommonResponse.withSuccessResp(hd105GiftResult);
        } catch (Exception e) {
            logger.info(logPrefix + "进入礼物列表，用户id：{}", userConsumer.getId(), e);
            return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE, ResponseConstant.SYSTEM_ERROR_MESG);
        }
    }

    private Hd105GiftResult getGift() {
        String logPre = "查询排行榜礼物列表_";
        Hd105GiftResult hd105GiftResult = new Hd105GiftResult();
        try {
            if (!redisClient.exists(HD105_GIFTLIST)) {
                List<Hd105GiftResponse> giftList = new ArrayList<>();
                ModelResult<HdInfo> hdInfoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_105.getIndex());
                HdInfo hdInfo = hdInfoModelResult.getModel();
                List<HdGift> hdGiftsList = hdInfo.getGiftList();
                Date endTime = hdInfo.getEndTime();
                if (hdInfoModelResult.isSuccess() && CollectionUtil.isNotEmpty(hdGiftsList)
                        && hdInfo.getStatus().equals(HdEnable.TRUE.getIndex()) && DateUtil.calcCompareDate3(endTime, new Date()) > 0) {
                    //剔除无效数据，按金额降序排序
                    List<HdGift> lists = hdGiftsList.stream()
                            .filter(s -> (s.getGiftPropJo() != null && s.getStatus().equals(HdGiftStatus.valid.getIndex())))
                            .sorted(Comparator.comparing(HdGift::getAmount).reversed()).collect(Collectors.toList());
                    logger.info(logPre + "过滤后的结果：lists{} " + JSONObject.toJSONString(lists.size()));
                    if (CollectionUtil.isNotEmpty(lists)) {
                        // 礼品信息
                        Integer top = lists.get(lists.size() - 1).getGiftPropJo().getInteger("top");
                        hd105GiftResult.setWinTopNum(top);
                        for (HdGift hdGift : lists) {
                            Hd105GiftResponse hd105GiftResponse = new Hd105GiftResponse();
                            BeanUtil.copyProperties(hdGift, hd105GiftResponse);
                            hd105GiftResponse.setTopRank(hdGift.getGiftPropJo().getString("topRank"));
                            hd105GiftResponse.setTopNum(hdGift.getGiftPropJo().getInteger("top"));
                            giftList.add(hd105GiftResponse);
                        }
                        hd105GiftResult.setHd105GiftResponses(giftList);
                    }
                }
                hd105GiftResult.setEndTime(hdInfo.getEndTime());
                hd105GiftResult.setStatus(hdInfo.getStatus());
                hd105GiftResult.setHdId(hdInfo.getId().longValue());
                redisClient.setObj(HD105_GIFTLIST, 3 * 60, hd105GiftResult);
                logger.info(logPre + "加入缓存key:{}", HD105_GIFTLIST);
                logger.info(logPre + "giftList{} " + JSONObject.toJSONString(giftList.size()));
            } else { //取缓存
                logger.info(logPre + "取缓存key:{}", HD105_GIFTLIST);
                hd105GiftResult = redisClient.getObj(HD105_GIFTLIST);
            }
            return hd105GiftResult;
        } catch (Exception e) {
            logger.info(logPre + "活动状态异常，礼物列表信息出现异常【{}】", e.getMessage(), e);
            return null;
        }
    }

    private Hd105GiftResponse getUserWeekGift(Hd105GiftResult hd105GiftResult, Integer usrPosition) {
        Hd105GiftResponse usrHdGift = null;
        List<Hd105GiftResponse> lists = hd105GiftResult.getHd105GiftResponses();
        if (CollectionUtil.isEmpty(lists)) {
            return usrHdGift;
        }
        for (Hd105GiftResponse hdGift : lists) {
            String[] strings = hdGift.getTopRank().split(";");
            if (Arrays.asList(strings).contains(String.valueOf(usrPosition))) {
                return hdGift;
            }
        }
        if (usrHdGift == null) {
            /** 无匹配到用户礼品 */
            logger.error("无法匹配到对应的用户礼品");
        }
        return usrHdGift;
    }

    private Hd105GiftResponse getUserLastWeekGift(Long hdId, Integer userId) {
        Hd105GiftResponse usrHdGift = new Hd105GiftResponse();
        Date beginDayOfWeek = DatesUtil.getBeginDayOfWeek();
        Date endDayOfWeek = DatesUtil.getEndDayOfWeek();
        ModelResult<List<HdUserGift>> userGiftByCondition = hdUserGiftServiceClient.queryHdUserGiftByCondition(userId.longValue(), hdId,
                beginDayOfWeek, endDayOfWeek);
        if (userGiftByCondition.isSuccess() && CollectionUtil.isNotEmpty(userGiftByCondition.getModel())) {
            HdUserGift hdUserGift = userGiftByCondition.getModel().get(0);
            usrHdGift.setHdId(hdUserGift.getHdId().intValue());
            usrHdGift.setGiftName(hdUserGift.getGiftName());
            usrHdGift.setGiftType(hdUserGift.getGiftType());
            usrHdGift.setId(hdUserGift.getGiftId().intValue());
            JSONObject jsonObject = JSONObject.parseObject(hdUserGift.getGiftProp());
            Integer topNum = jsonObject.getInteger("top");
            usrHdGift.setTopNum(topNum);
        }
        return usrHdGift;
    }

    /**
     * 获取排行榜信息
     */
    private Hd105Result queryRank(List<QuizWinTopResult> topNRanks, Hd105Result hd105Result, UserConsumer userConsumer, Integer dateType) {
        List<Hd105Response> hd105ResponseList = new ArrayList<>();
        Hd105GiftResult hd105GiftResult = getGift();
        //添加礼品信息，前n名
        for (int i = 0; i < topNRanks.size(); i++) {
            QuizWinTopResult quizRank = topNRanks.get(i);
            Hd105Response hd105Response = new Hd105Response();
            BeanUtil.copyProperties(quizRank, hd105Response, CopyOptions.create().ignoreNullValue());
            if (dateType.equals(DateType.THIS_WEEK.getIndex())) { //本周
                if (i < hd105GiftResult.getWinTopNum()) {
                    Hd105GiftResponse userWeekGift = getUserWeekGift(hd105GiftResult, i + 1);
                    hd105Response.setHd105GiftResponse(userWeekGift);
                }
                hd105Result.setEndTime(DatesUtil.getEndDayOfWeek());
            } else { //上周
                Hd105GiftResponse usrHdGift = getUserLastWeekGift(hd105GiftResult.getHdId(), quizRank.getUserId().intValue());
                hd105Response.setHd105GiftResponse(usrHdGift);
            }
            //查自己的,先查排行榜中是否有数据
            if (hd105Response.getUserId().equals(userConsumer.getId())) {
                hd105Result.setRank(i + 1);
                BeanUtil.copyProperties(hd105Response, hd105Result, CopyOptions.create().ignoreNullValue());
            }
            hd105ResponseList.add(hd105Response);
        }
        //排行榜没有用户时
        if (hd105Result.getRank() == null) {
            hd105Result.setUserId(userConsumer.getId());
            hd105Result.setNickName(userConsumer.getNickName());
            if (StringUtils.isNotBlank(userConsumer.getIcon())) {
                hd105Result.setUserIcon(userConsumer.getIcon());
            }
        }
        hd105Result.setWinTop(hd105ResponseList);
        return hd105Result;
    }

    /**
     * 设置排行榜礼品
     */
    private List<Hd105Response> queryRankList(List<QuizWinTopResult> topNRanks, Integer dateType) {
        List<Hd105Response> hd105ResponseList = new ArrayList<>();
        if (CollectionUtil.isEmpty(topNRanks)) {
            logger.info("排行榜信息为空");
            return hd105ResponseList;
        }
        Hd105GiftResult hd105GiftResult = getGift();
        //添加礼品信息，前n名
        for (int i = 0; i < topNRanks.size(); i++) {
            QuizWinTopResult quizRank = topNRanks.get(i);
            Hd105Response hd105Response = new Hd105Response();
            BeanUtil.copyProperties(quizRank, hd105Response, CopyOptions.create().ignoreNullValue());
            if (dateType.equals(DateType.THIS_WEEK.getIndex())) { //本周
                if (i < hd105GiftResult.getWinTopNum()) {
                    Hd105GiftResponse userWeekGift = getUserWeekGift(hd105GiftResult, i + 1);
                    hd105Response.setHd105GiftResponse(userWeekGift);
                }
            } else { //上周
                Hd105GiftResponse usrHdGift = getUserLastWeekGift(hd105GiftResult.getHdId(), quizRank.getUserId().intValue());
                hd105Response.setHd105GiftResponse(usrHdGift);
            }
            hd105ResponseList.add(hd105Response);
        }
        return hd105ResponseList;
    }


    /**
     * 查个人排行榜情况
     */
    private Hd105Result queryUserRank(List<Hd105Response> hd105ResponseList, UserConsumer userConsumer) {
        Hd105Result hd105Result = new Hd105Result();
        Hd105Response hd105Response;
        if (CollectionUtil.isEmpty(hd105ResponseList)) {
            logger.info("排行榜信息为空");
            return hd105Result;
        }
        for (int i = 0; i < hd105ResponseList.size(); i++) {
            hd105Response = hd105ResponseList.get(i);
            //查排行榜中是否有数据
            if (hd105Response.getUserId().equals(userConsumer.getId())) {
                hd105Result.setRank(i + 1);
                BeanUtil.copyProperties(hd105Response, hd105Result, CopyOptions.create().ignoreNullValue());
            }
        }
        //排行榜没有用户时
        if (hd105Result.getRank() == null) {
            hd105Result.setUserId(userConsumer.getId());
            hd105Result.setNickName(userConsumer.getNickName());
            if (StringUtils.isNotBlank(userConsumer.getIcon())) {
                hd105Result.setUserIcon(userConsumer.getIcon());
            }
        }
        return hd105Result;
    }

}
