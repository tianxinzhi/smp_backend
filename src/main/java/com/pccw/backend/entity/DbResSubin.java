package com.pccw.backend.entity;


import lombok.Data;

import javax.persistence.*;


/**
 * repository => store/shop
 */
@Data
@Entity
@Table(name = "res_subin")
@Deprecated
//@SequenceGenerator(name="id_subin",sequenceName = "subin_seq",allocationSize = 1)
public class DbResSubin extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_subin")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_subin_gen")
	@TableGenerator(
			name = "res_subin_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_subin_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;

	@Column(name = "repo_id")
	private Long repoId;

	@Column(name = "subin_code")
	private String subinCode;

	@Column(name = "subin_type")
	private String subinType;

}
