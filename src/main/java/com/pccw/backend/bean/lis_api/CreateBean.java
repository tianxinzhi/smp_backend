package com.pccw.backend.bean.lis_api;

import com.pccw.backend.bean.BaseBean;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Description: com.pccw.batchjob.bean
 * @Author: qiang
 * @Date: 2019/9/16
 * @Version: 1.0
 */

@Data
@NoArgsConstructor
public class CreateBean{

    private String status;

    private String requestUrl;

    private String requestHeader;

    private String requestBody;

    private String responseResult;

    private Date createAt;

    private Date updateAt;

}
