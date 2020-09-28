package com.pccw.backend.bean.stock_take;

import com.pccw.backend.bean.BaseBean;
import com.pccw.backend.entity.DbResStockTakeDtl;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-30 10:45
 **/
@Data
public class CreateBean extends BaseBean {

    private Long completeTime;

    private Long channelId;

    private String stockTakeNumber;

    private String fillStatus;

    private String displayQuantity;

    private List<DbResStockTakeDtl> line;
}
