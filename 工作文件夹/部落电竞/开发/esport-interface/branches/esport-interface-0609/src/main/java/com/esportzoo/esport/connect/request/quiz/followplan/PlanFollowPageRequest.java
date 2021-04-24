package com.esportzoo.esport.connect.request.quiz.followplan;

import com.esportzoo.esport.connect.request.BasePageRequest;
import lombok.Data;

/**
 * @Author lixisheng
 * @Date 2020/6/8 10:11
 */
@Data
public class PlanFollowPageRequest extends BasePageRequest {

    /** 推荐单方案ID*/
    private Long planId;

}
