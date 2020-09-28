package com.pccw.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;


/**
 * res_attr_value => product
 */

@Entity
@Table(name = "res_reservation")
@Data
//@SequenceGenerator(name="id_reservation",sequenceName = "reservation_seq",allocationSize = 1)
//@JsonResultParamMapAnnotation(param1 = "id",param2 = "attrValue")
@Where(clause = "active ='Y'")
public class DbResReservation extends Base{
	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_reservation")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_reservation_gen")
	@TableGenerator(
			name = "res_reservation_gen",
			table="fendo_generator",
			pkColumnName="seq_name",     //指定主键的名字
			pkColumnValue="res_reservation_pk",      //指定下次插入主键时使用默认的值
			valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
			//initialValue = 1,            //初始化值
			allocationSize=1             //累加值
	)
	private Long id;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "order_number")
	private String orderNo;

	@Column(name = "log_txtNum", length = 512)
	private String logTxtBum;

	@Column(name = "customer_type")
	private String customerType;

	@Column(name = "sku_id")
	private Long skuId;

	@Column(name = "repo_id")
	private Long repoId;

	@Column(name = "subinventory_id")
	private Long subinventoryID;

	@Column(name = "subinventory")
	private String subinventory;

	@Column(name = "qty")
	private Long qty;

	@Column(name = "staff_number")
	private String staffId;

	@Column(name = "reservation_date")
	private Long reservationDate;

	@Column(name = "order_date")
	private Long orderDate;

	@Column(name = "payment_status")
	private String paymentStatus;

	@Column(name = "days")
	private Long days;

	// Y,N
	@Column(name = "selected")
	private String selected;

	@Column(name = "remark")
	private String remark;

	@Column(name = "reason")
	private String reason;

	@Column(name = "courier")
	private String courier;

	@Column(name = "serial")
	private String serial;

	@Column(name = "serial_type")
	private String serialType;

	@Column(name = "iccID")
	private String iccID;

	@Column(name = "imei")
	private String imei;

	@Column(name = "mobile_number")
	private String mobileNumber;
}
