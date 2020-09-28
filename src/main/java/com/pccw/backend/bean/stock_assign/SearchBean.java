package com.pccw.backend.bean.stock_assign;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;


/**
 * SearchCondition
 */

@Data
@NoArgsConstructor
public class SearchBean extends BaseSearchBean implements Serializable{

    @PredicateAnnotation(type = PredicateType.LIKE)
    private String skuNum;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    @NotEmpty
    private String repoNum;

}