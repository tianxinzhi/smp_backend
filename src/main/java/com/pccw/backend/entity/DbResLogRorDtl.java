package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


/**
 * ROR = RESOURCE ORDER REQUEST
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "res_log_ror_dtl")
//@SequenceGenerator(name="id_logRorDtl",sequenceName = "logRorDtl_seq",allocationSize = 1)
@Deprecated
public class DbResLogRorDtl extends BaseLogDtl {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_logRorDtl")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_ror_dtl_gen")
	@TableGenerator(
			name = "res_log_ror_dtl_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_log_ror_dtl_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;

	@Column(name="dtl_skuId")
	private Long dtlSkuId;
//	private String dtlSkuId;

	@Column(name="dtl_itemId")
	private Long dtlItemId;
//	private String dtlItemId;

	@Column(name="dtl_repoId")
	private Long dtlRepoId;
//	private String dtlRepoId;

	@Column(name="dtl_qty")
	private Long dtlQty;

//	@Column(name="ccc")
//	private String ccc;
//
//	@Column(name="wo")
//	private String wo;

	@Column(name="detail_id")
	private String detailId;

	@JsonBackReference
	@JoinColumn(name = "log_ror_id")
	@ManyToOne(targetEntity = DbResLogRor.class)
	private DbResLogRor resLogRor;
}
