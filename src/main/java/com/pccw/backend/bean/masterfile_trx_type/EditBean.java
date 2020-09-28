package com.pccw.backend.bean.masterfile_trx_type;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(value="Trx_Type模块 - EditBean",description="")
public class EditBean extends CreateBean {

    private Long id;
}
