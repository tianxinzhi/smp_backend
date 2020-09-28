package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


/**
 * ROR = RESOURCE ORDER REQUEST
 */
@Data
@Entity
@Table(name = "res_log_mgt_dtl")
//@SequenceGenerator(name="id_logMgtDtl",sequenceName = "logMgtDtl_seq",allocationSize = 1)
public class DbResLogMgtDtl extends BaseLogDtl {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_logMgtDtl")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_mgt_dtl_gen")
    @TableGenerator(
            name = "res_log_mgt_dtl_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_log_mgt_dtl_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;



	@Column(name="dtl_skuId")
	private Long dtlSkuId;


	@Column(name="dtl_itemId")
	private Long dtlItemId;

	@Column(name="dtl_repoId")
	private Long dtlRepoId;

	@Valid
	@Min(1)
	@Column(name="dtl_qty")
	private long dtlQty;

	@Column(name = "item_code",length = 512)
	private String itemCode;

	@Column(name="detail_id")
	private String detailId;

	@JsonBackReference
	@JoinColumn(name = "log_mgt_id")
	@ManyToOne(targetEntity = DbResLogMgt.class)
	private DbResLogMgt resLogMgt;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)  //ALL  PERSIST
	@JoinColumn(name = "log_mgt_dtl_id")
	private List<DbResLogMgtDtlSerial> serials;

	@Transient
	private String skuCode;

}
