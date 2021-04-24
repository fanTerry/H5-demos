package com.esportzoo.esport.connect.response.helpcenter;

import lombok.Data;

import java.util.Date;

/**
 * @description: 用户反馈信息
 *
 * @author: Haitao.Li
 *
 * @create: 2020-01-13 10:48
 **/
@Data
public class UserFeedbackResponse {


	private Long id;
	/**
	 *用户id
	 */
	private Long userId;
	/**
	 *用户账号
	 */
	private String account;
	/**
	 *回复类型 {@link UserFeedbackType}
	 */
	private Integer feedbackType;

	private String feedbackTypeName;
	/**
	 *问题类别 {@link UserQuestionType}
	 */
	private Integer questionType;

	private String questionTypeName;
	/**
	 *内容
	 */
	private String content;
	/**
	 *图片地址
	 */
	private String imageUrl;
	/**
	 *联系电话
	 */
	private String phone;
	/**
	 *父问题id
	 */
	private Long parentId;
	/**
	 *创建时间
	 */
	private Date createTime;
	/**
	 *更新时间
	 */
	private Date updateTime;
	/**
	 *状态 {@link UserFeedbackStatus}
	 */
	private Integer status;

	/**
	 *回复最后更新时间
	 */
	private Date replyUpdateTime;

	/**
	 *回复数
	 */
	private Integer replyNum;

}
