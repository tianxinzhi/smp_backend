package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pccw.backend.annotation.JsonResultParamMapAnnotation;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/**
 * sku => product
 */

@Entity
@Table(name = "res_sku")
@Getter
@Setter
//@SequenceGenerator(name="id_sku",sequenceName = "sku_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "id",param2 = "skuCode",param3 = "serialControl")
public class DbResSku extends Base {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_sku")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_sku_gen")
	@TableGenerator(
			name = "res_sku_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_sku_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;

	@Column(name = "sku_code", length = 32)
	private String skuCode;

	@Column(name = "sku_desc", length = 512)
	private String skuDesc;

	@Column(name = "sku_name")
	private String skuName;

	@Column(name = "sku_origin",length = 32)
	private String skuOrigin;

	@Column(name = "start_date_active")
	private Long startDateActive;

	@Column(name = "end_date_active")
	private Long endDateActive;

	@Column(name = "tangible_item",length = 4)
	private String tangibleItem;

	@Column(name = "intangible_item",length = 4)
	private String intangibleItem;

	@Column(name = "intangible_item_no_balance",length = 4)
	private String intangibleItemNoBalance;

	@Column(name = "inventory_asset_flag",length = 4)
	private String inventoryAssetFlag;

	@Column(name = "returnable_flag",length = 4)
	private String returnableFlag;

	@Column(name = "max_reserve_days")
	private String maxReserveDays;

	@Column(name = "item_type")
	private String itemType;

	@Column(name = "serial_control",length = 8)
	private String serialControl;

	@Column(name = "uom")
	private String uom;
//	@JsonBackReference
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "sku",orphanRemoval = true)
//	private List<DbResSkuType> skuTypeList;
//
//	@JsonBackReference
//	@OneToMany(cascade = CascadeType.ALL,mappedBy = "sku",orphanRemoval = true)
//	private List<DbResSkuAttrValue> skuAttrValueList;


//	@OneToMany(cascade={CascadeType.ALL},mappedBy = "sku",orphanRemoval = true)
//	private List<DbResSkuRepo> skuRepoList;

//    @OneToOne(cascade = {CascadeType.ALL},mappedBy = "sku",orphanRemoval = true)
//    private DbResTypeSkuSpec dbResTypeSkuSpec;

	@JsonBackReference
//	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "sku_id")
	private List<DbResSpecAttr> skuAttrs;

	@OneToMany(cascade = {CascadeType.ALL},mappedBy = "sku",orphanRemoval = true)
	private List<DbResTypeSkuSpec> dbResTypeSkuSpec;
}
