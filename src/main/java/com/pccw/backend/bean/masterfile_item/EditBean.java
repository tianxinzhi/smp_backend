package com.pccw.backend.bean.masterfile_item;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value = "Item 模块 - EditBean", description = "")
public class EditBean extends CreateBean {
    private Long id;
}
