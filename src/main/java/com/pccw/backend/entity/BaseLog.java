package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Base
 */

@Data
@MappedSuperclass
public class BaseLog extends Base {
       	// Transation Number made from SMP self
	@Column(name="log_txtNum",length = 512)
	private String logTxtBum;

	// O - Order / M - Mangement / R - Repl
	@Column(name="log_type",length = 4)
        private String logType;

    // ASG(Assign) / RET(return) / EXC(Exchange)
    // ARS(Advanced Reserve) / CARS(Cancel advance reserve) / APU(Advance pick up)
    // RREQ(Replenishment request)
    // SOTS(stock out to shop)/ SIFS(stock in from shop)
    // SOTW(stock out to warehouse)/ SIFW(stock in from warehouse)
    // SIWPO(stock in without PO)
    // ST(Stock Take) / STA(Stock Take Adjustment)
    // SCC(stock change category)
    @Column(name = "log_orderNature", length = 512)
    private String logOrderNature;

    @Column(name = "log_order_nature_desc", length = 512)
    private String logOrderNatureDesc;

    @Column(name = "log_order_nature_source")
    private String logOrderNatureSource;

    // W - waiting LIS to handle / D - Done
    @Column(name = "status",length = 4)
    private String status;

    // remark
    @Column(name = "remark",length = 512)
    private String remark;

    @Column(name = "courier")
    private String courier;

    @Column(name = "serial")
    private String serial;

    @Column(name = "iccID")
    private String iccID;

    @Column(name = "imei")
    private String imei;

}
