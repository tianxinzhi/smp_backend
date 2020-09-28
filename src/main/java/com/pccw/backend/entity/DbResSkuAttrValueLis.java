package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="res_sku_attr_value_lis")
@Data
//@SequenceGenerator(name="id_sku_attr_value_lis",sequenceName = "sku_attr_value_Lis_seq",allocationSize = 1)
public class DbResSkuAttrValueLis extends Base {

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sku_attr_value_lis")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_sku_attr_value_lis_gen")
    @TableGenerator(
            name = "res_sku_attr_value_lis_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_sku_attr_value_lis_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "sku_attr_value_id",referencedColumnName = "id")
//    private DbResSkuAttrValue skuAttrValueId;//外键

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "sku_lis_id")
    private DbResSkuLis skuLis;


    @Column(name = "attr_name")
    private String attrName;

    @Column(name = "attr_value")
    private String attrValue;
}
