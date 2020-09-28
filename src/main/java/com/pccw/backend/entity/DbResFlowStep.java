package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * DbResFlowStep
 */

 @Entity
 @Data
 @Table(name = "res_flow_step")
// @SequenceGenerator(name="id_flowStep",sequenceName = "flowStep_seq",allocationSize = 1)
public class DbResFlowStep extends Base{
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_flowStep")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_flow_step_gen")
    @TableGenerator(
            name = "res_flow_step_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_flow_step_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;

    @Column(name="step_num")
    private String stepNum;

//    @Column(name = "role_id", columnDefinition = "number(11)")
    @Column(name = "role_id")
    private Long roleId;

    
}