package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


/**
 * class => category of sku
 */

@Getter
@Setter
@Entity
@Table(name = "res_class_type")
//@SequenceGenerator(name="id_class_type",sequenceName = "class_type_seq",allocationSize = 1)
public class DbResClassType extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_class_type")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_class_type_gen")
    @TableGenerator(
            name = "res_class_type_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_class_type_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@ManyToOne
	@JsonIgnoreProperties(value = { "relationOfTypeClass" })
	@JoinColumn(name = "class_id")
	private DbResClass classs;

	@ManyToOne
	@JsonIgnoreProperties(value = { "relationOfTypeClass" })
	@JoinColumn(name = "type_id")
	private DbResType type;
	

}
