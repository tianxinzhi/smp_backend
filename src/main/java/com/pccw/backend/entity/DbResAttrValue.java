package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 * res_attr_value => product
 */

@Entity
@Table(name = "res_attr_value")
@Data
//@SequenceGenerator(name="id_attrValue",sequenceName = "attrValue_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "id",param2 = "attrValue")
public class DbResAttrValue extends Base{
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_attrValue")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_attr_value_gen")
    @TableGenerator(
            name = "res_attr_value_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_attr_value_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "attr_value",columnDefinition = "varchar(128) ")
	private String attrValue;

	@Column(name = "unit_of_measure", columnDefinition = "varchar(16)  ")
	private String unitOfMeasure;

	@Column(name = "value_from", columnDefinition = "varchar(128)")
	private String valueFrom;

	@Column(name = "value_to", columnDefinition = "varchar(128)")
	private String valueTo;

	@JsonBackReference
	@JsonIgnoreProperties(value = { "attrAttrValueList" })
	@OneToMany(mappedBy = "attrValue",cascade = CascadeType.ALL)
	private List<DbResAttrAttrValue> attrAttrValueList;


}
