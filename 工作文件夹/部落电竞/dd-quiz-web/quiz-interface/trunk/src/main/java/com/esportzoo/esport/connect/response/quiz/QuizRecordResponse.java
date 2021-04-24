package com.esportzoo.esport.connect.response.quiz;

import com.esportzoo.esport.connect.request.BasePageRequest;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @Author lixisheng
 * @Date 2019/10/24 19:58
 */
@Data
public class QuizRecordResponse  implements Serializable {
    private static final long serialVersionUID = 6974317947145513193L;

    /**
     *  竞猜编号
     */
    private String planNo;

    /**
     * 竞猜时间
     */
    private Calendar creatTime;

    /**
     *  玩法名称
     */
    private String playName;
    /**
     * 方案内容
     */
    private String content;
    /**
     *  投注方案sp值(投注时的赔率)
     */
    private BigDecimal betSp;

    /**
     *  投入总额
     */
    private BigDecimal cost;

    /**
     * 当前方案赛事中奖状态，0未开，1未中，2已中
     * */
    private Integer winStatus;

    /**
     *主队名称
     */
    private String homeTeamName;

    /**
     *客队名称
     */
    private String awayTeamName;

    /**
     * 比赛结果（彩易科思Res）,1表示该玩法投注选项主队赢得结果，或为大分或为奇数，2代表该玩法投注选项客队赢得结果或为小分或为偶数
     */
    private Integer awardResult;

    /**
     *玩法编号
     */
    private Integer playNo;




}
