package com.esportzoo.esport.connect.response.expert;

import com.esportzoo.leaguelib.common.domain.Match;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description: 前端文章数据展示Bean
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-10 12:02
 **/
public class RecExpertColumnArticleResponse implements Serializable {

	private static final long serialVersionUID = -3680973990632465176L;
	/** 文章ID */
	private Long id;
	/** 专家用户Id */
	private Long userId;
	/** 专栏ID */
	private Long columnId;
	/** 游戏ID */
	private Integer videogameId;
	/** 标题 */
	private String title;
	/** 摘要 */
	private String articleDesc;
	/** 是否免费:1是 0不是 */
	private Integer isFree;
	/** 单价 */
	private BigDecimal price;
	/** 预览图片 */
	private String imageUrl;
	/** 付费总额 */
	private BigDecimal amount;
	/** 查看次数 */
	private Integer views;
	/** 留言次数 */
	private Integer replies;
	/** 文章状态{@link ExpertArticleStatus} */
	private Integer status;

	/** 发布时间 */
	private String publishTime;
	/** 关联赛事信息 */
	private List<Match> matchResultList;

	/** 专家名 */
	private String authorName;
	/** 专家头像 */
	private String authorAvatar;
	
	/**当前登录用户是否已经支付了改文章*/
	private boolean hasPayed = false;
	/** 文章列表位置标记 1=文章-文章列表   2付费文章-文章列表 */
	private int placeFlag = 2;//默认2


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArticleDesc() {
		return articleDesc;
	}

	public void setArticleDesc(String articleDesc) {
		this.articleDesc = articleDesc;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getReplies() {
		return replies;
	}

	public void setReplies(Integer replies) {
		this.replies = replies;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public List<Match> getMatchResultList() {
		return matchResultList;
	}

	public void setMatchResultList(List<Match> matchResultList) {
		this.matchResultList = matchResultList;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorAvatar() {
		return authorAvatar;
	}

	public void setAuthorAvatar(String authorAvatar) {
		this.authorAvatar = authorAvatar;
	}

	public Integer getVideogameId() {
		return videogameId;
	}

	public void setVideogameId(Integer videogameId) {
		this.videogameId = videogameId;
	}

	public boolean isHasPayed() {
		return hasPayed;
	}

	public void setHasPayed(boolean hasPayed) {
		this.hasPayed = hasPayed;
	}

	public int getPlaceFlag() {
		return placeFlag;
	}

	public void setPlaceFlag(int placeFlag) {
		this.placeFlag = placeFlag;
	}
	
}
