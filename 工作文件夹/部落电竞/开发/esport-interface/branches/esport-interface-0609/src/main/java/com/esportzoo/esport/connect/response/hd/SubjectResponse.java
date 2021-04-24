package com.esportzoo.esport.connect.response.hd;

import com.esportzoo.esport.hd.entity.HdGift;
import com.esportzoo.esport.hd.entity.HdSubjectOption;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 题目展示数据
 *
 * @author: Haitao.Li
 *
 * @create: 2019-09-17 16:14
 **/
@Data
public class SubjectResponse  implements Serializable {

	private static final long serialVersionUID = 2990479660339074330L;

	private Long id;

	private String name;

	/**
	 * 题目类型 单选多选 {@link HdSubjectType}
	 */
	private Integer type;

	/**
	 * 题目分类 电竞，娱乐 {@link HdSubjectCategory}
	 */
	private Integer category;

	/**
	 * 当前题目对应的选项
	 */
	private List<HdSubjectOption> hdSubjectOptions;

	/**
	 * 活动参与ID
	 */
	private Long hdUserLogId;

	/**
	 * 题目ID
	 */
	private Long  subjectId;

	/**
	 * 答题流水ID
	 */
	private Long  subjectLogId;

	/**
	 * 用户礼品信息
	 */
	private HdGift hdGift;

	/**
	 * 答对题目
	 */
	private Integer rightAnswer;

	/**
	 * 当天答题次数
	 */
	private Integer gameChanceNum;

	/**
	 * 获得星星的奖励个数
	 */
	private BigDecimal startPrizeNum ;

	/**
	 * 用户礼品流水ID
	 */
	private Long  userGiftLogId;


	/**此次答题的邀请码*/
	private String shareCode;


	/**
	 * 当天是否可以再次答题
	 */
	private Boolean canJoinSubject;



}
