package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 * sku => product
 */

@Entity
@Table(name = "res_sku_lis")
@Data
//@SequenceGenerator(name="id_skuLis",sequenceName = "skuLis_seq",allocationSize = 1)
//@JsonResultParamHandle(param1 = "id",param2 = "skuCode")
public class DbResSkuLis extends Base {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_skuLis")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_sku_lis_gen")
    @TableGenerator(
            name = "res_sku_lis_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_sku_lis_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "repo_id", length = 32)
	private Long repoId;

	private Long skuId;//外键关联res_sku

//	@JsonBackReference
//	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
//	@JoinColumn(name = "id")
//	@OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,mappedBy = "skuId")
//	@JoinColumn(name = "class_lis_id",referencedColumnName = "id")
//	private DbResClassLis classLisId;

	@Column(name = "sku_code")
	private String skuCode;

	@Column(name = "sku_name")
	private String skuName;

	@Column(name = "sku_desc")
	private String skuDesc;

	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "skuLis",orphanRemoval = true)
	private List<DbResSkuAttrValueLis> skuAttrValueLisList;

}
