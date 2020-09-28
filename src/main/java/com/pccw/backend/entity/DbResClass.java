package com.pccw.backend.entity;

import java.util.List;


import javax.persistence.*;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import com.pccw.backend.annotation.JsonResultParamMapPro;
import lombok.Getter;
import lombok.Setter;


/**
 * class => category of sku
 */

@Getter
@Setter
@Entity
@Table(name = "res_class")
//@SequenceGenerator(name="id_class",sequenceName = "class_seq",allocationSize = 1)
//@JsonResultParamMapAnnotation(param1 = "id",param2 = "parentClassId",param3 = "className")
@JsonResultParamMapPro(fieldMapping = {"id=id","pid=parentClassId","name=className"})
public class  DbResClass extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_class")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_class_gen")
	@TableGenerator(
			name = "res_class_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_class_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;



	@Column(name = "parent_class_id", length = 11)
	private String parentClassId;

	@Column(name = "class_name", length = 32)
	private String className;

	@Column(name = "class_type", length = 128)
	private String classType;

	@Column(name = "class_desc", length = 128)
	private String classDesc;

//	@JsonIgnore
//	@JsonIgnoreProperties(value = { "classList" })
//	@ManyToMany(mappedBy = "classList")
//	private List<DbResType> typeList;

	@OneToMany(cascade={CascadeType.ALL},mappedBy = "classs",orphanRemoval = true)
	List<DbResClassType> relationOfTypeClass;


}
