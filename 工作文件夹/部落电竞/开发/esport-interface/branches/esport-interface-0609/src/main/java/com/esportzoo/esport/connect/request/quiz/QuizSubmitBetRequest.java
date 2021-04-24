package com.esportzoo.esport.connect.request.quiz;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.quiz.QuizOrderType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 立即竞猜
 *
 * @author jing.wu
 * @version 创建时间：2019年10月23日 上午11:12:14
 */
@Data
public class QuizSubmitBetRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = 1351991690425188883L;
	// 比赛日期+赛事编号+“*”+玩法+“|”+竟猜选项+“@”+赔率;
	/** quiz_match_game表id */
	private Long matchGameId;
	/** 赛事编号 */
	private String matchNo;
	/** 玩法编号 */
	private Integer playNo;
	/** 选择的竞猜选项索引 */
	private String optIndex;
	/** 投注方案sp值(投注时的赔率) */
	private BigDecimal betSp;
	/** 投入总额 */
	private BigDecimal userBetNum;
	/**
	 * 0 代表不接受赔率变化，只有投注赔率和系统赔率完全一样的情况才能投注成功
	 * 1代表接收更好赔率。确认投注成功后结算的赔率比用户下注的赔率更高时会成功
	 * 2代表接收系统所有赔率，当投注赔率和系统赔率不一致的时候，直接按照系统赔率投注成功
	 * (前端预留字段,暂时没用上)
	 */
	private Short acceptAllPrice = 0;

	/** 方案类型 {@link QuizOrderType}*/
	private Integer orderType;

	/** 跟单的方案id*/
	private  Long recommendPlanId;

}
