package com.esportzoo.esport.connect.request.cms;

import java.io.Serializable;

import com.esportzoo.esport.constants.cms.CmsCommentLevel;

/**发布评论请求*/
public class PublishCommentRequest implements Serializable{

	private static final long serialVersionUID = -4227951466472589060L;
	
	/**必要参数  发布评论等级，具体参考{@link CmsCommentLevel}*/
	private Integer commentLevel;
	/**必要参数  cms_content表的id*/
	private Long contentId;
	/**必要参数  cms_content表的type_id*/
	private Long contentTypeId;
	/**必要参数  评论内容*/
	private String comment;
	/**非必要参数，只有在评论的对象是评论时才需要，评论的根id,cms_comment表的id*/
	private Long commentRootId;
	/**非必要参数，只有在评论的对象是评论时才需要，评论的父id,cms_comment表的id*/
	private Long commentParentId;
	
	public Integer getCommentLevel() {
		return commentLevel;
	}
	public void setCommentLevel(Integer commentLevel) {
		this.commentLevel = commentLevel;
	}
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Long getContentTypeId() {
		return contentTypeId;
	}
	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getCommentRootId() {
		return commentRootId;
	}
	public void setCommentRootId(Long commentRootId) {
		this.commentRootId = commentRootId;
	}
	public Long getCommentParentId() {
		return commentParentId;
	}
	public void setCommentParentId(Long commentParentId) {
		this.commentParentId = commentParentId;
	}
	
}
