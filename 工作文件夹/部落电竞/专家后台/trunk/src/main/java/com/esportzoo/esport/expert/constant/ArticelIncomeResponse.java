package com.esportzoo.esport.expert.constant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-29 17:44
 **/
public class ArticelIncomeResponse implements Serializable {

	private static final long serialVersionUID = 8767506363687855249L;


	/** 文章推荐编号-自动生成 */
	private String articleNo;
	/** 单价 */
	private BigDecimal price;

	/** 标题 */
	private String title;

	/** 创建时间 */
	private Calendar createTime;

	/** 付费总额 */
	private BigDecimal paySumMoney = new BigDecimal(0);

	/** 分成总额 */
	private BigDecimal payDividedMoney = new BigDecimal(0);

	/** 查看次数 */
	private Integer times;

	public String getArticleNo() {
		return articleNo;
	}

	public void setArticleNo(String articleNo) {
		this.articleNo = articleNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Calendar getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Calendar createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getPayDividedMoney() {
		return payDividedMoney;
	}

	public void setPayDividedMoney(BigDecimal payDividedMoney) {
		this.payDividedMoney = payDividedMoney;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public BigDecimal getPaySumMoney() {
		return paySumMoney;
	}

	public void setPaySumMoney(BigDecimal paySumMoney) {
		this.paySumMoney = paySumMoney;
	}
}
