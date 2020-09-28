package com.pccw.backend.bean.stock_threshold;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * SearchCondition
 */

@Data
@NoArgsConstructor
public class SearchBean{
    private List<String> skuId;
    private String repoId;

}