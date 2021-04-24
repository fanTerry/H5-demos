package com.esportzoo.esport.connect.request;

import java.util.List;

import lombok.Data;

/**
 * @description: 查询关注列表资讯参数
 *
 * @author: Haitao.Li
 *
 * @create: 2019-08-19 15:02
 **/
@Data
public class FollowRequest extends BaseRequest {

	private Integer pageNo;

	private Integer pageSize;

	/** 资讯类型 */
	private Integer contentType;

	private Long userId;

	/**
	 * 话题id
	 */
	private List<Long> cmsTopicIds;
	private Long cmsTopicId;

	/**
	 * 标记 是否去查询用户关注的用户资讯列表
	 */
	private Boolean showFollowUserCms = false;

	/**
	 * 标记 是否去查询用户资讯列表
	 */
	private Boolean showUserCms = false;

	/**
	 * 标记 是否查询置顶的内容
	 */
	private Boolean showTop = false;

	/**
	 * 查询我的记录类型
	 * {@link  com.esportzoo.esport.constants.cms.CmsMsgType}
	 */
	private Integer cmsMsgType ;
}
