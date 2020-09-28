package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 * DbResSpec
 */
@Data
@Entity
@Table(name = "res_spec")
//@SequenceGenerator(name="id_spec",sequenceName = "spec_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "id",param2 = "specName",param3 = "verId")
public class DbResSpec extends Base {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_spec")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_spec_gen")
    @TableGenerator(
            name = "res_spec_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_spec_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "spec_name",length = 64)
	private String specName;

	@Column(name="spec_desc",length = 512)
	private String specDesc;

	@Column(name="ver_id",length = 512)
	private String verId;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true) //,orphanRemoval = true  ,fetch=FetchType.EAGER
	@JoinColumn(name = "spec_id")
	private List<DbResSpecAttr> resSpecAttrList;

}
