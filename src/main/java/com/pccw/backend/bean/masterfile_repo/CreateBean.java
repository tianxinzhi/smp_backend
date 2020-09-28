package com.pccw.backend.bean.masterfile_repo;


import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Shop模块 - CreateBean",description="")
public class CreateBean extends BaseBean {

    @ApiModelProperty(value="门店编码",name="repoCode",example="")
    private String repoCode;

    @ApiModelProperty(value="门店名称",name="repoName",example="")
    private String repoName;

    @ApiModelProperty(value="门店地址",name="repoAddr",example="")
    private String repoAddr;

    @ApiModelProperty(value="areaId",name="areaId",example="")
    private Long areaId;

    @ApiModelProperty(value="仓库类型",name="repoType",example="")
    private String repoType;

    @ApiModelProperty(value="isClosed",name="isClosed",example="")
    private String isClosed;

    @ApiModelProperty(value="closedDay",name="closedDay",example="")
    private String closedDay;

    @ApiModelProperty(value="parentRepoId",name="parentRepoId",example="")
    private Long parentRepoId;
    
}