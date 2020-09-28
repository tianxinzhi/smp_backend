package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * sku => product
 */

@Entity
@Table(name = "res_spec_attr")
// @org.hibernate.annotations.Table(appliesTo = "res_spec_attr",comment = "spec data")
@Data
//@SequenceGenerator(name="id_specAttr",sequenceName = "specAttr_seq",allocationSize = 1)
public class DbResSpecAttr extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_specAttr")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_spec_attr_gen")
    @TableGenerator(
            name = "res_spec_attr_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_spec_attr_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

//	@Column(name = "ver_id", columnDefinition = "varchar(16)")
//	private String verId;

	@Column(name = "attr_id")
	private String attrId;

	@Column(name = "attr_value_id")
	private String attrValueId;

	@Column(name = "is_spec",length = 4)
	private String isSpec;
}
