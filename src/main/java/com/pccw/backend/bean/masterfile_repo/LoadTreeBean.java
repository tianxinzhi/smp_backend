package com.pccw.backend.bean.masterfile_repo;

import com.pccw.backend.bean.GeneralBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadTreeBean implements GeneralBean {
    private Long id;
    private Object pid;
    private String name;
    private Object other;
    private Object isClosed;
}
