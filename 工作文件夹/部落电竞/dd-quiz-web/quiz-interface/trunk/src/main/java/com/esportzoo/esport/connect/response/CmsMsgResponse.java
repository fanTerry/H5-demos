package com.esportzoo.esport.connect.response;

import com.esportzoo.esport.constants.cms.CmsMsgType;
import com.esportzoo.esport.domain.NewsListVo;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户动态相关消息Response
 * 评论我的、赞我的、收藏我的
 * @author tingjun.wang
 * @date 2019/12/23 15:11下午
 */
@Data
public class CmsMsgResponse extends NewsListVo implements Serializable{

	private static final long serialVersionUID = -676686672650887707L;
	/** 消息类型 {@link CmsMsgType } */
	private Integer msgType;
	/** 评论内容 */
	private String currentComment;
	/** 被评论的内容*/
	private String beComment;
	/** 被评论的用户id*/
	private Long objectUserId;
	/** 被评论的用户昵称 */
	private String objectUserName;
	/** 消息中,文章或视频的发布人ID */
	private Long cmsObjectUserId;
	/** 消息中,文章或视频的发布人昵称 */
	private String cmsObjectUserName;
	/** 视频唯一标识ID ,前端渲染相同视频资讯使用*/
	private String videoSignId;
}
