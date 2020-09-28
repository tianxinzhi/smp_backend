package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Deprecated
@Entity
@Table(name = "res_ccc")
//@SequenceGenerator(name="id_ccc",sequenceName = "ccc_seq",allocationSize = 1)
public class DbResCcc extends Base {


	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_ccc")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_ccc_gen")
    @TableGenerator(
            name = "res_ccc_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_ccc_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;


	@Column(name = "ccc_name", length = 255)
	private String cccName;

	@Column(name = "ccc_code", length = 255)
	private String cccCode;

}
