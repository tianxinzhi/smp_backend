package com.pccw.backend.bean.api_external.api_stock_in;

import com.pccw.backend.bean.api_external.CreateSIWPOBean;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-03 14:19
 **/

@Data
@NoArgsConstructor
public class CreateSIFSBean {

    private String dnNumber;

    private List<CreateSIWPOBean> batch;
}
