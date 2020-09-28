package com.pccw.backend.bean.stock_balance;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import com.pccw.backend.entity.DbResRepo;
import com.pccw.backend.entity.DbResSku;
import com.pccw.backend.entity.DbResStockType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;


/**
 * SearchCondition
 */

@Data
@NoArgsConstructor
public class SearchBean extends BaseSearchBean implements Serializable{

    @PredicateAnnotation(type = PredicateType.LIKE)
    private List<String> skuNum; // same as entity property and relative to the data clomun

    @PredicateAnnotation(type = PredicateType.LIKE)
    private String itemNum;

    @PredicateAnnotation(type = PredicateType.LIKE)
    private String skuDesc;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    @NotEmpty
    private String repoNum;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    private long stockTypeId;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    private long shopId;


}
