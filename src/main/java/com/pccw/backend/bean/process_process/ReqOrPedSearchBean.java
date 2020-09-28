package com.pccw.backend.bean.process_process;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 查询自己申请或需要自己审批的bean
 * @author ChenShuCheng
 * @create: 2019-12-05 11:00
 **/

@Data
@NoArgsConstructor
@ApiModel(value = "Process 模块 - MyRequestSearchBean", description = "")
public class ReqOrPedSearchBean {

    @ApiModelProperty(value="时间区间",name="date",example="")
    private Date[] date;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    @ApiModelProperty(value="业务类型编码",name="logOrderNature",example="")
    private String logOrderNature;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    @ApiModelProperty(value="商店id",name="repoId",example="")
    private Long repoId;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="Txtnumber",name="logTxtBum",example="")
    private String logTxtBum;

    //@PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="filter",name="filter",example="")
    private String filter;

    @PredicateAnnotation(type = PredicateType.BETWEEN)
    @ApiModelProperty(value="数据库中的时间查询范围",name="createAt",example="")
    private String[] createAt;

    @PredicateAnnotation(type = PredicateType.EQUEL)
    @ApiModelProperty(value="操作人的id",name="createBy",example="")
    private long createBy;
}
