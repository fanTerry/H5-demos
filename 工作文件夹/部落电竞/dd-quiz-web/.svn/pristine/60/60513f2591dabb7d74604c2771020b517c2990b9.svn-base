package com.esportzoo.esport.connect.request.quiz;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tingjun.wang
 * @date 2019/10/23 11:04上午
 */
@Data
public class QuizMatchInfoRequest extends BaseRequest implements Serializable{
	private static final long serialVersionUID = 2982911524760617186L;
	/** 联赛id */
	private Long leagueId;
	/** 赛事id */
	private Long matchId;
	/** 更多精彩中是否去除首页已显示的竞猜 flase 不去除展示，ture去除 不展示 */
	private Boolean distinctIndex;
	/** 去除首页已显示对局竞猜的id */
	private Long matchGameId;
	/** 赛事状态 */
	private Integer matchStatus;
	/** 当前页 */
	private Integer pageNo;
	/** 页大小 */
	private Integer pageSize;
	/** 查询开始时间 */
	private String startTime;
	/** 查询结束时间 */
	private String endTime;
	/** 是否推荐内容 */
	private Integer recommend;
	/** 游戏类型 */
	private Integer videoGameId;

}
