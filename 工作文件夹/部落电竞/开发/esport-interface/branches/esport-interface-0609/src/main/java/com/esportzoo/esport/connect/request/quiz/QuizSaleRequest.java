package com.esportzoo.esport.connect.request.quiz;

import com.esportzoo.esport.connect.request.BasePageRequest;
import lombok.Data;

/**
 * @description: 查询销售数据管理
 *
 * @author: Haitao.Li
 *
 * @create: 2020-04-23 19:20
 **/
@Data
public class QuizSaleRequest extends BasePageRequest {

	/** 1、按照注册时间 2、充值 3、投注 */
	private Integer sortType ;

	/** 正序 反序 */
	private Boolean sortFlag = true ;


}
