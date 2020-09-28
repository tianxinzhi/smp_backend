package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


/**
 * ROR = RESOURCE ORDER REQUEST
 */
@Deprecated
@Data
@Entity
@Table(name = "res_log_repl")
//@SequenceGenerator(name="id_repl",sequenceName = "repl_seq",allocationSize = 1)
public class DbResLogRepl extends BaseLog {


	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_repl")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_repl_gen")
    @TableGenerator(
            name = "res_log_repl_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_log_repl_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	// BatchId
	@Column(name="log_batchId")
	private long logBatchId;

	// DN Num
	@Column(name="log_dnNum")
	private long logDNNum;

	//warehouseID
	@Column(name="repo_id_from")
	private long repoIdFrom;

	//shop ID
	@Column(name="repo_id_to")
	private long repoIdTo;

	@OneToMany(cascade={CascadeType.ALL},mappedBy="dbResLogRepl")
	private List<DbResLogReplDtl> line;



}
