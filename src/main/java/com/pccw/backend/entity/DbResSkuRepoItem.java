package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Deprecated
@Entity
@Table(name = "res_skuRepoItem")
//@SequenceGenerator(name="id_skuRepoItem",sequenceName = "skuRepoItem_seq",allocationSize = 1)
public class DbResSkuRepoItem extends Base{

    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_skuRepoItem")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_skuRepoItem_gen")
    @TableGenerator(
            name = "res_skuRepoItem_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_skuRepoItem_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;


    @Column(name = "item_code", length = 128)
    private String itemCode;

    @ManyToOne
    @JsonIgnoreProperties(value = { "skuRepoItemList" })
    @JoinColumn(name = "res_sku_repo_id")
    private DbResSkuRepo skuRepo;

}
