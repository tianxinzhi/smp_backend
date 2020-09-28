package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Base
 */

@Data
@MappedSuperclass
public class BaseLogDtl extends Base {
	// Transation Number made from SMP self
	@Column(name = "log_txtNum", length = 512)
	private String logTxtBum;

	@Column(name = "dtl_logId")
	private long dtlLogId;

	// A - Add / D - Deduct
	@Column(name = "dtl_action", length = 4)
	private String dtlAction;

	// Good / Faulty / Intran
	@Column(name = "dtl_subin", length = 32)
		private String dtlSubin;

	// W - waiting LIS to handle / D - Done
	@Column(name = "lis_status", length = 4)
	private String lisStatus;

	// result from LIS
	@Column(name = "lis_result", length = 512)
	private String lisResult;

	// AVL(available)/DEM(demo)/RES(reserve)/ARE(ao_reserve)
	// FAU(faulty)
	// INT(ondelivery/intransit)
	@Column(name = "status", length = 4)
	private String Status;

	// Shop code mapping
	@Column(name = "ccc", length = 512)
	private String ccc;

	//  Work order
	@Column(name = "wo", length = 512)
	private String wo;
}
