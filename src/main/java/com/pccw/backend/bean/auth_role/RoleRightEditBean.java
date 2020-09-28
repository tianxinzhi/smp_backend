package com.pccw.backend.bean.auth_role;


import com.pccw.backend.entity.DbResRoleRight;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SearchCondition
 */

 @Data
@NoArgsConstructor
 @ApiModel(value="Role模块 - EditBean",description="")
public class RoleRightEditBean extends DbResRoleRight {

    private Long roleId;

}