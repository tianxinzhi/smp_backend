package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "res_process")
//@SequenceGenerator(name = "id_process",sequenceName = "process_seq",allocationSize = 1)
public class DbResProcess extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_process")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_process_gen")
    @TableGenerator(
            name = "res_process_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_process_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;

    @Column(name="log_txtNum",length = 512)
    private String logTxtBum;

    @Column(name = "repo_id")
    private Long repoId;

    @Column(name = "status")
    private String status;

    @Column(name = "process_status")
    private String processStatus;

    @Column(name = "log_orderNature",length = 512)
    private String logOrderNature;

    @Column(name = "flow_id")
    private Long flowId;

    @Column(name = "remark",length = 512)
    private String remark;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "process_id")
    private List<DbResProcessDtl> processDtls;
}
