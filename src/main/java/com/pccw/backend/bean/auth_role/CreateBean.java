package com.pccw.backend.bean.auth_role;


import com.pccw.backend.bean.BaseBean;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

@Data
@NoArgsConstructor
public class CreateBean extends BaseBean{

    private String roleName;

    private String roleDesc;

    private String[] rightId;

}