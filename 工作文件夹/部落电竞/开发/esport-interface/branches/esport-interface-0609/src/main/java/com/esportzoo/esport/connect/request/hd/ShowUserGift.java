package com.esportzoo.esport.connect.request.hd;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ShowUserGift
 * @Description
 * @Author jiajing.he
 * @Date 2019/9/24  09:45
 * @Version 1.0
 **/
@Data
public class ShowUserGift extends BaseRequest implements Serializable {
    private static final long serialVersionUID = -5531327810482568355L;

    /**
     * 是否展示当前用户获奖礼品信息  false为展示全部用户 true 当前用户
     */
    private Boolean showUser=Boolean.FALSE;

    private Long hdId;
}
