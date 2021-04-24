package com.esportzoo.esport.connect.response;

import java.io.Serializable;

import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.util.RegexUtils;

public class UsrInfoResponse implements Serializable{

	private static final long serialVersionUID = 8627128176829992000L;
	/**用户id*/
	private Long usrId;
	/**用户昵称*/
	private String nickName;
	/**用户头像*/
	private String avatarUrl;
	/**用户性别*/
	private Integer gender;
	/**所在城市*/
	private String city;
	/**所在国家*/
	private String country;
	/**所在省份*/
	private String province;
	/**uuid登录标识*/
	private String sid;
	
	public Long getUsrId() {
		return usrId;
	}

	public void setUsrId(Long usrId) {
		this.usrId = usrId;
	}

	public String getNickName() {
		return RegexUtils.filterEmoji(nickName, "").trim();
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
	public Integer getGender() {
		return gender;
	}
	
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String province) {
		this.province = province;
	}
	
	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public static UsrInfoResponse convertByUserConsumer(UserConsumer usr,String sid) {
    	UsrInfoResponse usrInfoResponse = new UsrInfoResponse();
    	usrInfoResponse.setUsrId(usr.getId());
    	usrInfoResponse.setNickName(usr.getNickName());
    	usrInfoResponse.setAvatarUrl(usr.getIcon());
    	usrInfoResponse.setGender(usr.getGender());
    	usrInfoResponse.setCity(usr.getCity());
    	usrInfoResponse.setProvince(usr.getProvince());
    	usrInfoResponse.setSid(sid);
    	return usrInfoResponse;
    }
}
