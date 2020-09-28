package com.pccw.backend.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * SearchCondition
 */
@Data
public class BaseBean implements Serializable {
    private long createAt;
    private long createBy;
    private long updateAt;
    private long updateBy;
    private String active;
    private String createAccountName;
    private String updateAccountName;
}
