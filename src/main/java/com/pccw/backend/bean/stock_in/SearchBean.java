package com.pccw.backend.bean.stock_in;

import com.pccw.backend.bean.BaseSearchBean;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchBean extends BaseSearchBean {

    private String deliveryNumber;
    //    private String logTxtNum;

//    private long logRepoOut;

//    private List<DbResLogMgtDtl> line;
}
