package com.pccw.backend.bean.auth_account;

import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Account 模块 - SearchBean", description = "")
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.EQUEL)
    private Long id;

    private Long[] roles;

    private String[] roleNames;

    private String accountPassword;

    @PredicateAnnotation(type = PredicateType.LIKE)
    @ApiModelProperty(value="账号",name="accountName",example="")
    private String accountName;

}
