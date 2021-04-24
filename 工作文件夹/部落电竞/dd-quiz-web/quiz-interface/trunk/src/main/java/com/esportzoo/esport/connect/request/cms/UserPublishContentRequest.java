package com.esportzoo.esport.connect.request.cms;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.CmsTypeDefineConstant;

/**
 * @description: 用户短文发布
 *
 * @author: Haitao.Li
 *
 * @create: 2019-08-23 14:35
 **/
@Data
public class UserPublishContentRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -451979149695539358L;

	/** {@link CmsTypeDefineConstant} */
	private Integer typeId;

	private String content;

	private String title;

	private String shortTitle;

	private String topics;

	private Integer status;

	private Integer views;

	private Date issueTime;

	private String issueUser;

	private Long issueUserId;

}
