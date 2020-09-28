package com.pccw.backend.bean;

/**
 * DeleteBean
 */

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;

@Data
@ApiModel(value="公共 - BaseDeleteBean",description="")
public class BaseDeleteBean extends BaseBean {

    private ArrayList<Long> ids;
}