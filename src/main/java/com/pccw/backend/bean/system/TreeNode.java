package com.pccw.backend.bean.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2019-12-15 10:01
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode {

    private Long id;

    private Long parentId;

    private String name;

    private String parentName;

    private String url;

    private boolean isLeaf;

    private String tpye;

    private String identifier;

    private Long sortNo;

    private List<TreeNode> children;
}
