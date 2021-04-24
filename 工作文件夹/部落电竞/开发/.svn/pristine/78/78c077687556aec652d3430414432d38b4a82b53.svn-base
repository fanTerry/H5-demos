package com.esportzoo.esport.connect.response.expert;

import com.esportzoo.esport.vo.expert.ExpertArticleContent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 文章详情响应
 * @author tingting.shen
 * @date 2019/05/13
 */
public class ArticleDetailResponse implements Serializable{

	private static final long serialVersionUID = 8290372015371612247L;
	
	/** 文章的id */
	private Long id;
	/** 专家用户Id */
	private Long userId;
	/** 专栏ID */
	private Long columnId;
	/** 游戏ID */
	private Integer videogameId;
	/** 标题 */
	private String title;
	/** 文章推荐编号-自动生成 */
	private String articleNo;
	/** 摘要 */
	private String articleDesc;
	/** 单价 */
	private BigDecimal price;
	/** 付费总额 */
	private BigDecimal amount;
	/** 查看次数 */
	private Integer views;
	/** 留言次数 */
	private Integer replies;
	/** 是否免费*/
	private boolean freeFlag;
	/** 发布时间 */
    private String publishTime;
	/** 内容 */
	private ExpertArticleContent content;
    /** 对阵信息 */
    private List<MatchVo> matchList;

    private boolean canView ;
    
    
	public Long getId() {
		return id;
	}
	public Long getUserId() {
		return userId;
	}
	public Long getColumnId() {
		return columnId;
	}
	public String getTitle() {
		return title;
	}
	public String getArticleNo() {
		return articleNo;
	}
	public String getArticleDesc() {
		return articleDesc;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public Integer getViews() {
		return views;
	}
	public Integer getReplies() {
		return replies;
	}
	public boolean isFreeFlag() {
		return freeFlag;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public ExpertArticleContent getContent() {
		return content;
	}
	public List<MatchVo> getMatchList() {
		return matchList;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
	}
	public void setArticleDesc(String articleDesc) {
		this.articleDesc = articleDesc;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public void setViews(Integer views) {
		this.views = views;
	}
	public void setReplies(Integer replies) {
		this.replies = replies;
	}
	public void setFreeFlag(boolean freeFlag) {
		this.freeFlag = freeFlag;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public void setContent(ExpertArticleContent content) {
		this.content = content;
	}
	public void setMatchList(List<MatchVo> matchList) {
		this.matchList = matchList;
	}

	public Integer getVideogameId() {
		return videogameId;
	}

	public void setVideogameId(Integer videogameId) {
		this.videogameId = videogameId;
	}

	public boolean isCanView() {
		return canView;
	}

	public void setCanView(boolean canView) {
		this.canView = canView;
	}
}
