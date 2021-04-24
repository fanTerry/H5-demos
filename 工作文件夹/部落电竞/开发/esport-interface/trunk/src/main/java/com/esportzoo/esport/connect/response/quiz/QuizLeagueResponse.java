package com.esportzoo.esport.connect.response.quiz;

import lombok.Data;

import java.io.Serializable;

/**
 * 竞猜联赛信息
 * @author tingjun.wang
 * @date 2019/10/23 9:31上午
 */
@Data
public class QuizLeagueResponse implements Serializable{
	private static final long serialVersionUID = 1714002235995920361L;
	/**
	 * 联赛id
	 */
	private Long leagueId;

	/**
	 * 联赛名称
	 */
	private String leagueName;

	/**
	 * 游戏id
	 */
	private Integer videogameId;

}
