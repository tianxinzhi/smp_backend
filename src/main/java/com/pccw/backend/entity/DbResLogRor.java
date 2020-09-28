package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/**
 * ROR = RESOURCE ORDER REQUEST
 */
@Getter
@Setter
@Entity
@Table(name = "res_log_ror")
@AllArgsConstructor
@NoArgsConstructor
//@SequenceGenerator(name="id_logRor",sequenceName = "logRor_seq",allocationSize = 1)
@Deprecated
public class DbResLogRor extends BaseLog {
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_logRor")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_log_ror_gen")
    @TableGenerator(
            name = "res_log_ror_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_log_ror_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    // POS / BOM / BES
    @Column(name = "log_sys",length = 8)
    private String logSys;

    // OrderId
    @Column(name="log_orderId",length = 512)
    private String logOrderId;

    // Related OrderId
    @Column(name="log_relatedOrderId",length = 512)
    private String logRelatedOrderId;

    // N(normal) / A(Advance Order)
    @Column(name="log_orderType",length = 1)
    private String logOrderType;

    @Column(name="sales_id",length = 10)
    private String salesId;

    @Column(name="tx_date",length = 20)
    private String txDate;

    @Column(name="biz_date",length = 20)
    private String bizDate;

    @Column(name="reserved_by_employee")
    private String reservedByEmployee;

    @Column(name="unreserved_by_employee")
    private String unReservedByEmployee;

    @Column(name="reservation_days")
    private long reservationDays;

    @Column(name="reservation_date")
    private long reservationDate;

    @Column(name="serial_type")
    private String serialType;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "log_ror_id")
    private List<DbResLogRorDtl> line;


}
