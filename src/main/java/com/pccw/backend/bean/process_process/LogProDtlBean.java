package com.pccw.backend.bean.process_process;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2019-12-05 19:20
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogProDtlBean {

    private long createAt;

    private long logRepoIn;

    private long logRepoOut;

    private String logTxtBum;

    private String remark;

    List<LogProDtlLineBean> line;
}
