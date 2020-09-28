package com.pccw.backend.entity;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;

import javax.persistence.*;


/**
 * res_attr_value => product
 */

@Entity
@Table(name = "res_attr_attr_value")
@Data
//@SequenceGenerator(name="id_attrAttrValue",sequenceName = "attrAttrValue_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "attr",param2 = "attrValue")
public class DbResAttrAttrValue extends Base{

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_attrAttrValue")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_attr_attr_value_gen")
    @TableGenerator(
            name = "res_attr_attr_value_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_attr_attr_value_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@ManyToOne
	@JoinColumn(name = "attr_id" )
	private DbResAttr attr;

	@ManyToOne
	@JoinColumn(name = "attr_value_id")
	private DbResAttrValue attrValue;

}
