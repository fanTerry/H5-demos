package com.esportzoo.esport.connect.request;

import com.esportzoo.esport.constants.shop.GoodsQueryType;
import lombok.Data;

/**
 * @description: 首页参数
 *
 * @author: Haitao.Li
 *
 * @create: 2019-07-22 10:01
 **/
@Data
public class ShopRequest {

	private  Integer clientType;

	private Integer pageNo = 1;

	private Integer pageSize = 20;

	private Integer contentType;

	private Integer goodsQueryType;
	/**商品分类id*/
	private Long categoryId;
	/**商品品牌id*/
	private Long brandId;
	/**商品类型： 1实物，2虚拟物品，3奖券 @see ShopGoodsType*/
	private Integer type;
	/**商品标签： 0无， 1热门推荐， 2新品， 3折扣 @see ShopGoodsLabel*/
	private Integer label;

	private Integer shopOrderStatus;




}
