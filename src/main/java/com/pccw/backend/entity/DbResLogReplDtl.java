package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;


/**
 * ROR = RESOURCE ORDER REQUES
 */
@Deprecated
@Data
@Entity
@Table(name = "res_log_repl_dtl")
//@SequenceGenerator(name="id_replDtl",sequenceName = "replDtl_seq",allocationSize = 1)
public class DbResLogReplDtl extends BaseLogDtl {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_replDtl")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_repl_dtl_gen")
    @TableGenerator(
            name = "res_log_repl_dtl_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_log_repl_dtl_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name="dtl_skuId")
	private long dtlSkuId;


	@Column(name="dtl_itemId")
	private long dtlItemId;

	@Column(name="dtl_repoId")
	private long dtlRepoId;

	@Column(name="dtl_qty")
	private long dtlQty;

	@ManyToOne
	@JoinColumn(name = "db_res_log_repl_id",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private DbResLogRepl dbResLogRepl;

}
