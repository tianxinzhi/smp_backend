package com.pccw.backend.entity;

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
@Table(name = "res_stock_replenishment_header")
@Entity
//@SequenceGenerator(name="id_stockReplenishmentHeader",sequenceName = "stockReplenishmentHeader_seq",allocationSize = 1)
@Where(clause = " active='Y'")
public class DbResStockReplenishmentHeader extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_stockReplenishmentHeader")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_stock_replenishment_header_gen")
    @TableGenerator(
            name = "res_stock_replenishment_header_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_stock_replenishment_header_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name = "from_channel_id")
    private Long fromChannelId;

    @Column(name = "log_txt_num", length = 512)
    private String logTxtNum;

    @Column(name = "status")
    private String status;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "replenishment_header_id")
    private List<DbResStockReplenishment> line;
}
