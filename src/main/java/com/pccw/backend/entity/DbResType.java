package com.pccw.backend.entity;


import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 *
 */

@Getter
@Setter
@Entity
@Table(name = "res_type")
//@SequenceGenerator(name="id_type",sequenceName = "type_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "id",param2 = "typeName")
public class DbResType extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_type")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_type_gen")
    @TableGenerator(
            name = "res_type_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_type_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;
	
	
	@Column(name = "type_code", length = 32)
	private String typeCode;

	@Column(name = "type_name", length = 32)
	private String typeName;

	@Column(name = "sequential",length = 32)
	private String sequential;

	@Column(name = "type_desc", length = 512)
	private String typeDesc;


//	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//	@JoinTable(name="res_class_type",
//			joinColumns = { @JoinColumn(name = "type_id") },
//			inverseJoinColumns = { @JoinColumn(name = "class_id") }
//	)
//	private List<DbResClass> classList;

	@OneToMany(cascade={CascadeType.ALL},mappedBy = "type",orphanRemoval = true)
	private List<DbResClassType> relationOfTypeClass;

	@OneToMany(cascade={CascadeType.ALL},mappedBy = "type",orphanRemoval = true)
	private List<DbResTypeSkuSpec> dbResTypeSkuSpecList;


}
