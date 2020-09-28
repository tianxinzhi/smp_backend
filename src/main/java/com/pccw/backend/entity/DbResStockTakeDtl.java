package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-30 09:59
 **/
@Data
@Table(name = "res_stock_take_dtl")
@Entity
//@SequenceGenerator(name="id_stockTakeDtl",sequenceName = "stockTakeDtl_seq",allocationSize = 1)
public class DbResStockTakeDtl extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_stockTakeDtl")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_stock_take_dtl_gen")
    @TableGenerator(
            name = "res_stock_take_dtl_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_stock_take_dtl_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name = "skuId")
    private Long skuId;

    @Column(name = "stockTakeOne")
    private String stockTakeOne;

    @Column(name = "stockTakeTwo")
    private String stockTakeTwo;

    @Column(name = "stockTakeThree")
    private String stockTakeThree;

    @Column(name = "stockTakeBalance")
    private String stockTakeBalance;

    @Column(name = "current_balance")
    private Long currentBalance;

    @Column(name = "difference")
    private String difference;

    @JsonBackReference
    @JoinColumn(name = "stock_take_id")
    @ManyToOne(targetEntity = DbResStockTake.class)
    private DbResStockTake resStockTake;
}
