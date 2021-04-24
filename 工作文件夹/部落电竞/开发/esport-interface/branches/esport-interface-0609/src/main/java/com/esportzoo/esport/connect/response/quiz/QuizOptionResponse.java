package com.esportzoo.esport.connect.response.quiz;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 题目选项
 * @author tingjun.wang
 * @date 2019/10/23 18:10下午
 */
@Data
public class QuizOptionResponse implements Serializable{
	/** 选项名字 */
	private String name;

	/** 选项赔率 */
	private String odds;

	/** 选项下标 */
	private Integer index;

	/** 最大投注限制 */
	private BigDecimal limit;

	/** 结果 */
}
