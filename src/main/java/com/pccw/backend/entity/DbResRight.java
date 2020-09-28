package com.pccw.backend.entity;


import javax.persistence.*;
import javax.persistence.Entity;

import com.pccw.backend.annotation.JsonResultParamMapAnnotation;
import com.pccw.backend.annotation.JsonResultParamMapPro;
import lombok.Data;

/**
 * DbResRight
 */

 @Entity
 @Data
 @Table(name = "res_right")
// @SequenceGenerator(name="id_right",sequenceName = "right_seq",allocationSize = 1)
// @JsonResultParamMapAnnotation(param1 = "id",param2 = "rightPid",param3 = "rightName")
 @JsonResultParamMapPro(fieldMapping = {"id=id","pid=rightPid","name=rightName"})
public class DbResRight extends Base{
    @Id
    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_right")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_right_gen")
    @TableGenerator(
            name = "res_right_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_right_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name="right_pid")
    private Long rightPid;

    @Column(name="right_name")
    private String rightName;

    @Column(name="right_url")
    private String rightUrl;

    /**
     * List 目录
     * Menu 菜单
     * Button 按钮
     */
    @Column(name = "right_type")
    private String rightType;

    /**
     * 权限标识
     * 菜单 ——> 类名
     * 按钮 ——> 方法路由
     */
    @Column(name = "right_identifier")
    private String rightIdentifier;

    /**
     * 显示排序
     */
    @Column(name = "sort_num")
    private Long sortNum;
}
