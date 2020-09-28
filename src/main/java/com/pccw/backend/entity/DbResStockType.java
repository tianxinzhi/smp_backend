package com.pccw.backend.entity;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/**
 * res_attr_value => product
 */
@Getter
@Setter
@Entity
@Table(name = "res_stock_type")
//@SequenceGenerator(name="id_stockType",sequenceName = "stockType_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "id",param2 = "stockTypeName")
public class DbResStockType extends Base{
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_stockType")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_stock_type_gen")
    @TableGenerator(
            name = "res_stock_type_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_stock_type_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "stocktype_name",length = 64)
	private String stockTypeName;

	@Column(name = "disable_date")
	private Long disableDate;

	@OneToMany(cascade={CascadeType.ALL},mappedBy = "stockType")
	private List<DbResSkuRepo> skuRepoList;


}
