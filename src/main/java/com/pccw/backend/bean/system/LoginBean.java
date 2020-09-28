package com.pccw.backend.bean.system;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "System 模块 - LoginBean", description = "")
public class LoginBean extends BaseBean {

    @ApiModelProperty(value="用户名",name="username",example="")
    private String username;

    @ApiModelProperty(value="密码",name="password",example="")
    private String password;
}
