package com.esportzoo.esport.connect.response.league;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tingjun.wang
 * @date 2020/1/6 16:13下午
 */
@Data
public class LeagueObjectVo implements Serializable {
	private static final long serialVersionUID = 7153807065887126817L;

	private String name;
	private Integer type;
	private String index;

	public static void main(String[] args) {
		List<LeagueObjectVo> list = new ArrayList<>();
		LeagueObjectVo leagueObjectVo = new LeagueObjectVo();
		leagueObjectVo.setIndex("1");
		leagueObjectVo.setName("LOL");
		leagueObjectVo.setType(1);
		list.add(leagueObjectVo);
		LeagueObjectVo objectVo = new LeagueObjectVo();
		objectVo.setIndex("水友赛");
		objectVo.setName("水友赛");
		objectVo.setType(2);
		list.add(objectVo);

		System.out.println(JSON.toJSON(list));

	}
}
