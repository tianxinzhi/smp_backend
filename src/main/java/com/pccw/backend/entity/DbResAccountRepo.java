package com.pccw.backend.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @description: 人员组织表
 * @author: ChenShuCheng
 * @create: 2020-04-26 18:53
 **/
@Data
@Entity
@Table(name = "res_account_repo")
public class DbResAccountRepo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.TABLE,generator="res_account_repo_gen")
    @TableGenerator(
            name = "res_account_repo_gen",
            table="fendo_generator",
            pkColumnName="seq_name",     //指定主键的名字
            pkColumnValue="res_account_repo_pk",      //指定下次插入主键时使用默认的值
            valueColumnName="seq_id",    //该主键当前所生成的值，它的值将会随着每次创建累加
            //initialValue = 1,            //初始化值
            allocationSize=1             //累加值
    )
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "repo_id")
    private Long repoId;
}
