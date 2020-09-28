package com.pccw.backend.bean;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * SearchCondition
 */

@Data
@NoArgsConstructor
public class BaseSearchBean extends BaseBean {


    @NotNull
    private Integer pageIndex=0;
    @NotNull
    private Integer pageSize=10;



}