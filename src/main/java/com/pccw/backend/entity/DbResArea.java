package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;


/**
 *  one area may have many reposity/shop/store
 */

@Data
@Deprecated
@Entity
@Table(name = "res_area")
//@SequenceGenerator(name="id_area",sequenceName = "area_seq",allocationSize = 1)
public class DbResArea extends Base {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_area")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_area_gen")
    @TableGenerator(
            name = "res_area_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_area_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;
	// @Id
	// @GeneratedValue
	// private Long id;

	@Column(name = "area_name", length = 64)
	private String areaName;

	@Column(name = "area_desc", length = 256)
	private String areaDesc;

	// @Column(name = "status", length = 6)
	// private String status;

}
