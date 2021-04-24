package com.esportzoo.esport.connect.request.cms;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

/**
 * @author tingjun.wang
 * @date 2019/12/25 9:58上午
 */
@Data
public class CmsMsgRequest extends BaseRequest{
	private Integer pageNo;
	private Integer pageSize;
}
