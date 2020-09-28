package com.pccw.backend.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * class => category of sku
 */

@Getter
@Setter
@Deprecated
@Entity
@Table(name = "res_class_lis")
//@SequenceGenerator(name="id_classLis",sequenceName = "classLis_seq",allocationSize = 1)
//@JsonResultParamHandle(param1 = "id",param2 = "parentClassId",param3 = "className")
public class DbResClassLis extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_classLis")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_class_lis_gen")
    @TableGenerator(
            name = "res_class_lis_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_class_lis_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "class_desc", length = 128)
	private String classDesc;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "class_id",referencedColumnName = "id")
	private DbResClass classId;//外键

//	@JsonBackReference
//	@OneToMany(cascade={CascadeType.ALL},mappedBy = "classLisId",orphanRemoval = true)
//	List<DbResSkuAttrValueLis> attrValueLisList;


}
