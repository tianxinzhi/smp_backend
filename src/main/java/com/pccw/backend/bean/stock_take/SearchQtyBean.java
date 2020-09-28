package com.pccw.backend.bean.stock_take;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-31 15:49
 **/
@Data
public class SearchQtyBean {

    @Valid
    @NotNull
    private Long channelId;

    @Valid
    @NotNull
    private List<Long> skuIds;
}
