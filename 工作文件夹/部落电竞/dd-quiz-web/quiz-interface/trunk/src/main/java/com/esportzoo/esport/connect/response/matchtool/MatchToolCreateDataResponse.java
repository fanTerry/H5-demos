package com.esportzoo.esport.connect.response.matchtool;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.esportzoo.esport.domain.tool.ToolGame;

/**
 * 创建赛事所需前置信息
 * 
 * @author jing.wu
 * @version 创建时间：2019年11月19日 下午2:40:24
 */
@Data
public class MatchToolCreateDataResponse implements Serializable {

	private static final long serialVersionUID = -5257865772928986831L;

	/** 当前用户是否允许创建赛事 */
	private Boolean canCreate;
	/** 当前可以选择的游戏 */
	private List<ToolGame> allGames;
	/** 参数队伍 2的N次方列表 */
	private List<Integer> teamsList;

}
