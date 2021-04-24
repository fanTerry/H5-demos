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
	/** 赛事状态  */
	private Integer matchStatus;
	private String matchStatusList;
	/** 当前页 */
	private Integer pageNo;
	/** 页大小 */
	private Integer pageSize;
	/** 查询开始时间 yyyy-MM-dd HH:mm:ss */
	private String startTime;
	/** 查询结束时间 yyyy-MM-dd HH:mm:ss */
	private String endTime;
	/** 游戏类型 */
	private Integer videoGameId;
	/** 是否查询完结玩法(QuizMatchGame) */
	private Boolean finished = false;


	///这些基本不用，但接口还有保留
	/** 是否推荐内容 */
	private Integer recommend;
	/** 更多精彩中是否去除首页已显示的竞猜 flase 不去除展示，ture去除 不展示 */
	private Boolean distinctIndex;
	/** 去除首页已显示对局竞猜的id */
	private Long matchGameId;



}
