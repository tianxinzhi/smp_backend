package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * @description:
 * @author: XiaoZhi
 * @create: 2020-09-03 09:30
 **/
@Data
@Table(name = "res_sku_repo_serial")
@Entity
//@SequenceGenerator(name="id_skuRepoSerial",sequenceName = "skuRepoSerial_seq",allocationSize = 1)
//@Where(clause = " active='Y'")
public class DbResSkuRepoSerial extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_skuRepoSerial")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_sku_repo_serial_gen")
    @TableGenerator(
            name = "res_sku_repo_serial_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_sku_repo_serial_pk",      //指定下次插入主键时使用默认的值
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

    @ManyToOne(targetEntity = DbResSkuRepo.class)
    @JsonBackReference
    @JoinColumn(name = "sku_repo_id")
    private DbResSkuRepo skuRepo;

}
