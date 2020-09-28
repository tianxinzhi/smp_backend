package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * @description:
 * @author: XiaoZhi
 * @create: 2020-09-03 09:30
 **/
@Data
@Table(name = "res_log_mgt_dtl_serial")
@Entity
//@SequenceGenerator(name="id_mgtDtlSerial",sequenceName = "mgtDtlSerial_seq",allocationSize = 1)
@Where(clause = " active='Y'")
public class DbResLogMgtDtlSerial extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_mgtDtlSerial")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_mgt_dtl_serial_gen")
    @TableGenerator(
            name = "res_log_mgt_dtl_serial_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_log_mgt_dtl_serial_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name = "courier")
    private String courier;

    @Column(name = "serial")
    private String serial;

    @Column(name = "expiry_date")
    private Long expiryDate;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "log_mgt_dtl_id" )
    private DbResLogMgtDtl logMgtDtl;
}
