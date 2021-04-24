package com.esportzoo.esport.connect.request.matchtool;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.tool.ToolMatchEnter;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author lixisheng
 * @Date 2019/11/20 09:48
 */
@Data
public class MatchToolHomeRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = -9101453226805358719L;
    private Integer pageNo = 1;
    private Integer pageSize = 10;

    /**状态0无效，1未开赛，2报名中，3比赛中，4已结束... {@link com.esportzoo.esport.constants.tool.ToolMatchStatus } */
    private Integer status;
    /**
     *赛事等级：0默认，1初级，2高级，3顶级 {@link com.esportzoo.esport.constants.tool.ToolMatchLevel}
     */
    private Integer matchLevel;
    /** 报名开关：0关闭，1开放  {@link ToolMatchEnter}*/
    private Integer isEnter;
    // 报名截止时间
    private Date deadline;
    // 开始时间
    private Date startTime;
    // 结束时间
    private Date endTime;
    // 创建人
    private String createUser;
    // 创建人id
    private Integer createUserId;
    /**showUserMacth 是否只展示当前userid用户的有关联的赛事*/
    private Boolean showUserMacth;
}
