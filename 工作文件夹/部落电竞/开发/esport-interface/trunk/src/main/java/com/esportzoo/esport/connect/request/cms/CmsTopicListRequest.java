package com.esportzoo.esport.connect.request.cms;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.esportzoo.esport.constants.CmsCategoryType;
import lombok.Data;

import com.esportzoo.esport.connect.request.BasePageRequest;
import com.esportzoo.esport.constants.cms.CmsTopicStatus;

/**
 * 话题列表请求
 * @author jing.wu
 * @version 创建时间：2019年8月26日 下午3:42:36
 */
@Data
public class CmsTopicListRequest extends BasePageRequest implements Serializable {

	private static final long serialVersionUID = -4024183482333758802L;

	/**
	 * 话题id
	 */
	private Long id;

	/** 话题名称 */
	private String name;

	/** 话题拼音的首字母 */
	private String orderTag;

	/** 话题状态 {@link CmsTopicStatus} */
	private Integer status;

	/** 查询创建日期 开始区间 */
	private Date createStartTime;

	/** 查询创建日期 结束区间 */
	private Date createEndTime;

	/** 创建用户 */
	private String createUser;

	/** 创建用户id */
	private Long createUserId;

	/**
	 * 资讯类型 @see {@link CmsCategoryType}
	 */
	private Integer contentType;

}
