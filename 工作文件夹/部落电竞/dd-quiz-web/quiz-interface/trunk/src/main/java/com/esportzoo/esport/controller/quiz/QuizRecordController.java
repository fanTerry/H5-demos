package com.esportzoo.esport.controller.quiz;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.esport.constants.quiz.QuizOption;
import com.esportzoo.esport.constants.quiz.SpInfoResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.util.SpUtils;
import com.esportzoo.esport.client.service.quiz.QuizExchangeGoodsServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizOrderServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizRecordPageRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.quiz.QuizExchangeGoodsStatus;
import com.esportzoo.esport.constants.quiz.QuizPlanStatus;
import com.esportzoo.esport.constants.quiz.QuizPlanWinStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.quiz.QuizPlayDefine;
import com.esportzoo.esport.util.CatchPlayDefineUtil;
import com.esportzoo.esport.vo.quiz.QuizExchangeGoodsQueryVo;
import com.esportzoo.esport.vo.quiz.QuizExchangeRecordResponse;
import com.esportzoo.esport.vo.quiz.QuizRecordQueryVo;
import com.esportzoo.esport.vo.quiz.QuizRecordResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 查询用户竞猜记录
 * @Author lixisheng
 * @Date 2019/10/24 19:36
 */
@Controller
@RequestMapping("/quiz/record")
public class QuizRecordController extends BaseController {
    private transient final Logger logger = LoggerFactory.getLogger(getClass());
    private static String loggerPre = "查询用户竞猜记录_";


    @Autowired
    private QuizOrderServiceClient quizOrderServiceClient;
    @Autowired
    private CatchPlayDefineUtil playDefineUtil;
    @Autowired
    private QuizExchangeGoodsServiceClient exchangeGoodsServiceClient;

    @RequestMapping(value = "/recordPage", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "竞猜记录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "竞猜记录信息", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<List<QuizRecordResponse>> queryRecordPage(QuizRecordPageRequest param, HttpServletRequest request) {
        UserConsumer userConsumer = getLoginUsr(request);
        try {
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("用户未登录");
            }
            QuizRecordQueryVo quizRecordQueryVo=new QuizRecordQueryVo();
            quizRecordQueryVo.setUserId(userConsumer.getId());//用户ID
            quizRecordQueryVo.setWinStatus(param.getWinStatus());//当前方案赛事中奖状态，0、待开奖，2、已中奖，1、未中奖
            //0、待开奖时，只展示已经投注成功的方案
            if (param.getWinStatus()!=null&&param.getWinStatus().intValue()== QuizPlanWinStatus.NOT_OPEN.getIndex()){
                quizRecordQueryVo.setPlanNoShowList(QuizPlanStatus.getNoAwaitShowList());
            }else {
                quizRecordQueryVo.setPlanNoShowList(QuizPlanStatus.getNoShowList());
            }
            DataPage<QuizRecordResponse> dataPage = new DataPage<>();
            dataPage.setPageNo(param.getPageNo());
            dataPage.setPageSize(param.getPageSize());
            logger.info(loggerPre+"请求参数"+"quizRecordQueryVo=【{}】,dataPage=【{dataPage}】",JSON.toJSONString(quizRecordQueryVo),JSON.toJSONString(dataPage));
            PageResult<QuizRecordResponse> pageResult = quizOrderServiceClient.queryRecordPage(quizRecordQueryVo, dataPage);
            logger.info(loggerPre+"pageResult={}",JSON.toJSONString(pageResult.getPage().getDataList().size()));
            if (!pageResult.isSuccess()) {
                logger.info(loggerPre+"分页查询用户竞猜记录订单接口,调用接口返回错误,errorMsg={},param={}",pageResult.getErrorMsg(), JSON.toJSONString(param));
                return CommonResponse.withErrorResp(pageResult.getErrorMsg());
            }
            List<QuizRecordResponse> dataList = pageResult.getPage().getDataList();
            if (dataList!=null) {
                for (QuizRecordResponse quizRecordResponse : dataList) {
                    String content=quizRecordResponse.getContent();
                    if (content.contains("FSGL")) {
                        logger.info("查询用户选择投注项");
                        String index = content.substring(content.indexOf("|") + 1, content.indexOf("@"));

                        List<QuizOption> quizOptionList = SpUtils.spToQuizOptionList(quizRecordResponse.getSp(), quizRecordResponse.getPlayNo(),
                                new String[]{quizRecordResponse.getHomeTeamName(), quizRecordResponse.getAwayTeamName()});
                        Map<String, QuizOption> indexAndQuizOptionMap = quizOptionList.stream().collect(Collectors.toMap(key -> key.getIndex(), value -> value));

                        if (quizRecordResponse.getAwardResult()!=null){
                            QuizOption answerOption = indexAndQuizOptionMap.get(quizRecordResponse.getAwardResult());
                            if (answerOption!=null){
                                quizRecordResponse.setAnswer((answerOption.getTeamName()==null?"":answerOption.getTeamName()+" ")+answerOption.getName()); //赢盘的项名字
                            }else {
                                logger.error("比赛结果与解析sp的值不匹配：quizRecordResponse{},quizOptionList:{}",JSON.toJSONString(quizRecordResponse),JSON.toJSONString(quizOptionList));
                            }
                        }
                        QuizOption option = indexAndQuizOptionMap.get(index);
                        quizRecordResponse.setOption((option.getTeamName()==null?"":option.getTeamName()+" ")+option.getName());//用户选择的投注项 名字
                        /* 设置玩法题目 */
                        //获得解析的sp对象
                        List<SpInfoResult> spInfoResults = SpUtils.resolverSp(quizRecordResponse.getSp());
                        //题目中内容取第一个选项的附加值
                        Map<Integer, QuizPlayDefine> playNoThirdNameMap = playDefineUtil.getPlayNoMap();
                        QuizPlayDefine quizPlayDefine = playNoThirdNameMap.get(quizRecordResponse.getPlayNo());
                        if (quizPlayDefine!=null && spInfoResults!=null){
                            String attach = spInfoResults.get(0).getAttach();
                            String subjectName = quizPlayDefine.getSubjectName();
                            if (StringUtils.isNotBlank(attach)){
                                subjectName = String.format(subjectName,attach);
                            }
                            quizRecordResponse.setPlayName(subjectName);
                        }
                    }
                }
            }
            logger.info("查询用户竞猜记录dataList{}",JSON.toJSONString(dataList.size()));
            return CommonResponse.withSuccessResp(dataList);
        } catch (Exception e) {
            logger.error(loggerPre+"查询用户竞猜记录出现异常，用户id:{}", userConsumer.getId(), e);
            return CommonResponse.withErrorResp("查询用户竞猜记录失败");
        }
    }


    @RequestMapping(value = "/exchangeRecord", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "兑奖播报", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "兑奖播报信息", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<List<QuizExchangeRecordResponse>> queryRecordPage(BaseRequest request) {
        List<QuizExchangeRecordResponse> responseList = new ArrayList<>();
        try {
            QuizExchangeGoodsQueryVo queryVo = new QuizExchangeGoodsQueryVo();
            queryVo.setStartCreateTime(DateUtil.getCurBeforeOrAferHours(-48));
            queryVo.setStatus(QuizExchangeGoodsStatus.VALID.getIndex());
            ModelResult<List<QuizExchangeRecordResponse>> listModelResult = exchangeGoodsServiceClient.indexRadioList(queryVo);
            responseList = listModelResult.getModel();
            if (!listModelResult.isSuccess() || responseList == null){
                return CommonResponse.withErrorResp("暂无兑奖记录");
            }
        } catch (Exception e) {
            logger.error("查询首页兑奖播报记录异常",e);
        }
        return CommonResponse.withSuccessResp(responseList);
    }
}
