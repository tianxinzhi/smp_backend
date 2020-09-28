package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/**
 * ROR = RESOURCE ORDER REQUEST
 */
@Getter
@Setter
@Entity
@Table(name = "res_log_mgt")
//@SequenceGenerator(name="id_logMgt",sequenceName = "logMgt_seq",allocationSize = 1)
public class DbResLogMgt extends BaseLog {
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_logMgt")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_mgt_gen")
    @TableGenerator(
            name = "res_log_mgt_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_log_mgt_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	@Column(name = "adjust_reason_id")
	private Long adjustReasonId;

	@Column(name = "delivery_date")
	private Long deliveryDate;

	@Column(name = "delivery_status")
	private String deliveryStatus;

    @Column(name = "delivery_number")
    private String deliveryNumber;

	@Column(name = "staff_number")
	private String staffNumber;

	@Column(name = "source_system")
	private String sourceSystem;

	@Column(name = "source_txn_header")
	private String sourceTxnHeader;

	@Column(name = "source_line")
	private String sourceTxnLine;

	// OrderId
	@Column(name="log_orderId",length = 512)
	private String logOrderId;

	// Related OrderId
	@Column(name="log_relatedOrderId",length = 512)
	private String logRelatedOrderId;

	// N(normal) / A(Advance Order)
	@Column(name="log_orderType",length = 1)
	private String logOrderType;

	@Column(name="tx_date",length = 20)
	private String txDate;

	@Column(name = "mobile_number")
	private String mobileNumber;

	// Repo In
	@Column(name="log_repo_in")
	private long logRepoIn;

	// Repo Out
	@Column(name="log_repo_out")
	private long logRepoOut;

	@Column(name="approval")
	private String approval;

	@Column(name="approval_by")
	private String approvalBy;

	@Column(name="publish_to_lis")
	private String pubToLis;

	@Column(name="receive_from_lis")
	private String recFromLis;

	@Column(name="trx_type_id")
	private Long trxTypeId;

	@JsonManagedReference
	@OneToMany(cascade = CascadeType.ALL)  //ALL  PERSIST
	@JoinColumn(name = "log_mgt_id")
    private List<DbResLogMgtDtl> line;

}
