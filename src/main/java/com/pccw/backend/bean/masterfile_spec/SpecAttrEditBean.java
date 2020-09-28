package com.pccw.backend.bean.masterfile_spec;


import com.pccw.backend.entity.DbResSpecAttr;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Spec模块 - EditBean",description="")
public class SpecAttrEditBean extends DbResSpecAttr {

    private Long specId;

}