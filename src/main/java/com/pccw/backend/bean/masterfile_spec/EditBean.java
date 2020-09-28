package com.pccw.backend.bean.masterfile_spec;




import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Spec模块 - EditBean",description="")
public class EditBean extends BaseBean {

    private Long id;
    @ApiModelProperty(value="规格名",name="specName",example="")
    private String specName;

    @ApiModelProperty(value="规格描述",name="specDesc",example="")
    private String specDesc;

    @ApiModelProperty(value="版本ID",name="verId",example="")
    private String verId;

    @ApiModelProperty(value="是否有效",name="active",example="")
    private String active;

    @ApiModelProperty(value="dtlList",name="dtlList",example="")
    private List<SpecAttrEditBean> resSpecAttrList;


}