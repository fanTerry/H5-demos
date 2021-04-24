package com.esportzoo.esport.vo.ubox;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/06/10
 */
public class UboxUserInfo implements Serializable{

	private static final long serialVersionUID = 7178213949278919948L;
	
	private String id;
	private String ubox_user_id;
	private String openId;
	private String nickName;
	private Integer gender;
	private String language;
	private String city;
	private String province;
	private String country;
	private String avatarUrl;
	private String unionId;
	private String create_time;
	private String update_time;
	private String appid;
	//用这个取友宝用户id更准确一下 友宝的两个接口都有返回    ubox_user_id只有登录接口返回
	private Long uid;
	private Integer balance;
	public String getId() {
		return id;
	}
	public String getUbox_user_id() {
		return ubox_user_id;
	}
	public String getOpenId() {
		return openId;
	}
	public String getNickName() {
		return nickName;
	}
	public Integer getGender() {
		return gender;
	}
	public String getLanguage() {
		return language;
	}
	public String getCity() {
		return city;
	}
	public String getProvince() {
		return province;
	}
	public String getCountry() {
		return country;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public String getUnionId() {
		return unionId;
	}
	public String getCreate_time() {
		return create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public String getAppid() {
		return appid;
	}
	public Long getUid() {
		return uid;
	}
	public Integer getBalance() {
		return balance;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setUbox_user_id(String ubox_user_id) {
		this.ubox_user_id = ubox_user_id;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setGender(Integer gender) {
		this.gender = gender;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
}
