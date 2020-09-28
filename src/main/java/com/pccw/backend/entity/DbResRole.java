package com.pccw.backend.entity;

import java.util.List;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import lombok.Data;

/**
 * DbResRole
 */

 @Entity
 @Data
 @Table(name="res_role")
// @SequenceGenerator(name="id_role",sequenceName = "role_seq",allocationSize = 1)
 @JsonResultParamMapAnnotation(param1 = "id",param2 = "roleName")
public class DbResRole extends Base {
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_role")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_role_gen")
    @TableGenerator(
            name = "res_role_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_role_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
            )
    private Long id;

    @Column(name="role_name")
    private String roleName;

    @Column(name="role_pid")
    private long rolePid;

    @Column(name="role_desc")
    private String roleDesc;

    @Column(name="role_funtiongroup")
    private String roleFunctionGroup;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "role_id")
    private List<DbResRoleRight> resRoleRightList;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "role_id")
    private List<DbResAccountRole> resAccountRoleList;
    
}
