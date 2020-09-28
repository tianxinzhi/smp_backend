package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "res_workorder")
@Deprecated
//@SequenceGenerator(name="id_workorder",sequenceName = "ccc_workorder",allocationSize = 1)
public class DbResWorkOrder extends Base {


	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_workorder")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_workorder_gen")
	@TableGenerator(
			name = "res_workorder_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_workorder_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;


	@Column(name = "wo_name", length = 255)
	private String woName;

	@Column(name = "wo_code", length = 255)
	private String woCode;

}
