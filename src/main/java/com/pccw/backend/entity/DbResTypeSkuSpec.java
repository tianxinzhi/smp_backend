package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;


/**
 * repository => store/shop
 */
@Data
@Entity
@Table(name = "res_type_sku_spec")
//@SequenceGenerator(name="id_tsp",sequenceName = "tsp_seq",allocationSize = 1)
public class DbResTypeSkuSpec extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_tsp")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_type_sku_spec_gen")
    @TableGenerator(
            name = "res_type_sku_spec_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_type_sku_spec_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

//	@Column(name = "type_id",length = 64)
//	private long typeId;

	@JoinColumn(name="sku_id")
//	@OneToOne
	@ManyToOne
	@JsonBackReference
	private DbResSku sku;

	@Column(name="spec_id",length = 512)
	private long specId;

	@Column(name="is_type",length = 4)
	private String isType;

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "type_id")
	private DbResType type;





}
