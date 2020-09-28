package com.pccw.backend.ctrl.dataconvertion;

import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.RestTemplateUtils;
import com.pccw.backend.util.Session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

/**
 * @Author: xiaozhi
 * @Date: 2020/1/9 17:26
 * @Desc:Lis存量数据同步
 */
@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("data_convertion")
@Api(value="DataConvertion",tags={"DataConvertion"})
public class LisStockDataCtrl {

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
    Session session;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private BufferedReader br = null;
    private FileReader fr = null;
    private FileWriter fw = null;
    private BufferedWriter bw = null;


    //batchjob ip端口
    @Value("${batchjob.ip}")
    private String host;
    @Value("${dc_path}")
    private String dcPath;
    //文件存放路径
    private final String dataPath = System.getProperty("user.dir")+dcPath;

    @ApiOperation(value="获取lis存量数据",tags={"DataConvertion"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/getStockDataFromLis")
    @Transactional
    public JsonResult getStockDataFromLis() {
        try {
            long begTime = System.currentTimeMillis();
            backData();
            getBacthJobData();
            transferData();
            syncData();
            log.info("------- 插入数据总共用时:"+(System.currentTimeMillis()-begTime)/1000+"秒 -------");
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
        return JsonResult.success(Arrays.asList());
    }

    /**
     * [1]备份hkt数据库
     *
     * item保存res_sku,res_sku_lis
     * catalog保存res_class,res_class_lis
     * element保存res_sku_attr_value,res_sku_attr_value_lis
     */
    private void backData() {
        //getNewOrUpdateJSONData
    }


    /**
     * [2]调用batchjob api 获取存量（存放到data目录下）
     */
    private void getBacthJobData() {
        try {
            //清空文件夹
            if(new File(dataPath).isDirectory()){
                for (File file : new File(dataPath).listFiles()) {
                    file.delete();
                }
            }
            //获取存量api
            String[] apiPaths =  new String[]{"syncElementFileToSmp","syncCategoryFileToSmp","syncItemFileToSmp"};
            for (String apiPath : apiPaths) {
                //请求api
                ResponseEntity<Resource> entity = RestTemplateUtils.post(host+apiPath, Resource.class);
                if(entity.getStatusCode() == HttpStatus.OK){
                    List<String> strings = entity.getHeaders().get("Content-Disposition");
                    String resText = strings.get(0);
                    resText = resText.substring(resText.indexOf("=")+1, resText.length());
                    //System.out.println(substring);
                    if(!new File(dataPath).exists()) new File(dataPath).mkdirs();
                    File lisFile = new File(dataPath+"/"+resText);
                    InputStreamReader isr = new InputStreamReader(entity.getBody().getInputStream());
                    br = new BufferedReader(isr);
                    fw = new FileWriter(lisFile);
                    bw = new BufferedWriter(fw);
                    String str = null;
                    while ((str=(br.readLine()))!=null){
                        bw.write(str);
                        bw.newLine();
                    }
                }
                closeReader();
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

    }


    /**
     *
     * [3]解析txt,获取数据并构造成smp对应逻辑结构map，并将其存入redis备用
     */
    private void transferData() {
        try {
            File f = new File(dataPath);
            if(!f.isDirectory()) return;
            readFileData(f.listFiles());
            geneSkuMapData();
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 读取文件每行内容并放入data Map
     * @throws Exception
     */
    private void geneSkuMapData() {
        //存放attrValue，attr,spec,class,sku的数据结构
        try {
            Map<String,Object> attrValueDataSet =  new HashMap<>();
            Map<String,Map> attrDataMap =  new HashMap<>();
            Map<String,Map<String,Map>> specDataMap =  new HashMap<>();
            Map<String,Object> classDataSet =  new HashMap<>();
            Map<String,Map> skuDataSet =  new HashMap<>();
            Map<String,Map<String,Map>> skuAttrMap = new HashMap<>();//sku_attr_value 的map
            Map<String,Object> attrList = (Map)session.get("attrValue");
            Map<String,Object> classList = (Map)session.get("class");
            Map<String,Object> skuList = (Map)session.get("sku");
            session.delete("sku");
            session.delete("class");
            session.delete("attrValue");

            for (String attrs : attrList.keySet()) {
                if(attrs.contains("INVENTORY_ITEM_ID")) continue;//去标题
                if(attrs.split("\\|").length != 5) log.info("lis_element文件中此行数据格式错误:"+attrs);
                long attr_skuId = Long.parseLong(attrs.split("\\|")[0].trim());
                long attr_classId = Long.parseLong(attrs.split("\\|")[1].trim());
                String attrName = attrs.split("\\|")[2].trim();
                String attrValue = attrs.split("\\|")[3].trim();

                attrValueDataSet.put(attrValue,"");

                //判断是否为同一attr的attrValue
                boolean findAttr = false;
                for (Map.Entry<String, Map> attrEntry : attrDataMap.entrySet()) {
                    //找到attr下新的attrvalue
                    if(attrEntry.getKey().equals(attrName)){
                        findAttr = true;
                        attrEntry.getValue().put(attrValue,"");
                        break;
                    }
                }
                //找到新的attr
                if(findAttr == false){

                    Map vaSet = new HashMap();
                    vaSet.put(attrValue,"");
                    attrDataMap.put(attrName,vaSet);
                }

                boolean findSpec = false;
                for (String specs : classList.keySet()) {
                    if(specs.contains("ITEM_CATALOG_GROUP_ID")) continue;
                    if(specs.split("\\|").length != 5) log.info("lis_category文件中此行数据格式错误:"+specs);
                    String specDesc = specs.split("\\|")[1].trim();

                    if(attr_classId == Long.parseLong(specs.split("\\|")[0].trim())){
                        findSpec = true;
                        boolean findMapSpec = false;
                        //查找spec对应的attr和attrValue
                        for (Map.Entry<String, Map<String, Map>> specMap : specDataMap.entrySet()) {
                            //找到spec的key时
                            if(specMap.getKey().equals(specDesc)){
                                findMapSpec = true;
                                boolean findSpecAttr = false;
                                for (Map.Entry<String, Map> attrMap : specMap.getValue().entrySet()) {
                                    //找到spec下的attr key时
                                    if(attrMap.getKey().equals(attrName)){
                                        findSpecAttr = true;
                                        attrMap.getValue().put(attrValue,"");
                                        break;
                                    }
                                }
                                //未找到，说明是spec下新的attr
                                if(findSpecAttr == false){
                                    Map attrValueSet = new HashMap();
                                    attrValueSet.put(attrValue,"");
                                    specMap.getValue().put(attrName,attrValueSet);
                                }
                                break;
                            }
                        }
                        //未找到，说明是新的spec
                        if(findMapSpec == false){
                            Map<String, Map> attrMap = new HashMap<>();
                            Map vaSet = new HashMap();
                            vaSet.put(attrValue,"");
                            attrMap.put(attrName,vaSet);
                            specDataMap.put(specDesc,attrMap);
                        }
                        classDataSet.put(specDesc,"");
                        boolean findSku = false;
                        for (String skus : skuList.keySet()) {
                            if(skus.contains("ORGANIZATION_ID")) continue;
                            if(skus.split("\\|").length != 7) log.info("lis_item文件中此行数据格式错误:"+skus);
                            String skuName = skus.split("\\|")[1].trim();
                            if(attr_skuId == Long.parseLong(skus.split("\\|")[2].trim())) {
                                Map skuMap = new HashMap();//单个sku的map
                                findSku = true;

                                boolean findSku_Attr = false;
                                for (Map.Entry<String, Map<String,Map>> sku_AttrMap : skuAttrMap.entrySet()) {
                                    //为同一个sku下的attr时
                                    if(sku_AttrMap.getKey().equals(skuName)){

                                        findSku_Attr = true;
                                        boolean findSku_Attr_Value = false;
                                        for (Map.Entry<String, Map> attrMap : sku_AttrMap.getValue().entrySet()) {
                                            //sku已经加入的attr
                                            if(attrMap.getKey().equals(attrName)){
                                                findSku_Attr_Value = true;
                                                attrMap.getValue().put(attrValue,"");
                                                break;
                                            }
                                        }
                                        //sku下新的attr
                                        if(findSku_Attr_Value == false){
                                            Map attrValueSet = new HashMap();
                                            attrValueSet.put(attrValue,"");
                                            sku_AttrMap.getValue().put(attrName,attrValueSet);
                                        }
                                        break;
                                    }
                                }
                                //新的sku ->attr
                                if(findSku_Attr == false){
                                    skuMap = new HashMap();

                                    Map<String, Map> map = new HashMap<>();
                                    Map vaSet = new HashMap();
                                    vaSet.put(attrValue,"");
                                    map.put(attrName,vaSet);
                                    skuAttrMap.put(skuName,map);
                                }
                                //sku
                                skuMap.put("skuName",skuName);
                                skuMap.put("skuDesc",skus.split("\\|")[3].trim());
                                skuMap.put("skuType",specDesc);
                                skuMap.put("skuAttrValue",skuAttrMap.get(skuName));//skuAttrValue
                                skuMap.put("repoId",Long.parseLong(skus.split("\\|")[0].trim()));
                                skuDataSet.put(skuName,skuMap);
                                break;
                            }
                        }

                        if(findSku == false){
                            log.info("------------ The INVENTORY_ITEM_ID " + attr_skuId +" 在lis_item文件中未找到！！！");
                        }
                        break;
                    }

                }
                if(findSpec == false){
                    log.info("------------ The ITEM_CATALOG_GROUP_ID "+attr_classId+" 在lis_catagory文件中未找到！！！");
                }

            }

            //对应关系存入redis
            //        redisTemplate.executePipelined((RedisCallback<Object>) connection  -> {
            //            connection.openPipeline();
            //            session.set("attrValue",attrValueDataSet);
            //            session.set("attr",attrDataMap);
            //            session.set("spec",specDataMap);
            //            session.set("class",classDataSet);
            //            session.set("sku",skuDataSet);
            //            connection.closePipeline();
            //            return null;
            //        });
            session.set("attrValue",attrValueDataSet);
            session.set("attr",attrDataMap);
            session.set("spec",specDataMap);
            session.set("class",classDataSet);
            session.set("sku",skuDataSet);
        } catch(Exception e){
            e.printStackTrace();
            log.info(e.getMessage());
        }
    }
    /**
     * 读取data下面的文件
     * @param files
     * @return
     * @throws Exception
     */
    private void readFileData(File[] files) throws Exception{
        for (File file : files) {
            if ( file.getName().contains("lis_item") ) {
                session.set("sku",readWord(file));
            } else if ( file.getName().contains("lis_category") ) {
                session.set("class",readWord(file));
            } else if ( file.getName().contains("lis_element") ) {
                session.set("attrValue",readWord(file));
            }

        }
    }

    /**
     * 读取单个文件内容
     * @param file
     * @return
     * @throws Exception
     */
    Map readWord(File file) throws Exception{
        fr = new FileReader(file);
        br = new BufferedReader(fr);
        //读取文件每个字段
        String str = null;
        Map<String,Object> objectList= new HashMap();
        while ((str=(br.readLine()))!=null){
            objectList.put(str,"");
        }
        return objectList;
    }


    /**
     * [4]从redis获取存入的结构map，更新数据到表
     */
    void syncData() {
        try {
            Map<String,Object> attrValueData = (Map) session.get("attrValue");
            Map<String, Map> attrData = (Map) session.get("attr");
            Map<String, Map<String, Map>> specData = (Map) session.get("spec");
            Map<String,Object> classData = (Map)session.get("class");
            Map<String,Map> skuData = (Map) session.get("sku");

//            List<DbResAttrValue> attrValueSet = new LinkedList<>();
//            List<DbResAttr> attrSet = new LinkedList<>();
//            List<DbResSpec> specSet = new LinkedList<>();
//            List<DbResClass> classSet = new LinkedList<>();
//            List<DbResType> typeSet = new LinkedList<>();
//            List<DbResSku> skuSet = new LinkedList<>();
//
//            List<DbResSkuLis> skuLisSet = new LinkedList<>();
//            List<DbResClassLis> classLisSet = new LinkedList<>();
            //Set<DbResSkuAttrValueLis> skuAttrValueLisSet = new LinkedHashSet<>();
            long time = System.currentTimeMillis();
            for (String s : attrValueData.keySet()) {
                DbResAttrValue value = new DbResAttrValue();
                value.setAttrValue(s);
                value.setCreateAt(time);
                value.setUpdateAt(time);
                value.setActive("Y");
                //attrValueSet.add(value);
                attrValueRepository.saveAndFlush(value);
            }
            //attrValueRepository.saveAll(attrValueSet);

            //attr
            for (Map.Entry<String, Map> attrName : attrData.entrySet()) {
                DbResAttr value = new DbResAttr();
                value.setAttrName(attrName.getKey());
                value.setAttrDesc(attrName.getKey());
                value.setCreateAt(time);
                value.setUpdateAt(time);
                value.setActive("Y");

                List<DbResAttrAttrValue> attrAttrValueList = new LinkedList<>();
                //中间表
                for (Object o : attrName.getValue().keySet()) {
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
            for (Map.Entry<String, Map<String,Map>> specEntry : specData.entrySet()) {
                DbResSpec value = new DbResSpec();
                value.setSpecName(specEntry.getKey());
                value.setSpecDesc(specEntry.getKey());
                value.setVerId("1.0");
                value.setCreateAt(time);
                value.setUpdateAt(time);
                value.setActive("Y");
                Map<String,Map> attrMap = specEntry.getValue();

                List<DbResSpecAttr> specAttrList = new LinkedList<>();
                for (Map.Entry<String, Map> attrEntry : attrMap.entrySet()) {
                    DbResAttr attr = attrRepository.getDbResAttrsByAttrName(attrEntry.getKey()).get(0);
                    for (Object o : attrEntry.getValue().keySet()) {
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
            for (String classDatum : classData.keySet()) {
                DbResClass value = new DbResClass();
                value.setClassName(classDatum);
                value.setClassDesc(classDatum);
                value.setCreateAt(time);
                value.setUpdateAt(time);
                value.setActive("Y");
                //classSet.add(value);
                //classLis
                DbResClassLis classLis = new DbResClassLis();
                classLis.setClassId(value);
                classLis.setClassDesc(classDatum);
                classLis.setCreateAt(time);
                classLis.setUpdateAt(time);
                classLis.setActive("Y");
                //classLisSet.add(classLis);

                //type
                DbResType type = new DbResType();
                type.setTypeCode(classDatum);
                type.setTypeName(classDatum);
                type.setTypeDesc(classDatum);
                type.setCreateAt(time);
                type.setUpdateAt(time);
                type.setActive("Y");

                List<DbResTypeSkuSpec> typeSkuSpecs = new LinkedList<>();
                DbResTypeSkuSpec value2 = new DbResTypeSkuSpec();
                value2.setType(type);
                value2.setSpecId(specRepository.getDbResSpecsBySpecName(classDatum).get(0).getId());
                value2.setIsType("Y");
                value2.setCreateAt(time);
                value2.setUpdateAt(time);
                value2.setActive("Y");
                typeSkuSpecs.add(value2);

                List<DbResClassType> classTypeList = new LinkedList<>();
                DbResClassType value3 = new DbResClassType();
                value3.setType(type);
                value3.setClasss(value);
                value3.setCreateAt(time);
                value3.setUpdateAt(time);
                value3.setActive("Y");
                classTypeList.add(value3);

                type.setDbResTypeSkuSpecList(typeSkuSpecs);
                type.setRelationOfTypeClass(classTypeList);

                //typeSet.add(type);
                classRepository.saveAndFlush(value);
                classLisRepository.saveAndFlush(classLis);
                typeRepository.saveAndFlush(type);
            }
            //classRepository.saveAll(classSet);
            //classLisRepository.saveAll(classLisSet);
            //typeRepository.saveAll(typeSet);

            //sku,skuLis
            int count = 0;
            for (Map skuDatum : skuData.values()) {
                DbResSku sku = new DbResSku();
                String skuName = skuDatum.get("skuName").toString();
                sku.setSkuName(skuName);
                sku.setSkuCode(skuName);
                sku.setSkuDesc(skuDatum.get("skuDesc").toString());
                sku.setSkuOrigin(StaticVariable.SKU_ORIGIN_FROM_LIS);
                sku.setCreateAt(time);
                sku.setUpdateAt(time);
                sku.setActive("Y");
                //skuLis
                DbResSkuLis skuLis = new DbResSkuLis();
                BeanUtils.copyProperties(sku,skuLis);
                //List<DbResClassLis> skuType1 = classLisRepository.getDbResClassLissByClassDesc(skuDatum.get("skuType").toString());
                //skuLis.setClassLisId(skuType1 == null ? null:skuType1.get(0));
                skuLis.setRepoId(Long.parseLong(skuDatum.get("repoId").toString()));
                //skuType
                //sku新建一个spec
                DbResSpec spec = specRepository.getDbResSpecsBySpecName(skuDatum.get("skuType").toString()).get(0);
                DbResSpec newSpec = new DbResSpec();
                newSpec.setSpecName(spec.getSpecName());
                newSpec.setSpecDesc(spec.getSpecDesc());
                newSpec.setVerId(spec.getVerId());
                newSpec.setCreateAt(time);
                newSpec.setUpdateAt(time);
                newSpec.setActive("Y");


                List<DbResSkuAttrValueLis> skuAttrValueLisList = new LinkedList<>();
                List<DbResSpecAttr> specAttrList = new LinkedList<>();
                Map<String,Map> attrMap =  (Map)skuDatum.get("skuAttrValue");
                for (Map.Entry<String, Map> attrEntry : attrMap.entrySet()) {
                    DbResAttr attr = attrRepository.getDbResAttrsByAttrName(attrEntry.getKey()).get(0);
                    for (Object o : attrEntry.getValue().keySet()) {
                        //newSpec下的attr
                        DbResAttrValue attrValue = attrValueRepository.getDbResAttrValuesByAttrValue(o.toString()).get(0);
                        DbResSpecAttr specAttr = new DbResSpecAttr();
                        specAttr.setAttrId(String.valueOf(attr.getId()));
                        specAttr.setAttrValueId(String.valueOf(attrValue.getId()));
                        specAttr.setActive("Y");
                        specAttr.setCreateAt(time);
                        specAttr.setUpdateAt(time);
                        specAttrList.add(specAttr);
                        //
                        DbResSkuAttrValueLis skuAttrValueLis = new DbResSkuAttrValueLis();
                        //skuAttrValueLis.setSkuLis(skuLis);
                        //skuAttrValueLis.setSkuAttrValueId(skuAttrValue);
                        skuAttrValueLis.setAttrName(attrEntry.getKey());
                        skuAttrValueLis.setAttrValue(o.toString());
                        skuAttrValueLisList.add(skuAttrValueLis);
                    }
                }
                newSpec.setResSpecAttrList(specAttrList);
                specRepository.saveAndFlush(newSpec);

                DbResTypeSkuSpec skuType = new DbResTypeSkuSpec();
                skuType.setSku(sku);
                List<DbResSpec> dbResSpecsBySpecName = specRepository.getDbResSpecsBySpecName(spec.getSpecName());
                DbResSpec maxSpec = dbResSpecsBySpecName.stream().max(Comparator.comparing(DbResSpec::getId)).get();
                skuType.setType(typeRepository.getDbResTypesByTypeCode(skuDatum.get("skuType").toString()).get(0));
                skuType.setSpecId(maxSpec.getId());
                skuType.setIsType("N");
                skuType.setCreateAt(time);
                skuType.setUpdateAt(time);
                skuType.setActive("Y");
//                sku.setDbResTypeSkuSpec(skuType);
                //skuSet.add(sku);
                skuLis.setSkuAttrValueLisList(skuAttrValueLisList);
                //skuLisSet.add(skuLis);
                count++;
                DbResSku sku1 = skuRepository.saveAndFlush(sku);
                skuLis.setSkuId(sku1.getId());
                skuLisRepository.saveAndFlush(skuLis);
            }
            log.info("插入sku数量为："+count);
//            skuRepository.saveAll(skuSet);
            //           skuLisRepository.saveAll(skuLisSet);
            session.delete("attrValue");
            session.delete("attr");
            session.delete("spec");
            session.delete("class");
            session.delete("sku");
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 关闭IO缓冲
     */
    void closeReader() {
        try {
            if(br!=null){
                br.close();
            }
            if(bw!=null){
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }

}
