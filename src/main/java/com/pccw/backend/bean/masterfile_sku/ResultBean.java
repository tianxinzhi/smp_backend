package com.pccw.backend.bean.masterfile_sku;

import com.pccw.backend.bean.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@ApiModel(value="Sku模块 - SkuResultBean",description="sku搜索结果")
public class ResultBean extends BaseBean {

    @ApiModelProperty(value="skuId",name="id",example="")
    private long id;

    @ApiModelProperty(value="repo",name="repo",example="")
    private long stores;

    @ApiModelProperty(value="shopCode",name="shopCode",example="")
    private String storeCodes;

    @ApiModelProperty(value="skuCode",name="sku编码",example="")
    private String skuCode;

    @ApiModelProperty(value="skuDesc",name="sku详情",example="")
    private String skuDesc;

    @ApiModelProperty(value="skuName",name="sku名称",example="")
    private String skuName;

    @ApiModelProperty(value="tangibleItem",name="tangibleItem",example="")
    private String tangibleItem;

    @ApiModelProperty(value="inventoryAssetFlag",name="inventoryAssetFlag",example="")
    private String inventoryAssetFlag;

    @ApiModelProperty(value="returnableFlag",name="returnableFlag",example="")
    private String returnableFlag;

    @ApiModelProperty(value="startDateActive",name="startDateActive",example="")
    private Long startDateActive;

    @ApiModelProperty(value="endDateActive",name="endDateActive",example="")
    private Long endDateActive;

    @ApiModelProperty(value="qty",name="qty",example="")
    private long qty;

    @ApiModelProperty(value="type",name="type",example="1")
    private long type;

    @ApiModelProperty(value="typeName",name="typeName",example="iphone")
    private String typeName;

    @ApiModelProperty(value="spec",name="spec",example="1")
    private long spec;

    @ApiModelProperty(value="specName",name="specName",example="peizhi")
    private String specName;

    @ApiModelProperty(value="attrs",name="attrs",example="[2,3]")
    private long[] attrs;

    @ApiModelProperty(value="attrNames",name="attrNames",example="['color','shape']")
    private String[] attrNames;

    @ApiModelProperty(value="attrValues",name="attrValues",example="[['4','6'],['5','7']]")
    private List<String[]> attrValues;

    @ApiModelProperty(value="attrValueNames",name="attrValueNames",example="[['red','green'],['red','blue']]")
    private List<String[]> attrValueNames;

    @ApiModelProperty(value="skuAttrs",name="skuAttrs",example="[2,3]")
    private long[] skuAttrs;

    @ApiModelProperty(value="skuAttrNames",name="skuAttrNames",example="['color','shape']")
    private String[] skuAttrNames;

    @ApiModelProperty(value="skuAttrValues",name="skuAttrValues",example="[['4','6'],['5','7']]")
    private List<String[]> skuAttrValues;

    @ApiModelProperty(value="skuAttrValueNames",name="skuAttrValueNames",example="[['red','green'],['red','blue']]")
    private List<String[]> skuAttrValueNames;

    private List<Map> attrData;

    //前端展示
    private Map tableMutiData;
}
