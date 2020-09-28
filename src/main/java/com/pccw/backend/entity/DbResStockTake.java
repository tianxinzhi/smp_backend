package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-30 09:30
 **/
@Data
@Table(name = "res_stock_take")
@Entity
//@SequenceGenerator(name="id_stockTake",sequenceName = "stockTake_seq",allocationSize = 1)
public class DbResStockTake extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_stockTake")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_stock_take_gen")
    @TableGenerator(
            name = "res_stock_take_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_stock_take_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name = "stockTakeNumber",length = 512)
    private String stockTakeNumber;

    @Column(name = "channelId")
    private Long channelId;

    @Column(name = "completeTime")
    private Long completeTime;

    /**
     * 单据填写状态：completed/draft
     */
    @Column(name = "fillStatus")
    private String fillStatus;

    // Y , N
    @Column(name = "display_quantity",length = 8)
    private String displayQuantity;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "resStockTake")  //ALL  PERSIST
//    @JoinColumn(name = "stock_take_id")
    private List<DbResStockTakeDtl> line;

}
