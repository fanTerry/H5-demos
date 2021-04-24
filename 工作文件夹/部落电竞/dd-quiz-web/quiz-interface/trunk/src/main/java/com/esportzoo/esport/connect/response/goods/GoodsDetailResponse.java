package com.esportzoo.esport.connect.response.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 商品详情页
 * 
 * @author wujing
 * @date:2019年5月8日上午10:58:24
 */
public class GoodsDetailResponse implements Serializable {

	private static final long serialVersionUID = 3878289646252064226L;
	/** 商品id */
	private Long id;
	/** 商品编号 */
	private String code;
	/** 商品名称 */
	private String name;
	/** 商品子标题 */
	private String subTitle;
	/** 商品描述详情 */
	private String description;
	/** 商品图片 */
	private String image;
	/** 商品图片 */
	private List<String> imageList;
	/** 商品图片，可以扩展 */
	private String subImage;
	/** 商品供应商id */
	private Long supplierId;
	/** 商品分类id */
	private Long categoryId;
	/** 商品品牌id */
	private Long brandId;
	/** 商品类型： 1实物，2虚拟物品，3奖券 @see ShopGoodsType */
	private Integer type;
	/** 商品标签： 0无， 1热门推荐， 2新品， 3折扣 @see ShopGoodsLabel */
	private Integer label;
	/** 商品排序 */
	private Integer sort;
	/** 商品销量 */
	private Integer num;
	/** 商品剩余库存（可用库存） */
	private Integer stock;
	/** 参考价（RMB） */
	private BigDecimal referencePrice;
	/** 折扣价（RMB） */
	private BigDecimal discountPrice;
	/** 预先设定所需要实际兑换支付的金额（RMB） */
	private BigDecimal payPayment;
	/** 预先设定所需要实际兑换支付的虚拟货币值 */
	private BigDecimal payScore;
	/** 点击量（查看量） */
	private Integer views;
	/** 发布时间 */
	private Date publishTime;
	/** 创建时间 */
	private Date createTime;
	/** 修改时间 */
	private Date updateTime;
	/** 商品状态 0创建， 1可兑， 2下架，3删除 @see ShopGoodsStatus */
	private Integer status;
	/** 业务平台类型1：默认 2：友宝 @see BizSystem */
	private Integer bizSystem;
	/** 六名已购买该商品的随机用户头像 */
	private LinkedHashSet<String> Icons;

	/** 购买人数（兑换量） */
	private Long purchases;
	/** 购买须知说明 */
	private String purchaseNote;
	/** 售后须知说明 */
	private String serviceNote;
	/** 商品所属游戏id */
	private Integer gameId;
	/** 标签描述 */
	private String labelName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<String> getImageList() {
		return imageList;
	}

	public void setImageList(List<String> imageList) {
		this.imageList = imageList;
	}

	public String getSubImage() {
		return subImage;
	}

	public void setSubImage(String subImage) {
		this.subImage = subImage;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLabel() {
		return label;
	}

	public void setLabel(Integer label) {
		this.label = label;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public BigDecimal getReferencePrice() {
		return referencePrice;
	}

	public void setReferencePrice(BigDecimal referencePrice) {
		this.referencePrice = referencePrice;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public BigDecimal getPayPayment() {
		return payPayment;
	}

	public void setPayPayment(BigDecimal payPayment) {
		this.payPayment = payPayment;
	}

	public BigDecimal getPayScore() {
		return payScore;
	}

	public void setPayScore(BigDecimal payScore) {
		this.payScore = payScore;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getBizSystem() {
		return bizSystem;
	}

	public void setBizSystem(Integer bizSystem) {
		this.bizSystem = bizSystem;
	}

	public LinkedHashSet<String> getIcons() {
		return Icons;
	}

	public void setIcons(LinkedHashSet<String> icons) {
		Icons = icons;
	}

	public Long getPurchases() {
		return purchases;
	}

	public void setPurchases(Long purchases) {
		this.purchases = purchases;
	}

	public String getPurchaseNote() {
		return purchaseNote;
	}

	public void setPurchaseNote(String purchaseNote) {
		this.purchaseNote = purchaseNote;
	}

	public String getServiceNote() {
		return serviceNote;
	}

	public void setServiceNote(String serviceNote) {
		this.serviceNote = serviceNote;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

}
