package com.pccw.backend.entity;

import java.util.List;

import javax.persistence.*;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Getter;
import lombok.Setter;


/**
 * repository => store/shop
 */
@Getter
@Setter
@Entity
@Table(name = "res_repo")
//@SequenceGenerator(name="id_repo",sequenceName = "repo_seq",allocationSize = 1)
@JsonResultParamMapAnnotation(param1 = "id",param2 = "repoCode",param3 = "repoType")
public class DbResRepo extends Base {

	@Id
	@Column(name = "id")
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_repo")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="res_repo_gen")
    @TableGenerator(
            name = "res_repo_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_repo_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
	private Long id;

	// @ManyToOne(cascade= {CascadeType.ALL})
	// @JoinColumn(name="area_id", referencedColumnName="id")
	// private DbResArea area;

	// @OneToMany
//     @JoinTable(name="res_sku_repo",
//       		joinColumns = { @JoinColumn(name = "sku_id", referencedColumnName = "id") },
//       		inverseJoinColumns = { @JoinColumn(name = "repo_id", referencedColumnName = "id") }
//    )
//    @JoinTable(name="res_sku_repo")
//    @JoinColumn(name = "id",referencedColumnName = "repo_id")
// 	private List<DbResSku> skus;

	// @Column(name = "repo_type", length = 6)
	// private String repoType;

	@Column(name = "area_id")
	private Long areaId;

	@Column(name="repo_code",length = 64)
	private String repoCode;
	
	@Column(name = "repo_name", length = 64)
	private String repoName;

	@Column(name = "repo_addr", length = 512)
	private String repoAddr;

	@Column(name = "repo_type", length = 11)
	private String repoType;

	@Column(name = "is_closed",length = 4)
	private String isClosed;

	@Column(name="closed_day")
	private Long closedDay;

	@Column(name = "parent_repo_id")
	private Long parentRepoId;

	@Column(name = "disable_date")
	private Long disableDate;

	@OneToMany(cascade={CascadeType.ALL},mappedBy = "repo")
	private List<DbResSkuRepo> skuRepoList;

}
