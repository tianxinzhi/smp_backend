package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 *  one area may have many reposity/shop/store
 */

@Data
@Entity
@Table(name = "res_account")
//@SequenceGenerator(name="id_account",sequenceName = "account_seq",allocationSize = 1)
public class DbResAccount extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_account")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_account_gen")
    @TableGenerator(
            name = "res_account_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_account_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "account_name",length = 255)
	private String accountName;
	
	@Column(name = "account_password",length = 512)
	private String accountPassword;

	@JsonBackReference
	@JoinColumn(name = "account_id")
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
	private List<DbResAccountRole> accountRoles;

}
