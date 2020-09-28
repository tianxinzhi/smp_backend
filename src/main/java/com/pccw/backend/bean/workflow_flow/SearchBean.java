package com.pccw.backend.bean.workflow_flow;


import com.pccw.backend.annotation.PredicateAnnotation;
import com.pccw.backend.annotation.PredicateType;
import com.pccw.backend.bean.BaseSearchBean;
import com.pccw.backend.entity.DbResFlowStep;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
public class SearchBean extends BaseSearchBean {

    @PredicateAnnotation(type = PredicateType.LIKE)
    private String flowName;

    @PredicateAnnotation(type = PredicateType.LIKE)
    private String flowDesc;

    private String flowNature;

    private long id;

    private List<Map> stepData;

    private List<DbResFlowStep> resFlowStepList;
    
}