package com.esportzoo.esport.connect.request.helpcenter;


import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.cms.ReportType;
import com.esportzoo.esport.constants.user.UserReportType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 举报内容
 * @Author lixisheng
 * @Date 2020/4/14 17:11
 */
@Data
public class ReportRequest extends BaseRequest implements Serializable {

    private static final long serialVersionUID = -1985389919747986533L;

    /** 举报内容的类型 {@link ReportType} */
    private Integer reportType;
    /** 举报内容id */
    private Integer reportId;
//    /** 举报内容 */
//    private String content;
//    /** 是否有其他问题 */
//    private Boolean otherType;
    /** 其他问题 */
    private String otherProblems;
    /** 举报类型 {@link UserReportType} */
    private List<Integer> reportIndexList;

}
