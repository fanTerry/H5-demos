package com.esportzoo.esport.connect.response.game;

import com.esportzoo.esport.vo.game.GameAwardLevelVo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: 欢乐摇一摇游戏流水
 *
 * @author: Haitao.Li
 *
 * @create: 2020-04-04 15:07
 **/

@Data
public class ShakeGameOrderLogResponse implements Serializable {

	private String gameName;

	/**
	 *消耗货币
	 */
	private BigDecimal score;

	/**
	 *返奖货币
	 */
	private BigDecimal returnScore;

	/**
	 *创建时间
	 */
	private Date createTime;

	/**
	 *订单状态：0:初始化，1:中奖，2未中奖,3：异常单
	 * {@link GameOrderStatus}
	 */
	private Integer status;


	/**
	 *用于前端按日期天汇总数据条目
	 */
	private String sortDate;


	List<GameAwardLevelVo> awardLevelVoList;

}
