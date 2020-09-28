package com.pccw.backend.entity;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

/**
 *
 */

@Data
@Entity
@Table(name = "res_log_exception")
//@SequenceGenerator(name="id_log_exception",sequenceName = "log_exception_seq",allocationSize = 1)
@AllArgsConstructor
public class DbResLogException {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_log_exception")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_exception_gen")
    @TableGenerator(
            name = "res_log_exception_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_log_exception_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "log_exception_code",length = 32)
	private String code;

	@Column(name = "log_exception_msg",length = 4000)
	private String msg;

//	@Column(name = "log_exception_fileName", length = 64)
//	private String fileName;

//	@Column(name = "log_exception_methodName", length = 64)
//	private String methodName;

	@Column(name = "log_exception_reqUri", length = 64)
	private String reqUri;

	@Column(name="create_at")
	private Long createAt;

	@Column(name="create_by")
	private Long createBy;
}
