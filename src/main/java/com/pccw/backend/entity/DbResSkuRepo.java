package com.pccw.backend.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

/*
 * which store, which sku , how many qty
 */


@Entity
@Table(name = "res_sku_repo")
@Getter
@Setter
//@SequenceGenerator(name="id_skuRepo",sequenceName = "skuRepo_seq",allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class DbResSkuRepo extends Base{
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_skuRepo")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_sku_repo_gen")
    @TableGenerator(
            name = "res_sku_repo_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_sku_repo_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@ManyToOne
	@JsonIgnoreProperties(value = { "skuRepoList" })
	@JoinColumn(name = "sku_id")
	private DbResSku sku;

	@ManyToOne
	@JsonIgnoreProperties(value = { "skuRepoList" })
	@JoinColumn(name = "repo_id")
	private DbResRepo repo;

//	@Column(name = "item_id")
//	private Long itemId;

	@ManyToOne
	@JsonIgnoreProperties(value = { "skuRepoList" })
	@JoinColumn(name = "stock_type_id")
	private DbResStockType stockType;

//	@OneToMany(cascade={CascadeType.ALL},mappedBy = "skuRepo",orphanRemoval = true)
//	private List<DbResSkuRepoItem> skuRepoItemList;

//	@Column(name="subin_id")
//	private String subinId;

	@Column(name = "qty")
	private Long qty;

	// remark
	@Column(name = "remark",length = 512)
	private String remark;

	@Column(name = "date_received")
	private Long dateReceived;

	@Column(name = "is_consigned",length = 8)
	private String isConsigned;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@JoinColumn(name = "sku_repo_id")
	private List<DbResSkuRepoSerial> serials;

}
