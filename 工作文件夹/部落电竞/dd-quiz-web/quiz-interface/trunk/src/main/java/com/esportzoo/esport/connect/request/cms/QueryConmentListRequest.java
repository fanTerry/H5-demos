package com.esportzoo.esport.connect.request.cms;

import java.io.Serializable;

import com.esportzoo.esport.connect.request.BasePageRequest;
import lombok.Data;

@Data
public class QueryConmentListRequest extends BasePageRequest implements Serializable{

	private static final long serialVersionUID = -8890577733591200569L;

	/**
	 * 文章id
	 */
	private Long contentId;

	/**
	 * 一级评论id
	 */
	private Long cmsCommentId;


}
