package com.esportzoo.esport.vo.user.open;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Description 锦鲤商城h5地址请求对象
 * @Author zheng.lin
 * @Date 2020/6/3 17:05
 */
@Data
public class KoiH5UrlRequestVO {

    /** 会员ID，长度限8至32位 */
    @JSONField(name = "member_id")
    private String memberId;
    /** 真实姓名 */
    @JSONField(name = "member_name")
    private String memberName;
    /** 身份证号 */
    @JSONField(name = "identity_number")
    private String identityNumber;
    /** 手机号码 */
    @JSONField(name = "mobile")
    private String mobile;
    /** 消息通知地址 */
    @JSONField(name = "notify_url")
    private String notifyUrl;

}
