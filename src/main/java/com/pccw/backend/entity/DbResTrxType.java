package com.pccw.backend.entity;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import com.pccw.backend.annotation.JsonResultParamMapPro;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 * trx_type
 */
@Data
@Entity
@Table(name = "res_trx_type")
//@SequenceGenerator(name="id_trxType",sequenceName = "trxType_seq",allocationSize = 1)
//@JsonResultParamMapAnnotation(param1 = "id",param2 = "transactionTypeName",param3 = "natureType",param4 = "serialControl")
@JsonResultParamMapPro(fieldMapping = {"value=id","label=transactionTypeName","other=natureType","flag=serialControl"})
public class DbResTrxType extends Base {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_trxType")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_trx_type_gen")
	@TableGenerator(
			name = "res_trx_type_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_trx_type_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;

	@Column(name="transaction_type_name")
	private String transactionTypeName;

	@Column(name="description")
	private String description;

	@Column(name="transaction_action")
	private String transactionAction;

	@Column(name="transaction_source")
	private String transactionSource;

	@Column(name="disable_date")
	private Long disableDate;

	@Column(name = "serial_control",length = 8)
	private String serialControl;

	@Column(name = "nature_type")
	private String natureType;
}
