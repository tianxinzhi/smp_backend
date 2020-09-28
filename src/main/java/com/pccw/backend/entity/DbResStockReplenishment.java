package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @description:
 * @author: XiaoZhi
 * @create: 2020-08-30 09:30
 **/
@Data
@Table(name = "res_stock_replenishment")
@Entity
//@SequenceGenerator(name="id_stockReplenishment",sequenceName = "stockReplenishment_seq",allocationSize = 1)
public class DbResStockReplenishment extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_stockReplenishment")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_stock_replenishment_gen")
    @TableGenerator(
            name = "res_stock_replenishment_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_stock_replenishment_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name = "to_channel_id")
    private Long toChannelId;

//    @Column(name = "from_channel_name")
//    private String fromChannelName;
//
//    @Column(name = "to_channel_name")
//    private String toChannelName;

    @Column(name = "sku_id")
    private Long skuId;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "qty")
    private Long qty;

    @Column(name = "last_replenish")
    private Long lastReplenish;

    @Column(name = "suggested_qty_1")
    private Long suggestedQty1;

    @Column(name = "suggested_qty_2")
    private Long suggestedQty2;

    @Column(name = "suggested_qty_3")
    private Long suggestedQty3;

    @Column(name = "request_date")
    private Long requestDate;

    @Column(name = "log_txt_num", length = 512)
    private String logTxtNum;

}
