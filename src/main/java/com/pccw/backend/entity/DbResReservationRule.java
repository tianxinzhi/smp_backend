package com.pccw.backend.entity;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;

import javax.persistence.*;


/**
 * res_attr_value => product
 */

@Entity
@Table(name = "res_reservation_rule")
@Data
//@SequenceGenerator(name="id_reservation_rule",sequenceName = "reservationRule_seq",allocationSize = 1)
//@JsonResultParamMapAnnotation(param1 = "id",param2 = "attrValue")
public class DbResReservationRule extends Base{
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_reservation_rule")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_reservation_rule_gen")
	@TableGenerator(
			name = "res_reservation_rule_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_reservation_rule_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;

	@Column(name = "customer_type")
	private String customerType;

	@Column(name = "sku_id")
	private Long skuId;

	@Column(name = "payment_status")
	private String paymentStatus;

	@Column(name = "priority")
	private int priority;

}
