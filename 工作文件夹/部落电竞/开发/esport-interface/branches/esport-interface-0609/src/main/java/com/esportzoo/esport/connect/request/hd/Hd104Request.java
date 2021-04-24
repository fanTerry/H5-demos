package com.esportzoo.esport.connect.request.hd;

import com.esportzoo.esport.connect.request.BaseRequest;

/**
 * @description: 获取用户信息
 *
 * @author: Haitao.Li
 *
 * @create: 2020-03-18 15:38
 **/
public class Hd104Request extends BaseRequest {

	Integer hdId;

	/** 当前页 */
	private Integer pageNo;
	/** 页大小 */
	private Integer pageSize;

	public Integer getHdId() {
		return hdId;
	}

	public void setHdId(Integer hdId) {
		this.hdId = hdId;
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
}
