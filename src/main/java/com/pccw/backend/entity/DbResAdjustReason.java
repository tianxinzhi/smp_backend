package com.pccw.backend.entity;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;

import javax.persistence.*;


/**
 * res_attr_value => product
 */

@Entity
@Table(name = "res_adjust_reason")
@Data
//@SequenceGenerator(name="id_adjustReason",sequenceName = "adjustReason_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "id",param2 = "adjustReasonName",param3 = "remark")
public class DbResAdjustReason extends Base{
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_adjustReason")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_adjust_reason_gen")
    @TableGenerator(
            name = "res_adjust_reason_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_adjust_reason_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "adjust_reason_name",length = 64)
	private String adjustReasonName;

	@Column(name = "remark",length = 512)
	private String remark;
}
