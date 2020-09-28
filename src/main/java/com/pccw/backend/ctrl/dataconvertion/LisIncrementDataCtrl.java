package com.pccw.backend.ctrl.dataconvertion;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: xiaozhi
 * @Date: 2020/02/19 10:26
 * @Desc: Lis增量数据同步
 */
@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("data_convertion")
@Api(value="DataConvertion",tags={"DataConvertion"})
public class LisIncrementDataCtrl {

    @Autowired
    private ResSkuRepository skuRepository;
    @Autowired
    private ResSkuLisRepository skuLisRepository;
    @Autowired
    private ResClassLisRepository classLisRepository;
    @Autowired
    private ResClassRepository classRepository;
    @Autowired
    private ResTypeRepository typeRepository;
    @Autowired
    private ResSpecRepository specRepository;
    @Autowired
    private ResAttrRepository attrRepository;
    @Autowired
    private ResAttrValueRepository attrValueRepository;
    @Autowired
    ResTypeSkuSpecRepository typeSkuSpecRepository;


    private Set<String> attrValueDataSet =  new HashSet<>();
    private Map<String,Set> attrDataMap =  new HashMap<>();
    private Map<String,Map<String,Set>> specDataMap =  new HashMap<>();
    private Set<String> classDataSet =  new HashSet<>();
    private Set<Map> skuDataSet =  new HashSet<>();
    private Map<String,Map<String,Set>> skuAttrMap = new HashMap<>();//sku_attr_value 的map

    @ApiOperation(value="获取lis增量数据",tags={"DataConvertion"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/getIncrementDataFromLis")
    public JsonResult getIncrementDataFromLis(@RequestBody JSONObject addObject) {
        try {
            JSONArray elementData = addObject.getJSONArray("ElementData");
            JSONArray catalogData = addObject.getJSONArray("CatalogData");
            JSONArray itemData = addObject.getJSONArray("ItemData");

            //数据关系解析及对应
            for(int i=0;i<elementData.size();i++){
                JSONObject attr = elementData.getJSONObject(i);
                long skuId = Long.parseLong(attr.get("inventoryItemId").toString());
                long classId = Long.parseLong(attr.get("itemCatalogGroupId").toString());
                String attrName = (String)attr.get("elementName");
                String attrValue = (String)attr.get("elementValue");

                attrValueDataSet.add(attrValue);

                boolean findAttr = false;
                for (Map.Entry<String, Set> attrEntry : attrDataMap.entrySet()) {
                    //找到attr下新的attrvalue
                    if(attrEntry.getKey().equals(attrName)){
                        findAttr = true;
                        attrEntry.getValue().add(attrValue);
                        break;
                    }
                }
                //找到新的attr
                if(findAttr == false){

                    Set vaSet = new HashSet();
                    vaSet.add(attrValue);
                    attrDataMap.put(attrName,vaSet);
                }

                // boolean findSpec = false;
                for (int j=0;j<catalogData.size();j++) {
                    JSONObject classObj = catalogData.getJSONObject(j);
                    String classDesc = (String)classObj.get("description");
                    long classId2 = Long.parseLong(classObj.get("itemCatalogGroupId").toString());


                    if(classId == classId2){
                        //findSpec = true;
                        boolean findMapSpec = false;
                        //查找spec对应的attr和attrValue
                        for (Map.Entry<String, Map<String, Set>> specMap : specDataMap.entrySet()) {
                            //找到spec的key时
                            if(specMap.getKey().equals(classDesc)){
                                findMapSpec = true;
                                boolean findSpecAttr = false;
                                for (Map.Entry<String, Set> attrMap : specMap.getValue().entrySet()) {
                                    //找到spec下的attr key时
                                    if(attrMap.getKey().equals(attrName)){
                                        findSpecAttr = true;
                                        attrMap.getValue().add(attrValue);
                                        break;
                                    }
                                }
                                //未找到，说明是spec下新的attr
                                if(findSpecAttr == false){
                                    Set attrValueSet = new HashSet();
                                    attrValueSet.add(attrValue);
                                    specMap.getValue().put(attrName,attrValueSet);
                                }
                                break;
                            }
                        }
                        //未找到，说明是新的spec
                        if(findMapSpec == false){
                            Map<String, Set> attrMap = new HashMap<>();
                            Set vaSet = new HashSet();
                            vaSet.add(attrValue);
                            attrMap.put(attrName,vaSet);
                            specDataMap.put(classDesc,attrMap);
                        }
                        classDataSet.add(classDesc);
                        //boolean findSku = false;
                        for (int k=0;k<itemData.size();k++) {
                            JSONObject skuObj = itemData.getJSONObject(k);
                            long skuId2 = Long.parseLong(skuObj.get("inventoryItemId").toString());
                            long classId3 = Long.parseLong(skuObj.get("itemCatalogGroupId").toString());
                            long repoId = Long.parseLong(skuObj.get("organizationId").toString());
                            String skuName = (String)skuObj.get("item");
                            String skuDesc = (String)skuObj.get("description");

                            if(skuId == skuId2) {
                                Map skuMap = new HashMap();//单个sku的map
                                //findSku = true;

                                boolean findSku_Attr = false;
                                for (Map.Entry<String, Map<String,Set>> sku_AttrMap : skuAttrMap.entrySet()) {
                                    //为同一个sku下的attr时
                                    if(sku_AttrMap.getKey().equals(skuName)){

                                        findSku_Attr = true;
                                        boolean findSku_Attr_Value = false;
                                        for (Map.Entry<String, Set> attrMap : sku_AttrMap.getValue().entrySet()) {
                                            //sku已经加入的attr
                                            if(attrMap.getKey().equals(attrName)){
                                                findSku_Attr_Value = true;
                                                attrMap.getValue().add(attrValue);
                                                break;
                                            }
                                        }
                                        //sku下新的attr
                                        if(findSku_Attr_Value == false){
                                            Set attrValueSet = new HashSet();
                                            attrValueSet.add(attrValue);
                                            sku_AttrMap.getValue().put(attrName,attrValueSet);
                                        }
                                        break;
                                    }
                                }
                                //新的sku ->attr
                                if(findSku_Attr == false){
                                    skuMap = new HashMap();

                                    Map<String, Set> map = new HashMap<>();
                                    Set vaSet = new HashSet();
                                    vaSet.add(attrValue);
                                    map.put(attrName,vaSet);
                                    skuAttrMap.put(skuName,map);
                                }
                                //sku
                                skuMap.put("skuName",skuName);
                                skuMap.put("skuDesc",skuDesc);
                                skuMap.put("skuType",classDesc);
                                skuMap.put("skuAttrValue",skuAttrMap.get(skuName));//skuAttrValue
                                skuMap.put("repoId",repoId);
                                skuDataSet.add(skuMap);
                                //break;
                            }
                        }
                        //break;
                    }

                }

            }
            syncData();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
        return JsonResult.success(Arrays.asList());
    }

    /**
     * 数据更新或插入
     */
    void syncData() {
        try {
            //插入数据库
            long time = System.currentTimeMillis();
            for (String attrValue : attrValueDataSet) {
                List<DbResAttrValue> dbResAttrValues = attrValueRepository.getDbResAttrValuesByAttrValue(attrValue);
                if(dbResAttrValues==null||dbResAttrValues.size()<=0){
                    DbResAttrValue value = new DbResAttrValue();
                    value.setAttrValue(attrValue);
                    value.setCreateAt(time);
                    value.setUpdateAt(time);
                    value.setActive("Y");
                    attrValueRepository.saveAndFlush(value);
                }
            }
            for (Map.Entry<String, Set> attrName : attrDataMap.entrySet()) {
                List<DbResAttr> dbResAttrs = attrRepository.getDbResAttrsByAttrName(attrName.getKey());
                DbResAttr value = null;
                List<DbResAttrAttrValue> attrAttrValueList = null;
                //新增或者修改
                if(dbResAttrs==null||dbResAttrs.size()<=0){
                    value = new DbResAttr();
                    attrAttrValueList = new LinkedList<>();
                    value.setCreateAt(time);
                } else {
                    value = dbResAttrs.get(0);
                    attrAttrValueList = value.getAttrAttrValueList();
                    attrAttrValueList.clear();
                }
                value.setAttrName(attrName.getKey());
                value.setAttrDesc(attrName.getKey());
                value.setUpdateAt(time);
                value.setActive("Y");

                //中间表
                for (Object o : attrName.getValue()) {
                    DbResAttrValue attrValue = attrValueRepository.getDbResAttrValuesByAttrValue(o.toString()).get(0);

                    DbResAttrAttrValue attrAttrValue = new DbResAttrAttrValue();
                    attrAttrValue.setAttr(value);
                    attrAttrValue.setAttrValue(attrValue);
                    attrAttrValue.setCreateAt(time);
                    attrAttrValue.setUpdateAt(time);
                    attrAttrValue.setActive("Y");
                    attrAttrValueList.add(attrAttrValue);
                }
                value.setAttrAttrValueList(attrAttrValueList);
                //attrSet.add(value);
                attrRepository.saveAndFlush(value);
            }
            //attrRepository.saveAll(attrSet);
            //spec
            for (Map.Entry<String, Map<String,Set>> specEntry : specDataMap.entrySet()) {
                List<DbResSpec> dbResSpecs = specRepository.getDbResSpecsBySpecName(specEntry.getKey());
                DbResSpec value = null;
                List<DbResSpecAttr> specAttrList = null;
                //新增或者修改
                if(dbResSpecs==null||dbResSpecs.size()<=0){
                    value = new DbResSpec();
                    specAttrList = new LinkedList<>();
                    value.setCreateAt(time);
                } else {
                    value = dbResSpecs.get(0);
                    specAttrList = value.getResSpecAttrList();
                    specAttrList.clear();
                }
                value.setSpecName(specEntry.getKey());
                value.setSpecDesc(specEntry.getKey());
                value.setUpdateAt(time);
                value.setActive("Y");
                Map<String,Set> attrMap = specEntry.getValue();

                for (Map.Entry<String, Set> attrEntry : attrMap.entrySet()) {
                    DbResAttr attr = attrRepository.getDbResAttrsByAttrName(attrEntry.getKey()).get(0);
                    for (Object o : attrEntry.getValue()) {
                        DbResAttrValue value1 = attrValueRepository.getDbResAttrValuesByAttrValue(o.toString()).get(0);
                        DbResSpecAttr specAttr = new DbResSpecAttr();
                        specAttr.setAttrId(attr.getId()+"");
                        specAttr.setAttrValueId(value1.getId()+"");
                        specAttr.setCreateAt(time);
                        specAttr.setUpdateAt(time);
                        specAttr.setActive("Y");
                        specAttrList.add(specAttr);
                    }
                }
                value.setResSpecAttrList(specAttrList);
                //specSet.add(value);
                specRepository.saveAndFlush(value);
            }
            //specRepository.saveAll(specSet);
            //class,classLis,type
            for (String classDatum : classDataSet) {
                List<DbResClass> classList = classRepository.getDbResClasssByClassName(classDatum);
                if(classList==null || classList.size()<=0){
                    DbResClass value = new DbResClass();
                    value.setClassName(classDatum);
                    value.setClassDesc(classDatum);
                    value.setCreateAt(time);
                    value.setUpdateAt(time);
                    value.setActive("Y");

                    classRepository.saveAndFlush(value);

                    DbResClassLis classLis = new DbResClassLis();
                    classLis.setClassId(value);
                    classLis.setClassDesc(classDatum);
                    classLis.setCreateAt(time);
                    classLis.setUpdateAt(time);
                    classLis.setActive("Y");

                    classLisRepository.saveAndFlush(classLis);


                }

                List<DbResType> dbResTypes = typeRepository.getDbResTypesByTypeCode(classDatum);
                DbResType type = null;
                List<DbResClassType> classTypeList = null;
                List<DbResTypeSkuSpec> typeSkuSpecs = null;
                //新增或者修改
                if(dbResTypes==null || dbResTypes.size()<=0){
                    type = new DbResType();
                    classTypeList = new LinkedList<>();
                    typeSkuSpecs = new LinkedList<>();
                    type.setCreateAt(time);
                } else {
                    type = dbResTypes.get(0);
                    classTypeList = type.getRelationOfTypeClass();
                    typeSkuSpecs = type.getDbResTypeSkuSpecList();
                    classTypeList.clear();
                    typeSkuSpecs.clear();
                }
                type.setTypeCode(classDatum);
                type.setTypeName(classDatum);
                type.setTypeDesc(classDatum);
                type.setUpdateAt(time);
                type.setActive("Y");

                DbResTypeSkuSpec typeSkuSpec = new DbResTypeSkuSpec();
                typeSkuSpec.setIsType("Y");
                typeSkuSpec.setType(type);
                typeSkuSpec.setSpecId(specRepository.getDbResSpecsBySpecName(classDatum).get(0).getId());
                typeSkuSpec.setCreateAt(time);
                typeSkuSpec.setUpdateAt(time);
                typeSkuSpec.setActive("Y");
                typeSkuSpecs.add(typeSkuSpec);

                DbResClassType value3 = new DbResClassType();
                value3.setType(type);
                value3.setClasss(classRepository.getDbResClasssByClassName(classDatum).get(0));
                value3.setCreateAt(time);
                value3.setUpdateAt(time);
                value3.setActive("Y");
                classTypeList.add(value3);

                type.setDbResTypeSkuSpecList(typeSkuSpecs);
                type.setRelationOfTypeClass(classTypeList);

                typeRepository.saveAndFlush(type);

            }
            //classRepository.saveAll(classSet);
            //classLisRepository.saveAll(classLisSet);
            //typeRepository.saveAll(typeSet);

            //sku,skuLis
            int count = 0;
            for (Map skuDatum : skuDataSet) {
                String skuName = skuDatum.get("skuName").toString();
                List<DbResSku> dbResSkus = skuRepository.getDbResSkusBySkuCode(skuName);
                DbResSku sku = null;
                DbResSkuLis skuLis = null;
                List<DbResSkuAttrValueLis> skuAttrValueLisList = null;
                DbResTypeSkuSpec typeSkuSpec = null;
                DbResType type = null;
                //新增或者修改
                if(dbResSkus == null || dbResSkus.size()<=0){
                    sku = new DbResSku();
                    skuLis = new DbResSkuLis();
                    skuAttrValueLisList = new LinkedList<>();
                    sku.setCreateAt(time);
                } else {
                    sku = dbResSkus.get(0);
                    type = typeRepository.getDbResTypesByTypeCode(skuDatum.get("skuType").toString()).get(0);
                    typeSkuSpec = typeSkuSpecRepository.getDbResTypeSkuSpecsBySkuAndType(sku,type);
                    skuLis = skuLisRepository.getDbResSkuLissBySkuCode(skuName).get(0);
                    skuAttrValueLisList = skuLis.getSkuAttrValueLisList();
                    skuAttrValueLisList.clear();
                }
                sku.setSkuName(skuName);
                sku.setSkuCode(skuName);
                sku.setSkuDesc(skuDatum.get("skuDesc").toString());
                sku.setSkuOrigin(StaticVariable.SKU_ORIGIN_FROM_LIS);
                sku.setUpdateAt(time);
                sku.setActive("Y");

                Map<String,Set> attrMap =  (Map)skuDatum.get("skuAttrValue");
                if(typeSkuSpec!=null){
                    //未修改type，只是修改了spec的attr
                    DbResSpec spec = specRepository.findById(typeSkuSpec.getSpecId()).get(0);
                    List<DbResSpecAttr> resSpecAttrList = spec.getResSpecAttrList();
                    resSpecAttrList.clear();
                    for (Map.Entry<String, Set> attrEntry : attrMap.entrySet()) {
                        DbResAttr attr = attrRepository.getDbResAttrsByAttrName(attrEntry.getKey()).get(0);
                        for (Object o : attrEntry.getValue()) {
                            DbResAttrValue attrValue = attrValueRepository.getDbResAttrValuesByAttrValue(o.toString()).get(0);
                            DbResSpecAttr specAttr = new DbResSpecAttr();
                            specAttr.setAttrId(String.valueOf(attr.getId()));
                            specAttr.setAttrValueId(String.valueOf(attrValue.getId()));
                            specAttr.setActive("Y");
                            specAttr.setCreateAt(time);
                            specAttr.setUpdateAt(time);
                            resSpecAttrList.add(specAttr);

                            DbResSkuAttrValueLis skuAttrValueLis = new DbResSkuAttrValueLis();
                            //skuAttrValueLis.setSkuLis(skuLis);
                            skuAttrValueLis.setAttrName(attrEntry.getKey());
                            skuAttrValueLis.setAttrValue(o.toString());
                            skuAttrValueLisList.add(skuAttrValueLis);
                        }
                    }
                    specRepository.saveAndFlush(spec);
                } else {
                    //选了新的type，创建新的spec,删除之前的spec
                    List<DbResTypeSkuSpec> typeSkuSpecs = typeSkuSpecRepository.getDbResTypeSkuSpecsByType(type);
                    if(typeSkuSpecs!=null && typeSkuSpecs.size()>0){
                        DbResSpec oldSpec = specRepository.getOne(typeSkuSpecs.get(0).getSpecId());
                        DbResSpec newSpec = new DbResSpec();
                        newSpec.setSpecName(oldSpec.getSpecName());
                        newSpec.setSpecDesc(oldSpec.getSpecDesc());
                        newSpec.setVerId(oldSpec.getVerId());
                        newSpec.setCreateAt(time);
                        newSpec.setUpdateAt(time);
                        newSpec.setActive("Y");

                        List<DbResSpecAttr> specAttrList = new LinkedList<>();
                        for (Map.Entry<String, Set> attrEntry : attrMap.entrySet()) {
                            DbResAttr attr = attrRepository.getDbResAttrsByAttrName(attrEntry.getKey()).get(0);
                            for (Object o : attrEntry.getValue()) {
                                DbResAttrValue attrValue = attrValueRepository.getDbResAttrValuesByAttrValue(o.toString()).get(0);

                                DbResSpecAttr specAttr = new DbResSpecAttr();
                                specAttr.setAttrId(String.valueOf(attr.getId()));
                                specAttr.setAttrValueId(String.valueOf(attrValue.getId()));
                                specAttr.setActive("Y");
                                specAttr.setCreateAt(time);
                                specAttr.setUpdateAt(time);
                                specAttrList.add(specAttr);

                                DbResSkuAttrValueLis skuAttrValueLis = new DbResSkuAttrValueLis();
                                //skuAttrValueLis.setSkuLis(skuLis);
                                skuAttrValueLis.setAttrName(attrEntry.getKey());
                                skuAttrValueLis.setAttrValue(o.toString());
                                skuAttrValueLisList.add(skuAttrValueLis);
                            }
                        }
                        newSpec.setResSpecAttrList(specAttrList);

                        DbResTypeSkuSpec dbResTypeSkuSpecBySku = typeSkuSpecRepository.getDbResTypeSkuSpecBySku(sku);
                        if(dbResTypeSkuSpecBySku!=null) typeSkuSpecRepository.delete(dbResTypeSkuSpecBySku);
                        specRepository.delete(oldSpec);
                        specRepository.saveAndFlush(newSpec);

                        List<DbResSpec> dbResSpecsBySpecName = specRepository.getDbResSpecsBySpecName(oldSpec.getSpecName());
                        DbResSpec maxSpec = dbResSpecsBySpecName.stream().max(Comparator.comparing(DbResSpec::getId)).get();
                        DbResTypeSkuSpec newTypeSpec = new DbResTypeSkuSpec();
                        //BeanUtils.copyProperties(typeSkuSpec,newTypeSpec);
                        newTypeSpec.setSpecId(maxSpec.getId());
                        newTypeSpec.setType(typeSkuSpecs.get(0).getType());
                        newTypeSpec.setSku(sku);
                        newTypeSpec.setIsType("N");
                        newTypeSpec.setCreateAt(time);
                        newTypeSpec.setUpdateAt(time);
                        newTypeSpec.setActive("Y");
//                        sku.setDbResTypeSkuSpec(newTypeSpec);
                    }
                }
                BeanUtils.copyProperties(sku,skuLis);
                skuLis.setRepoId(Long.parseLong(skuDatum.get("repoId").toString()));
                skuLis.setSkuAttrValueLisList(skuAttrValueLisList);
                //skuLisSet.add(skuLis);
                count++;
                DbResSku sku1 = skuRepository.saveAndFlush(sku);
                skuLis.setSkuId(sku1.getId());
                skuLisRepository.saveAndFlush(skuLis);
            }
            log.info("插入sku数量为："+count);
            log.info("------- 插入数据总共用时:"+(System.currentTimeMillis()-time)/1000+"秒 -------");
        } catch (BeansException | NumberFormatException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }
}
