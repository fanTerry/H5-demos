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
import com.esportzoo.esport.connect.response.hd.*;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.quiz.DateType;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.hd.constants.HdCode;
import com.esportzoo.esport.hd.constants.HdGiftStatus;
import com.esportzoo.esport.hd.entity.HdGift;
import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.entity.HdUserGift;
import com.esportzoo.esport.hd.gift.HdGiftServiceClient;
import com.esportzoo.esport.hd.gift.HdUserGiftServiceClient;
import com.esportzoo.esport.hd.info.HdInfoServiceClient;
import com.esportzoo.esport.manager.hd.HdCommonManager;
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
    //本周缓存时间
    private final Integer WINTOP_WEEK_OVERTIME = 3 * 60;
    //上一周缓存时间
    private final Integer WINTOP_LASTWEEK_OVERTIME = 10 * 60 * 60;
    //本周礼品列表
    private final String HD105_GIFTLIST = "hd105_giftlist";
    //用户一周内中奖标记
    private final String HD105_WIN_GIFT = "hd105_win_gift_";


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
    private HdCommonManager hdCommonManager;


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
        long startTime = System.currentTimeMillis();
        try {
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("用户未登录");
            }
            //查询活动信息
            Hd105GiftResult hd105GiftResult = new Hd105GiftResult();
            QuizWinTopVo quizWinTopVo = new QuizWinTopVo();
            Hd105Result hd105Result = new Hd105Result();
            if (!redisClient.exists(HD105_GIFTLIST)) {
                CommonResponse<HdInfo> hd105Info = hdCommonManager.getHdBaseInfo(HdCode.HD_105.getIndex());
                if (null != hd105Info && hd105Info.getData() != null && StringUtils.isNotBlank(hd105Info.getCode()) && hd105Info.getCode().equals(ResponseConstant.RESP_SUCC_CODE)) {
                    HdInfo hd105InfoData = hd105Info.getData();
                    getHd105Info(hd105InfoData, hd105GiftResult);
                } else if (hd105Info.getCode() == ResponseConstant.HD_IS_NULL_CODE || hd105Info.getCode() == ResponseConstant.HD_NOT_ENABLE_ERROR_CODE
                        || hd105Info.getCode() == ResponseConstant.HD_NOT_START_ERROR_CODE || hd105Info.getCode() == ResponseConstant.HD_HAS_END_ERROR_CODE) {
                    return CommonResponse.withResp(hd105Info.getCode(), hd105Info.getMessage());
                } else {
                    logger.info(logPrefix + "活动【{}】状态异常:{}", HdCode.HD_105.getIndex(), JSONObject.toJSONString(hd105Info));
                    return CommonResponse.withErrorResp("活动105状态异常");
                }
            } else { //取缓存
                logger.info(logPrefix + "活动信息取缓存key:{}", HD105_GIFTLIST);
                hd105GiftResult = redisClient.getObj(HD105_GIFTLIST);
            }
            //查询排行榜信息
            List<Hd105Response> hd105ResponsesList = new ArrayList<>();
            if (quizWinTopRequset.getDateType().equals(DateType.THIS_WEEK.getIndex())) {//本周排行榜
                quizWinTopVo.setStartTime(DatesUtil.getBeginDayOfWeek());//本周开始时间
                quizWinTopVo.setEndTime(DatesUtil.getEndDayOfWeek());//本周结束时间
                hd105Result = getHd105Result(hd105GiftResult, hd105Result, quizWinTopRequset, userConsumer, quizWinTopVo, hd105ResponsesList, WINTOP_WEEK_RESULT, WINTOP_WEEK_OVERTIME);
            } else {//上周排行榜
                quizWinTopVo.setStartTime(DatesUtil.getBeginDayOfLastWeek());
                quizWinTopVo.setEndTime(DatesUtil.getEndDayOfLastWeek());
                hd105Result = getHd105Result(hd105GiftResult, hd105Result, quizWinTopRequset, userConsumer, quizWinTopVo, hd105ResponsesList, WINTOP_LASTWEEK_RESULT, WINTOP_LASTWEEK_OVERTIME);
            }
            hd105Result.setEndTime(DatesUtil.getEndDayOfWeek());
            long endTime = System.currentTimeMillis();
            logger.info("查询排行榜，耗时{}毫秒", (endTime - startTime));
            return CommonResponse.withSuccessResp(hd105Result);
        } catch (Exception e) {
            logger.error(logPrefix + "查询排行榜出现异常，用户id:{}", userConsumer.getId(), e);
            return CommonResponse.withErrorResp("查询排行榜失败");
        }
    }

    private Hd105Result getHd105Result(Hd105GiftResult hd105GiftResult, Hd105Result hd105Result, QuizWinTopRequset quizWinTopRequset, UserConsumer userConsumer, QuizWinTopVo quizWinTopVo, List<Hd105Response> hd105ResponsesList, String key, int i) {
        if (!redisClient.exists(key)) {
            ModelResult<List<QuizWinTopResult>> listModelResult = quizOrderServiceClient.queryQuizWinTopList(quizWinTopVo);
            if (listModelResult.isSuccess() && CollectionUtil.isNotEmpty(listModelResult.getModel())) {
                //设置排行榜礼品
                if (quizWinTopRequset.getDateType().equals(DateType.THIS_WEEK.getIndex())) {
                    hd105ResponsesList = queryRankList(hd105GiftResult, listModelResult.getModel());
                } else {
                    hd105ResponsesList = queryRankLastWeekList(hd105GiftResult, listModelResult.getModel());
                }
                if (CollectionUtil.isNotEmpty(hd105ResponsesList)) {
                    logger.info("{}查询排行榜，加入缓存key:{}", logPrefix, key);
                    redisClient.setObj(key, i, hd105ResponsesList);
                }
            }
        } else {
            hd105ResponsesList = redisClient.getObj(key);
        }
        //查询个人排行榜信息
        hd105Result = queryUserRank(hd105ResponsesList, userConsumer);
        hd105Result.setWinTop(hd105ResponsesList);
        return hd105Result;
    }

    /**
     * 匹配到对应的用户，本周礼品信息
     *
     * @param hd105GiftResult 活动礼品信息
     * @param usrPosition     排名
     * @return
     */
    private Hd105GiftResponse getUserWeekGift(Hd105GiftResult hd105GiftResult, Integer usrPosition) {
        Hd105GiftResponse usrHdGift = null;
        List<Hd105GiftResponse> lists = hd105GiftResult.getHd105GiftResponses();
        if (CollectionUtil.isEmpty(lists)) {
            return usrHdGift;
        }
//        for (Hd105GiftResponse hdGift : lists) {
//            String[] strings = hdGift.getTopRank().split(";");
//            if (Arrays.asList(strings).contains(String.valueOf(usrPosition))) {
//                return hdGift;
//            }
//        }
        for (Hd105GiftResponse hdGift : lists) {
            String num = hdGift.getTopRank();
            if (num.contains(String.valueOf(usrPosition))) {
                return hdGift;
            } else if (num.contains("-")) {
                String[] split = num.split("-");
                Integer position1 = Integer.valueOf(split[0]);
                Integer position2 = Integer.valueOf(split[1]);
                if (usrPosition >= position1 && usrPosition <= position2) {
                    return hdGift;
                }
            }
        }

        if (usrHdGift == null) {
            /** 无匹配到用户礼品 */
            logger.error("无法匹配到对应的用户礼品");
        }
        return usrHdGift;
    }


    /**
     * 查询本周整个排行榜信息
     *
     * @param hd105GiftResult
     * @param topNRanks
     * @return
     */
    private List<Hd105Response> queryRankList(Hd105GiftResult hd105GiftResult, List<QuizWinTopResult> topNRanks) {
        List<Hd105Response> hd105ResponseList = new ArrayList<>();
        if (CollectionUtil.isEmpty(topNRanks)) {
            logger.info("排行榜信息为空");
            return hd105ResponseList;
        }
        //添加礼品信息，前n名
        for (int i = 0; i < topNRanks.size(); i++) {
            QuizWinTopResult quizRank = topNRanks.get(i);
            Hd105Response hd105Response = new Hd105Response();
            BeanUtil.copyProperties(quizRank, hd105Response, CopyOptions.create().ignoreNullValue());
            if (hd105GiftResult.getWinTopNum() != null && i < hd105GiftResult.getWinTopNum()) {
                Hd105GiftResponse userWeekGift = getUserWeekGift(hd105GiftResult, i + 1);
                hd105Response.setHd105GiftResponse(userWeekGift);
            }
            hd105ResponseList.add(hd105Response);
        }
        return hd105ResponseList;
    }

    /**
     * 查询上周整个排行榜信息
     *
     * @param hd105GiftResult
     * @param topNRanks
     * @return
     */
    private List<Hd105Response> queryRankLastWeekList(Hd105GiftResult hd105GiftResult, List<QuizWinTopResult> topNRanks) {
        List<Hd105Response> hd105ResponseList = new ArrayList<>();
        if (CollectionUtil.isEmpty(topNRanks)) {
            logger.info("排行榜信息为空");
            return hd105ResponseList;
        }
        Map<Long, HdUserGift> hdUserGiftMap = new HashMap<>();
        //上周，拿用户礼品集合
        List<Long> userIdList = topNRanks.stream().map(QuizWinTopResult::getUserId).distinct().collect(Collectors.toList());
        Date beginDayOfWeek = DatesUtil.getBeginDayOfWeek();
        Date endDayOfWeek = DatesUtil.getEndDayOfWeek();
        ModelResult<List<HdUserGift>> listModelResult = hdUserGiftServiceClient.queryHdUserGiftByUserIdList(userIdList, hd105GiftResult.getHdId(), beginDayOfWeek, endDayOfWeek);
        if (listModelResult != null && listModelResult.isSuccess() && CollectionUtil.isNotEmpty(listModelResult.getModel())) {
            List<HdUserGift> hdUserGiftList = listModelResult.getModel();
            hdUserGiftMap = hdUserGiftList.stream().collect(Collectors.toMap(k -> k.getUserId(), v -> v));
        }
        //添加礼品信息
        for (int i = 0; i < topNRanks.size(); i++) {
            QuizWinTopResult quizRank = topNRanks.get(i);
            Hd105Response hd105Response = new Hd105Response();
            BeanUtil.copyProperties(quizRank, hd105Response, CopyOptions.create().ignoreNullValue());
            if (!hdUserGiftMap.isEmpty()) {
                HdUserGift hdUserGift = hdUserGiftMap.get(quizRank.getUserId());
                if (hdUserGift != null) {
                    Hd105GiftResponse hd105GiftResponse = new Hd105GiftResponse();
                    BeanUtil.copyProperties(hdUserGift, hd105GiftResponse, CopyOptions.create().ignoreNullValue());
                    hd105Response.setHd105GiftResponse(hd105GiftResponse);
                }
            }
            hd105ResponseList.add(hd105Response);
        }
        return hd105ResponseList;
    }

    /**
     * 查询用户自己的排名情况
     *
     * @param hd105ResponseList 整个排行榜
     * @param userConsumer      用户
     * @return
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
                break;
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


    /**
     * 过滤无效礼品
     *
     * @param hd105InfoData
     * @param hd105GiftResult
     * @return
     */
    public Hd105GiftResult getHd105Info(HdInfo hd105InfoData, Hd105GiftResult hd105GiftResult) {
        String logPre = "查询排行榜活动信息_";
        List<Hd105GiftResponse> giftList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(hd105InfoData.getGiftList())) {
            List<HdGift> hdGiftsList = hd105InfoData.getGiftList();
            //剔除无效数据，按金额降序排序
            List<HdGift> lists = hdGiftsList.stream()
                    .filter(s -> (s.getGiftPropJo() != null && s.getStatus().equals(HdGiftStatus.valid.getIndex())))
                    .sorted(Comparator.comparing(HdGift::getAmount).reversed()).collect(Collectors.toList());
            logger.info(logPre + "过滤后的结果：lists{} " + JSONObject.toJSONString(lists.size()));
            if (CollectionUtil.isNotEmpty(lists)) {
                // 礼品信息
                Integer top = lists.get(lists.size() - 1).getGiftPropJo().getInteger("top");
                if (top > 0) {
                    hd105GiftResult.setWinTopNum(top);
                    for (HdGift hdGift : lists) {
                        Hd105GiftResponse hd105GiftResponse = new Hd105GiftResponse();
                        BeanUtil.copyProperties(hdGift, hd105GiftResponse, CopyOptions.create().ignoreNullValue());
                        hd105GiftResponse.setTopRank(hdGift.getGiftPropJo().getString("topRank"));
                        hd105GiftResponse.setTopNum(hdGift.getGiftPropJo().getInteger("top"));
                        giftList.add(hd105GiftResponse);
                    }
                }
            }
            hd105GiftResult.setHd105GiftResponses(giftList);
        }
        BeanUtil.copyProperties(hd105InfoData, hd105GiftResult, CopyOptions.create().ignoreNullValue());
        redisClient.setObj(HD105_GIFTLIST,  60, hd105GiftResult);
        logger.info(logPre + "加入缓存key:{},giftList{} ", HD105_GIFTLIST, JSONObject.toJSONString(giftList.size()));
        return hd105GiftResult;
    }

    @RequestMapping(value = "/queryUserGift", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户上周内的中奖信息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "查询用户上周内的中奖信息", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse queryUserGift(HttpServletRequest request) {
        UserConsumer userConsumer = getLoginUsr(request);
        UserHdGiftResponse userHdGiftResponse = new UserHdGiftResponse();
        try {
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("用户未登录");
            }
            if (StringUtils.isEmpty(redisClient.get(HD105_WIN_GIFT + userConsumer.getId()))) {
                Date beginDayOfWeek = DatesUtil.getBeginDayOfWeek();
                Date endDayOfWeek = DatesUtil.getEndDayOfWeek();
                ModelResult<HdInfo> hdInfoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_105.getIndex());
                if (hdInfoModelResult == null && !hdInfoModelResult.isSuccess() && hdInfoModelResult.getModel() == null) {
                    logger.error(logPrefix + "查询105活动信息异常");
                    return CommonResponse.withErrorResp("查询105活动信息异常");
                }
                HdInfo hdInfo = hdInfoModelResult.getModel();
                ModelResult<List<HdUserGift>> userGiftByCondition = hdUserGiftServiceClient.queryHdUserGiftByCondition(userConsumer.getId(), hdInfo.getId().longValue(),
                        beginDayOfWeek, endDayOfWeek);
                if (userGiftByCondition == null && !userGiftByCondition.isSuccess()) {
                    logger.error(logPrefix + "查询用户中奖信息异常");
                    return CommonResponse.withErrorResp("查询用户中奖信息异常");
                } else if (CollectionUtil.isNotEmpty(userGiftByCondition.getModel())) {
                    HdUserGift hdUserGift = userGiftByCondition.getModel().get(0);
                    BeanUtil.copyProperties(hdUserGift, userHdGiftResponse, CopyOptions.create().ignoreNullValue());
                    userHdGiftResponse.setExistWinPrize(true);
                }
                Long remainingWeekTime = DateUtil.calcSubTime004(new Date(), DatesUtil.getEndDayOfWeek());
                redisClient.set(HD105_WIN_GIFT + userConsumer.getId(), "1", remainingWeekTime.intValue() + WINTOP_LASTWEEK_OVERTIME);
            } else {
                userHdGiftResponse.setExistWinPrize(false);
            }
        } catch (Exception e) {
            logger.error(logPrefix + "查询用户中奖异常，用户id:{}", userConsumer.getId(), e);
            return CommonResponse.withErrorResp("查询用户中奖异常");
        }
        return CommonResponse.withSuccessResp(userHdGiftResponse);
    }

}
