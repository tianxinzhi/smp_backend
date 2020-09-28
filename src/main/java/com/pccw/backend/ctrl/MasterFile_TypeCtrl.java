package com.pccw.backend.ctrl;


import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_type.CreateBean;
import com.pccw.backend.bean.masterfile_type.EditBean;
import com.pccw.backend.bean.masterfile_type.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AuthRightCtrl
 */

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST, origins = "*", allowCredentials = "false")
@RequestMapping("/masterfile_type")
@Api(value="MasterFile_TypeCtrl",tags={"masterfile_type"})
public class MasterFile_TypeCtrl extends BaseCtrl<DbResType> implements ICheck {


    @Autowired
    ResTypeRepository repo;
    @Autowired
    ResTypeSkuSpecRepository resTypeSkuSpecRepository;
    @Autowired
    ResClassRepository resClassRepository;
    @Autowired
    ResClassTypeRepository resClassTypeRepository;
    @Autowired
    ResSpecRepository resSpecRepository;
    @Autowired
    ResSkuTypeRepository skuTypeRepository;

    @ApiOperation(value="搜索type",tags={"masterfile_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    public JsonResult search(@RequestBody SearchBean b) {
        try {
            Specification spec = Convertor.convertSpecification(b);
            Sort sort = new Sort(Sort.Direction.DESC,"id");
            List<DbResType> res =repo.findAll(spec,PageRequest.of(b.getPageIndex(),b.getPageSize(),sort)).getContent();
            ArrayList<SearchBean> dbResTypes = new ArrayList<>();
            if(!Objects.isNull(res) && res.size() > 0){
                for (DbResType type:res){
                    SearchBean searchBean = new SearchBean();
//                    List typeSkuList = repo.searchTypeInSku(type.getId());
                    BeanUtils.copyProperties(type, searchBean);
//                    searchBean.setTypeSkuList(typeSkuList);
                    List<DbResTypeSkuSpec> dbResTypeSkuSpecList = type.getDbResTypeSkuSpecList();
                    if(Objects.nonNull(dbResTypeSkuSpecList) && dbResTypeSkuSpecList.size()>0){
                        for (DbResTypeSkuSpec tss:dbResTypeSkuSpecList){
                            if("Y".equals(tss.getIsType())){
                                searchBean.setSpecId(tss.getSpecId());
                                DbResSpec sp = repo.findBySpecId(tss.getSpecId());
                                searchBean.setSpecName(sp.getSpecName());
                                searchBean.setVerId(sp.getVerId());
                                searchBean.setAttrData(specSearch(searchBean.getSpecId()).getData());
                            }
                        }
                    }
                    if(type.getRelationOfTypeClass() != null && type.getRelationOfTypeClass().size() > 0){
                        List<DbResClassType> relationOfTypeClass = type.getRelationOfTypeClass();
                        String classNames = "";
                        List classIds = new ArrayList<>();
                        for(DbResClassType ct:relationOfTypeClass){
                            classNames += ct.getClasss().getClassName()+",";
                            classIds.add(ct.getClasss().getId());
                        }
                        if(!StringUtils.isEmpty(classNames)){
                            classNames = classNames.substring(0,classNames.length()-1);
                        }
                        searchBean.setCreateAccountName(getAccountName(type.getCreateBy()));
                        searchBean.setUpdateAccountName(getAccountName(type.getUpdateBy()));
                        searchBean.setClassName(classNames);
                        searchBean.setClassId(classIds);
                    }
                    dbResTypes.add(searchBean);
                }
            }
            return JsonResult.success(dbResTypes,repo.count(spec));
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @ApiOperation(value="删除type",tags={"masterfile_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids) {
        return this.delete(repo, ids);
    }

    @Transactional(rollbackOn = Exception.class)
    @ApiOperation(value="创建type",tags={"masterfile_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/create")
    public JsonResult create(@RequestBody CreateBean b) {
        try {
            long t = new Date().getTime();
            b.setCreateAt(t);
            b.setCreateBy(getAccount());
            b.setUpdateAt(t);
            b.setUpdateBy(getAccount());
            b.setActive("Y");
            DbResType dbResType = new DbResType();
            BeanUtils.copyProperties(b, dbResType);
            //保存数据到res_type_class表
            List<Long> classIds = b.getClassId();
            List<DbResClassType> classTypeList = new ArrayList<DbResClassType>();
            if(!Objects.isNull(classIds) && classIds.size() > 0){
                for(Long cid:classIds){
                    Optional<DbResClass> optional = resClassRepository.findById(cid);
                    DbResClass dbResClass = optional.get();
                    DbResClassType dbResClassType = new DbResClassType();
                    dbResClassType.setClasss(dbResClass);
                    dbResClassType.setType(dbResType);
                    dbResClassType.setCreateAt(t);
                    dbResClassType.setCreateBy(getAccount());
                    dbResClassType.setUpdateAt(t);
                    dbResClassType.setUpdateBy(getAccount());
                    dbResClassType.setActive("Y");
                    classTypeList.add(dbResClassType);
                }
            }
            dbResType.setRelationOfTypeClass(classTypeList);
            //保存数据到res_type_sku_spec表
            DbResTypeSkuSpec dbResTypeSkuSpec = new DbResTypeSkuSpec();
            dbResTypeSkuSpec.setCreateAt(t);
            dbResTypeSkuSpec.setCreateBy(getAccount());
            dbResTypeSkuSpec.setUpdateAt(t);
            dbResTypeSkuSpec.setUpdateBy(getAccount());
            dbResTypeSkuSpec.setActive("Y");
            dbResTypeSkuSpec.setIsType("Y");
            dbResTypeSkuSpec.setType(dbResType);
            DbResSpec dbResSpec = repo.findByVerAndSpecName(b.getVerId(),b.getSpecName());
            dbResTypeSkuSpec.setSpecId(dbResSpec.getId());
            ArrayList<DbResTypeSkuSpec> typeSkuSpecList = new ArrayList<>();
            typeSkuSpecList.add(dbResTypeSkuSpec);
            dbResType.setDbResTypeSkuSpecList(typeSkuSpecList);
            repo.saveAndFlush(dbResType);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @ApiOperation(value="编辑type",tags={"masterfile_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/edit")
    public JsonResult edit(@RequestBody EditBean b) {
        try {
            //级联更新
            long t = new Date().getTime();
            Optional<DbResType> optional = repo.findById(b.getId());
            DbResType dbResType = optional.get();
            List<DbResClassType> relationOfTypeClass = dbResType.getRelationOfTypeClass();
            relationOfTypeClass.clear();
            dbResType.setUpdateAt(t);
            dbResType.setUpdateBy(getAccount());
            dbResType.setTypeCode(b.getTypeCode());
            dbResType.setTypeDesc(b.getTypeDesc());
            dbResType.setTypeName(b.getTypeName());
            //更新数据到res_type_class表
            List<Long> classIds = b.getClassId();
            if(!Objects.isNull(classIds) && classIds.size() > 0){
                for(Long id:classIds){
                    Optional<DbResClass> optiona2 = resClassRepository.findById(id);
                    DbResClass dbResClass = optiona2.get();
                    DbResClassType classType = new DbResClassType();
                    classType.setClasss(dbResClass);
                    classType.setType(dbResType);
                    classType.setCreateAt(t);
                    classType.setCreateBy(getAccount());
                    classType.setUpdateAt(t);
                    classType.setUpdateBy(getAccount());
                    classType.setActive("Y");
                    relationOfTypeClass.add(classType);
                }
            }
            //更新数据到res_type_sku_spec表
            List<DbResTypeSkuSpec> dbResTypeSkuSpecList = dbResType.getDbResTypeSkuSpecList();
            if(Objects.nonNull(dbResTypeSkuSpecList) && dbResTypeSkuSpecList.size()>0){
                for (DbResTypeSkuSpec tss:dbResTypeSkuSpecList){
                    if("Y".equals(tss.getIsType())){
                        tss.setUpdateAt(t);
                        DbResSpec dbResSpec = repo.findByVerAndSpecName(b.getVerId(),b.getSpecName());
                        tss.setSpecId(dbResSpec.getId());
                    }
                }
            }else{
                DbResTypeSkuSpec dts = new DbResTypeSkuSpec();
                dts.setCreateAt(t);
                dts.setCreateBy(getAccount());
                dts.setUpdateAt(t);
                dts.setUpdateBy(getAccount());
                dts.setActive("Y");
                dts.setIsType("Y");
                DbResSpec dbResSpec = repo.findByVerAndSpecName(b.getVerId(),b.getSpecName());
                dts.setSpecId(dbResSpec.getId());
                dts.setType(dbResType);
                dbResType.getDbResTypeSkuSpecList().add(dts);
            }
            repo.saveAndFlush(dbResType);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    /**
     * 根据specId查询attr&attrValue
     * 返回指定格式给前端详情展示
     * @param id
     * @return
     */
    @ApiOperation(value="搜索spec_attr&attrValue",tags={"masterfile_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/specSearch")
    public JsonResult specSearch(@RequestBody Long id) {
        try {
            List<Map> list = new ArrayList<>();
            List<Map> attrList= repo.specSearch(id);
            attrList.stream()
                    .collect(Collectors.groupingBy(s -> s.get("attrName")))
                    .forEach((k,v)->{
                          HashMap<Object, Object> hm = new HashMap<>();
                          List<String> attrValueList = new ArrayList<>();
                          v.forEach((a)->{ attrValueList.add(a.get("attrValue").toString());});
                          hm.put("attrName",k);
                          hm.put("attrValue",attrValueList);
                          list.add(hm);
                            }
                    );
            return JsonResult.success(list);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="禁用type",tags={"masterfile_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, MasterFile_TypeCtrl.class,resTypeSkuSpecRepository);
    }

    @ApiOperation(value="启用type",tags={"masterfile_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj, BaseRepository... check) {
        ResTypeSkuSpecRepository resscr = (ResTypeSkuSpecRepository)check[0];
        BaseDeleteBean bean = (BaseDeleteBean)obj;
        for(Long id:bean.getIds()) {
            List<DbResTypeSkuSpec> list = resscr.getDbResTypeSkuSpecsByTypeId(id);
            if (list != null && list.size() > 0) {
                return id;
            }
        }
        return 0;
    }
}
