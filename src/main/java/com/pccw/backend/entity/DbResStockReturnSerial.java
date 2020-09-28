package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-30 09:59
 **/
@Data
@Table(name = "res_stock_return_serial")
@Entity
//@SequenceGenerator(name="id_stockReturnSerialDtl",sequenceName = "stockReturnSerial_seq",allocationSize = 1)
public class DbResStockReturnSerial extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_stockReturnSerialDtl")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_stock_return_serial_gen")
    @TableGenerator(
            name = "res_stock_return_serial_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_stock_return_serial_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name = "skuId")
    private Long skuId;

    @Column(name = "return_line")
    private String returnLineId;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "expiry_date")
    private Long expiryDate;

}
