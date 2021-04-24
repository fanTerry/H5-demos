package com.esportzoo.esport.connect.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 商城订单前端展示的查询状态
 * @author tingting.shen
 * @date 2019/08/12
 */
public class ShopOrderStatusShow implements Serializable {

	private static final long serialVersionUID = 2852757157382423779L;
	
	/**订单状态对应的ShopOrderStatus的index*/
	private Integer contentType;
	
	/**前端展示的订单状态*/
	private String name;
	
	private String frontClass;
	

	public ShopOrderStatusShow() {
		
	}
	
	public ShopOrderStatusShow(Integer contentType, String name, String frontClass) {
		super();
		this.contentType = contentType;
		this.name = name;
		this.frontClass = frontClass;
	}

	public Integer getContentType() {
		return contentType;
	}

	public String getName() {
		return name;
	}

	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFrontClass() {
		return frontClass;
	}

	public void setFrontClass(String frontClass) {
		this.frontClass = frontClass;
	}

	public static void main(String[] args) {
		ShopOrderStatusShow show1 = new ShopOrderStatusShow(0, "全部兑换", "allExchange_icon");
		//ShopOrderStatusShow show2 = new ShopOrderStatusShow(1, "待支付");
		ShopOrderStatusShow show3 = new ShopOrderStatusShow(5, "待收货", "get_icon");
		ShopOrderStatusShow show4 = new ShopOrderStatusShow(6, "已完成", "done_icon");
		List<ShopOrderStatusShow> list = new ArrayList<>();
		list.add(show1);
		//list.add(show2);
		list.add(show3);
		list.add(show4);
		String s = JSON.toJSONString(list);
		System.out.println(s);
	}

	
}
