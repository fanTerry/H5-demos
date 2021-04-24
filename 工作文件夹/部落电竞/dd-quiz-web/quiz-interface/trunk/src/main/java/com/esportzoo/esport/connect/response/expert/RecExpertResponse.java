package com.esportzoo.esport.connect.response.expert;

import com.esportzoo.esport.connect.response.BaseResponse;

import java.io.Serializable;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-10 15:01
 **/
public class RecExpertResponse  extends BaseResponse implements Serializable {


	private static final long serialVersionUID = 6302836136070843339L;

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 头像
	 */
	private String avatarImgUrl;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatarImgUrl() {
		return avatarImgUrl;
	}

	public void setAvatarImgUrl(String avatarImgUrl) {
		this.avatarImgUrl = avatarImgUrl;
	}
}
