package com.pccw.backend.bean.stock_movement;


import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value = "Process 模块 - SearchBean", description = "")
public class SearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="状态",name="status",example="")
    private String status;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="Nature",name="logOrderNature",example="")
    private String logOrderNature;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="商店Id",name="repoId",example="")
    private Long repoId;

    @PredicateAnnotation(type = PredicateType.BETWEEN)
    @ApiModelProperty(value="时间范围",name="creatAt",example="")
    private String[] createAt;

//    @PredicateAnnotation(type = PredicateType.LIKE)
    private List<String> skuNum; // same as entity property and relative to the data clomun

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="商店Id",name="toRepoId",example="")
    private Long toRepoId;

    @NotNull
    private Integer pageIndex=0;
    @NotNull
    private Integer pageSize=10;

    private long createBy;
    private long updateAt;
    private long updateBy;
    private String active;


}
