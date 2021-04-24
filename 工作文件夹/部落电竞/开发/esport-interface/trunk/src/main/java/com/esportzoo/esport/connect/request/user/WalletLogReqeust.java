package com.esportzoo.esport.connect.request.user;

import com.esportzoo.esport.connect.request.BaseRequest;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tingjun.wang
 * @date 2019/8/22 19:12
 */
public class WalletLogReqeust extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -1260628472705363973L;
	private Integer pageNo = 1;
	private Integer pageSize = 10;
	private Integer type;
	/**开始时间*/
	private Date startTime;
	/**结束时间*/
	private Date endTime;

	/** 业务操作类型(1加币，2减币) */
	private Integer walletOperType;

	public Integer getWalletOperType() {
		return walletOperType;
	}

	public void setWalletOperType(Integer walletOperType) {
		this.walletOperType = walletOperType;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
