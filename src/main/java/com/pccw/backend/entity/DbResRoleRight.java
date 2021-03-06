package com.pccw.backend.entity;


import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "res_role_right")
@Data
//@SequenceGenerator(name="id_roleRight",sequenceName = "roleRight_seq",allocationSize = 1)
public class DbResRoleRight extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_roleRight")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_role_right_gen")
    @TableGenerator(
            name = "res_role_right_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_role_right_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "right_id")
	private Long rightId;
	

	
}
