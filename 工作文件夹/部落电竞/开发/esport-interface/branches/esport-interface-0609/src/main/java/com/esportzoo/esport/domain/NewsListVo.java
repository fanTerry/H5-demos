package com.esportzoo.esport.domain;

import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.constants.CmsCategoryType;
import com.esportzoo.esport.vo.cms.FollowedUserVo;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class NewsListVo implements Serializable {

	private static final long serialVersionUID = 2189919457649913054L;

	private Long aricleId;
	private Long issueUserId;
	private String authorImg;
	/** {@link CmsCategoryType} */
	private Integer type;
	private String imageSrc;
	/** 作者 */
	private String tag;
	/** 短文的内容 咨询、视频的标题 */
	private String detail;
	/** 观看数 */
	private Long seeNum;
	/** 评论次数 */
	private Long discussNum;
	/** 前端原因(此处也是点赞数，用的是这个) */
	private Integer commentNum;
	/** 点赞数 */
	private Long ups;
	private boolean upFlag = false;
	/** 标题图片 */
	private String titleImg;
	private Integer isTop;
	/** 发布时间,格式为yyyy-MM-dd HH:mm */
	private String publishTimeStr;

	/** 该字段 关注列表资讯使用 */
	private FollowedUserVo followedUser;

	/** 用户发布短文图片列表 */
	private List<String> shortArticleList;

	/** 用户收藏数量*/
	private Integer favorites;
	/** 用户是否收藏*/
	private boolean favoritesFlag = false;
	/** 用户是否评论*/
	private boolean discussFlag = false;
	private Date updateTime;

	public Integer getFavorites() {
		return favorites;
	}

	public void setFavorites(Integer favorites) {
		this.favorites = favorites;
	}

	public boolean isFavoritesFlag() {
		return favoritesFlag;
	}

	public void setFavoritesFlag(boolean favoritesFlag) {
		this.favoritesFlag = favoritesFlag;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getImageSrc() {
		return imageSrc;
	}

	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}

	public String getTag() {
		return tag;
	}

	public String getAuthorImg() {
		return authorImg;
	}

	public void setAuthorImg(String authorImg) {
		this.authorImg = authorImg;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Long getSeeNum() {
		return seeNum;
	}

	public void setSeeNum(Long seeNum) {
		this.seeNum = seeNum;
	}

	public Long getDiscussNum() {
		return discussNum;
	}

	public void setDiscussNum(Long discussNum) {
		this.discussNum = discussNum;
	}

	public Long getAricleId() {
		return aricleId;
	}

	public void setAricleId(Long aricleId) {
		this.aricleId = aricleId;
	}

	public Long getUps() {
		return ups;
	}

	public void setUps(Long ups) {
		this.ups = ups;
	}

	public String getTitleImg() {
		return titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public String getPublishTimeStr() {
		if (StringUtils.isNotBlank(publishTimeStr)) {
			return DateUtil.getShortTime(publishTimeStr);
		}
		return publishTimeStr;
	}

	public void setPublishTimeStr(String publishTimeStr) {
		this.publishTimeStr = publishTimeStr;
	}

	public FollowedUserVo getFollowedUser() {
		return followedUser;
	}

	public void setFollowedUser(FollowedUserVo followedUser) {
		this.followedUser = followedUser;
	}

	public Long getIssueUserId() {
		return issueUserId;
	}

	public void setIssueUserId(Long issueUserId) {
		this.issueUserId = issueUserId;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public boolean isUpFlag() {
		return upFlag;
	}

	public void setUpFlag(boolean upFlag) {
		this.upFlag = upFlag;
	}

	public List<String> getShortArticleList() {
		return shortArticleList;
	}

	public void setShortArticleList(List<String> shortArticleList) {
		this.shortArticleList = shortArticleList;
	}

	public Integer getIsTop() {
		return isTop;
	}

	public void setIsTop(Integer isTop) {
		this.isTop = isTop;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public boolean isDiscussFlag() {
		return discussFlag;
	}
	public void setDiscussFlag(boolean discussFlag) {
		this.discussFlag = discussFlag;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NewsListVo)) return false;

		NewsListVo that = (NewsListVo) o;

		if (isUpFlag() != that.isUpFlag()) return false;
		if (getAricleId() != null ? !getAricleId().equals(that.getAricleId()) : that.getAricleId() != null)
			return false;
		if (getIssueUserId() != null ? !getIssueUserId().equals(that.getIssueUserId()) : that.getIssueUserId() != null)
			return false;
		if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
		if (getImageSrc() != null ? !getImageSrc().equals(that.getImageSrc()) : that.getImageSrc() != null)
			return false;
		if (getTag() != null ? !getTag().equals(that.getTag()) : that.getTag() != null) return false;
		if (getDetail() != null ? !getDetail().equals(that.getDetail()) : that.getDetail() != null) return false;
		if (getSeeNum() != null ? !getSeeNum().equals(that.getSeeNum()) : that.getSeeNum() != null) return false;
		if (getDiscussNum() != null ? !getDiscussNum().equals(that.getDiscussNum()) : that.getDiscussNum() != null)
			return false;
		if (getCommentNum() != null ? !getCommentNum().equals(that.getCommentNum()) : that.getCommentNum() != null)
			return false;
		if (getUps() != null ? !getUps().equals(that.getUps()) : that.getUps() != null) return false;
		if (getTitleImg() != null ? !getTitleImg().equals(that.getTitleImg()) : that.getTitleImg() != null)
			return false;
		if (getPublishTimeStr() != null ? !getPublishTimeStr().equals(that.getPublishTimeStr()) : that.getPublishTimeStr() != null)
			return false;
		if (getFollowedUser() != null ? !getFollowedUser().equals(that.getFollowedUser()) : that.getFollowedUser() != null)
			return false;
		return getShortArticleList() != null ? getShortArticleList().equals(that.getShortArticleList()) : that.getShortArticleList() == null;
	}

	@Override
	public int hashCode() {
		int result = getAricleId() != null ? getAricleId().hashCode() : 0;
		result = 31 * result + (getIssueUserId() != null ? getIssueUserId().hashCode() : 0);
		result = 31 * result + (getType() != null ? getType().hashCode() : 0);
		result = 31 * result + (getImageSrc() != null ? getImageSrc().hashCode() : 0);
		result = 31 * result + (getTag() != null ? getTag().hashCode() : 0);
		result = 31 * result + (getDetail() != null ? getDetail().hashCode() : 0);
		result = 31 * result + (getSeeNum() != null ? getSeeNum().hashCode() : 0);
		result = 31 * result + (getDiscussNum() != null ? getDiscussNum().hashCode() : 0);
		result = 31 * result + (getCommentNum() != null ? getCommentNum().hashCode() : 0);
		result = 31 * result + (getUps() != null ? getUps().hashCode() : 0);
		result = 31 * result + (isUpFlag() ? 1 : 0);
		result = 31 * result + (getTitleImg() != null ? getTitleImg().hashCode() : 0);
		result = 31 * result + (getPublishTimeStr() != null ? getPublishTimeStr().hashCode() : 0);
		result = 31 * result + (getFollowedUser() != null ? getFollowedUser().hashCode() : 0);
		result = 31 * result + (getShortArticleList() != null ? getShortArticleList().hashCode() : 0);
		return result;
	}

}
