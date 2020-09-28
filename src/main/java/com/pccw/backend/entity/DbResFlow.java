package com.pccw.backend.entity;

import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;

/**
 * DbResFlow
 */

 @Entity
 @Data
 @Table(name = "res_flow")
// @SequenceGenerator(name="id_flow",sequenceName = "flow_seq",allocationSize = 1)
 @JsonResultParamMapAnnotation(param1 = "flowNature",param2 = "flowName")
public class DbResFlow extends Base{
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_flow")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_flow_gen")
    @TableGenerator(
            name = "res_flow_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_flow_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;

    @Column(name="flow_name")
    private String flowName;

    @Column(name="flow_desc")
    private String flowDesc;

    @Column(name="flow_nature")
    private String flowNature;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "flow_id")
    private List<DbResFlowStep> resFlowStepList;

    
}