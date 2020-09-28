package com.pccw.backend.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode implements GeneralBean {
    private Long id;
    private Object pid;
    private String name;
}
