package com.esportzoo.esport.connect.request.quiz.followplan;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

/**
 * @Author lixisheng
 * @Date 2020/6/9 16:46
 */
@Data
public class RecommendDetailRequest extends BaseRequest {

    /** 推荐单id  quiz_plan_recommend的id */
    private Long id;

}
