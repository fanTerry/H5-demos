package com.esportzoo.esport.connect.response.league;

import java.io.Serializable;

/**
 * @author tingjun.wang
 * @date 2019/8/20 14:28
 */
public class TeamResultVo implements Serializable {
	private static final long serialVersionUID = -8928763458249519881L;
	private Long id;
	/** 战队id */
	private Long teamId;
	/** 战队名称 */
	private String name;
	/** 战队简称 */
	private String acronym;
	/** 战队logo */
	private String imageUrl;
	/** 本地战队logo */
	private String esportImageUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEsportImageUrl() {
		return esportImageUrl;
	}

	public void setEsportImageUrl(String esportImageUrl) {
		this.esportImageUrl = esportImageUrl;
	}
}
