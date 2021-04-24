package com.esportzoo.esport.connect.response.hd;

import java.io.Serializable;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2020-03-18 15:51
 **/
public class Hd104Response implements Serializable {

	/**分享码 **/
	private String shareCode;
	private Long sumStar;

	private String icon;
	private String nickName;
	private Long helpStar;
	private Long acceptStar;



	public String getShareCode() {
		return shareCode;
	}

	public void setShareCode(String shareCode) {
		this.shareCode = shareCode;
	}

	public Long getSumStar() {
		return sumStar;
	}

	public void setSumStar(Long sumStar) {
		this.sumStar = sumStar;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Long getHelpStar() {
		return helpStar;
	}

	public void setHelpStar(Long helpStar) {
		this.helpStar = helpStar;
	}

	public Long getAcceptStar() {
		return acceptStar;
	}

	public void setAcceptStar(Long acceptStar) {
		this.acceptStar = acceptStar;
	}
}
