package com.pccw.backend.bean.process_process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2019-12-05 18:31
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Step {
    private String title;
    private String desc;
    private String active;
    private String stepNum;
    private Long processDtlsId;
    private String remark;
    private String status;
    private Long approvedDate;
}
