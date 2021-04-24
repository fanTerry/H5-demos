package com.esportzoo.esport.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 订单详情前端展示
 *
 * @author: Haitao.Li
 *
 * @create: 2019-07-29 10:33
 **/
@Data
public class ShopOrderDetailVo {

	/**主键id*/
	private Long id;

	private String goodImg;
	/**商品订单ID*/
	private Long orderId;
	/**兑换订单编号*/
	private String orderNo;

	private String orderStausDec;
	/**商品ID*/
	private Long goodsId;
	/**商品编号*/
	private String goodsCode;
	/**商品名称*/
	private String goodsName;
	/**商品单价,所需虚拟币*/
	private BigDecimal scorePrice;
	/**商品单价,所需现金*/
	private BigDecimal paymentPrice;
	/**商品数量*/
	private Integer quantity;
	/**商品总价(虚拟币)*/
	private BigDecimal scoreTotal;
	/**商品总价(现金)*/
	private BigDecimal paymentTotal;
	/**商品进货批次*/
	private String batchNo;
	/**商品进货价，和批次号关联*/
	private BigDecimal price;
	/** 创建时间 */
	private Date createTime;
	/** 修改时间 */
	private Date updateTime;
	/** 扩展字段，json格式 */
	private String features = "{}";
}
