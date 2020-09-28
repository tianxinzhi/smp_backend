package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;


/**
 *  one area may have many reposity/shop/store
 */

@Data
@Entity
@Table(name = "res_account_role")
//@SequenceGenerator(name="id_account_role",sequenceName = "accountRole_seq",allocationSize = 1)
public class DbResAccountRole extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_account_role")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_account_role_gen")
    @TableGenerator(
            name = "res_account_role_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_account_role_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "role_id")
	private Long roleId;

//	@ManyToOne
//	@JsonBackReference
//	@JoinColumn(name = "account_id")
//	private DbResAccount account;
	
}
