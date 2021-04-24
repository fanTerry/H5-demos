package com.esportzoo.esport.connect.request.shop;

import java.io.Serializable;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.EsportPayway;

/**
 * 商城提交订单参数
 * 
 * @author wujing
 *
 */
public class ShopSubmitOrderRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -9134207413916129661L;
	/** 地址id */
	private Long addressId;
	/** 兑换商品的id */
	private Long goodsId;
	/** 兑换该商品数量 */
	private Integer num;
	/** 用户选择的支付方式 {@link EsportPayway} */
	private Integer choosedPayWay;
	/** 订单备注信息 */
	private String remark;

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getChoosedPayWay() {
		return choosedPayWay;
	}

	public void setChoosedPayWay(Integer choosedPayWay) {
		this.choosedPayWay = choosedPayWay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
