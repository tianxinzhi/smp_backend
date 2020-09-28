package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Deprecated
@Entity
@Table(name = "res_sku_type")
@Data
//@SequenceGenerator(name="id_sku_type",sequenceName = "sku_type_seq",allocationSize = 1)
public class DbResSkuType extends Base {

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sku_type")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_sku_type_gen")
    @TableGenerator(
            name = "res_sku_type_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_sku_type_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "sku_id")
    private DbResSku sku;

    @Column(name = "type_id")
    private long typeId;
}
