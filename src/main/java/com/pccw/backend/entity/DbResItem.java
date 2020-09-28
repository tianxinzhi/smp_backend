package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;


/**
 *  one sku can include many items
 */

@Data
@Entity
@Table(name = "res_item")
@Deprecated
//@SequenceGenerator(name="id_item",sequenceName = "item_seq",allocationSize = 1)
public class DbResItem extends Base {


	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_item")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_item_gen")
	@TableGenerator(
			name = "res_item_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_item_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;


	@Column(name = "item_code", length = 128)
	private String itemCode;

	@Column(name = "item_name", length = 32)
	private String itemName;

	// A - avaliable, S - saled
	@Column(name = "item_status", length = 4)
	private String itemStatus;
}
