package com.pccw.backend.bean.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-03-09 16:03
 **/
@Data
@NoArgsConstructor
@ApiModel(value = "System 模块 - UrlBean", description = "")
public class UrlBean {
    @ApiModelProperty(value="模块名",name="menu",example="")
    private String menu;

    @ApiModelProperty(value="按钮名",name="button",example="")
    private String button;
}
