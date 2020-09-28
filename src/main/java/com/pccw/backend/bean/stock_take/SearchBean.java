package com.pccw.backend.bean.stock_take;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import lombok.Data;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-30 10:46
 **/
@Data
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    private String stockTakeNumber;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    private Long channelId;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    private String fillStatus;
}
