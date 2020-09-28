package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "res_process_dtl")
//@SequenceGenerator(name = "id_process_dtl",sequenceName = "process_dtl_seq",allocationSize = 1)
public class DbResProcessDtl extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_process_dtl")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_process_dtl_gen")
    @TableGenerator(
            name = "res_process_dtl_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_process_dtl_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "step_id")
    private Long stepId;

    @Column(name="step_num")
    private String stepNum;

//    @Column(name = "role_id", columnDefinition = "number(11)")
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "status")
    private String status;

    @Column(name = "remark",length = 512)
    private String remark;

}
