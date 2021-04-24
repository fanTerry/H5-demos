package com.esportzoo.esport.connect.request.helpcenter;

import java.io.Serializable;

import lombok.Data;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.user.UserFeedbackType;
import com.esportzoo.esport.constants.user.UserQuestionType;

/**
 * 意见反馈提交
 * @author jing.wu
 * @version 创建时间：2020年1月9日 下午3:52:11
 */
@Data
public class UserFeedBackSubmitRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = 8697713675290573927L;

	/** 回复类型 {@link UserFeedbackType} */
	private Integer feedbackType;
	/** 问题类别 {@link UserQuestionType} */
	private Integer questionType;
	/** 内容 */
	private String content;
	/** 联系电话 */
	private String phone;
}
