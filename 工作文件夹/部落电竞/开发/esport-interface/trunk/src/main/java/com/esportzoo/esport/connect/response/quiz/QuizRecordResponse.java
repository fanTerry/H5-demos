package com.esportzoo.esport.connect.response.quiz;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.connect.request.BasePageRequest;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @Author lixisheng
 * @Date 2019/10/24 19:58
 */
@Data
public class QuizRecordResponse  implements Serializable {
    private static final long serialVersionUID = 6974317947145513193L;

    /**竞猜编号 */
    private String planNo;
    /** 订单id*/
    private Long orderId;
    /** 竞猜时间*/
    private Calendar createTime;

    /** 玩法名称*/
    private String playName;

    /**  投注方案sp值(投注时的赔率)*/
    private BigDecimal betSp;

    /**  投入总额 */
    private BigDecimal cost;

    /** 赢盘的项 名字 */
    private String answer;

    /** 用户选择的投注项 名字*/
    private String option;
    /** 当前方案赛事中奖状态，1未开，2已中，3未中*/
    private Integer winStatus;

    /** 方案表状态 */
    private  Integer status;

    /**主队名称*/
    private String homeTeamName;

    /**客队名称*/
    private String awayTeamName;

    /**中奖总额 */
    private BigDecimal prize;

    /** 对战局数*/
    private String fightNum;

    /** 方案内容一场赛事一条记录*/
    private String content;

    /** 比赛结果（彩易科思Res）*/
    private String awardResult;

    /** 玩法编号*/
    private Integer playNo;

    /**玩法sp值规则*/
    private String sp;

    /** 赛事编号（彩易科思13位）
     * match_no
     */
    private String matchNo;

    /**比赛开始时间*/
    private Date startTime;

    /** 赛事对局id */
    private Long matchGameId;

    /** 游戏类型 */
    private Integer videoGameId;

    /** 第三方拒票原因（彩易科思：rejectDesc） */
    private String rejectDesc;

    /**订单表状态*/
    private Integer quizOrderStatus;

    /**
     *跟投订单中奖金额
     */
    private BigDecimal winAward;

    /**
     *推荐用户中奖分成
     */
    private BigDecimal winIncomeUser;
    /**
     *系统中奖分成
     */
    private BigDecimal winIncomeSys;

    /**
     *推荐单户账号
     */
    private String recommendAccount;

    /**
     *平台服务费率  例如 1  那就是 1%
     */
    private Integer feeRate;
    /**
     *发单人提成比例 例如 1  那就是 1%
     */
    private Integer commissionRate;

    /** 是否展示比赛信息*/
    private Boolean showMatch=false;

    /** 是否推单*/
    private Boolean recommendType =false;
    /**
     *推荐单用户昵称
     */
    private String followUserNickName;

    /**小局 赛事状态 */
    private Integer matchStatus;


    /** 方案类型 {@link com.esportzoo.esport.constants.quiz.QuizPlanType} */
    private Integer planType;

    public String getRejectDesc() {
        List<String> msgName=new ArrayList<String>(Arrays.asList("msg","resultMsg"));
        if (StrUtil.isBlank(rejectDesc)){
            return rejectDesc;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(rejectDesc);
            List<String> collect = msgName.stream().filter(a -> rejectDesc.contains(a)).collect(Collectors.toList());
            return jsonObject.getString(collect.get(0));
        } catch (Exception e) {
        }
        return rejectDesc;
    }


}
