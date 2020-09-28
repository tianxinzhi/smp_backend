package com.pccw.backend.bean.masterfile_attr;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Attr模块 - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;

    private long[] ids;
}
