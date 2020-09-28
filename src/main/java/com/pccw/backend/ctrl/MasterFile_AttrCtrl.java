package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_attr.CreateBean;
import com.pccw.backend.bean.masterfile_attr.EditBean;
import com.pccw.backend.bean.masterfile_attr.ResultBean;
import com.pccw.backend.bean.masterfile_attr.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResAttr;
import com.pccw.backend.entity.DbResAttrAttrValue;
import com.pccw.backend.entity.DbResAttrValue;
import com.pccw.backend.entity.DbResSpecAttr;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResAttrRepository;
import com.pccw.backend.repository.ResAttrValueRepository;
import com.pccw.backend.repository.ResSpecAttrRepository;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("masterfile_attr")
@Api(value="MasterFile_AttrCtrl",tags={"masterfile_attr"})
public class MasterFile_AttrCtrl extends BaseCtrl<DbResAttr> implements ICheck {

    @Autowired
    ResAttrRepository repo;

    @Autowired
    ResAttrValueRepository valueRepo;

    @Autowired
    ResSpecAttrRepository specAttrRepository;

    @ApiOperation(value="创建attr",tags={"masterfile_attr"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public JsonResult create(@RequestBody CreateBean bean) {
        try {
            System.out.println("bean:"+bean);

            DbResAttr attr = new DbResAttr();
            BeanUtils.copyProperties(bean,attr);
            attr.setActive("Y");
            attr.setCreateAt(System.currentTimeMillis());
            attr.setUpdateAt(System.currentTimeMillis());
            attr.setCreateBy(getAccount());
            attr.setUpdateBy(getAccount());
            List<DbResAttrAttrValue> attrAttrValueList = new LinkedList<>();

            for(String value:bean.getAttrValues()){
                DbResAttrAttrValue attrAttrValue = new DbResAttrAttrValue();
                DbResAttrValue attrValue = new DbResAttrValue();
                attrValue.setId(Long.parseLong(value));
                attrAttrValue.setAttr(attr);
                attrAttrValue.setAttrValue(attrValue);
                attrAttrValue.setActive("Y");
                attrAttrValue.setCreateAt(System.currentTimeMillis());
                attrAttrValue.setUpdateAt(System.currentTimeMillis());
                attrAttrValue.setCreateBy(getAccount());
                attrAttrValue.setUpdateBy(getAccount());
                attrAttrValueList.add(attrAttrValue);
            }
            attr.setAttrAttrValueList(attrAttrValueList);
            repo.saveAndFlush(attr);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
           return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="删除attr",tags={"masterfile_attr"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids) {
        return this.delete(repo,ids);
    }

    @ApiOperation(value="修改attr",tags={"masterfile_attr"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    public JsonResult edit(@RequestBody EditBean b) {
        try {
            DbResAttr resAttr = repo.findById(b.getId()).get();
            resAttr.setAttrDesc(b.getAttrDesc());
            resAttr.setAttrName(b.getAttrName());
            resAttr.setUpdateAt(System.currentTimeMillis());
            //resAttr.setCreateBy(getAccount());
            resAttr.setUpdateBy(getAccount());
            List<DbResAttrAttrValue> attrAttrValueList = resAttr.getAttrAttrValueList();
            attrAttrValueList.clear();
            for(String valueId:b.getAttrValues()){
                DbResAttrAttrValue resAttrAttrValue = new DbResAttrAttrValue();

                DbResAttrValue attrValue = new DbResAttrValue();
                attrValue.setId(Long.parseLong(valueId));

                resAttrAttrValue.setActive("Y");
                resAttrAttrValue.setCreateAt(System.currentTimeMillis());
                resAttrAttrValue.setUpdateAt(System.currentTimeMillis());
                resAttrAttrValue.setCreateBy(getAccount());
                resAttrAttrValue.setUpdateBy(getAccount());
                resAttrAttrValue.setAttr(resAttr);
                resAttrAttrValue.setAttrValue(attrValue);
                attrAttrValueList.add(resAttrAttrValue);
            }
            repo.saveAndFlush(resAttr);
            return JsonResult.success(Arrays.asList());
        } catch (NumberFormatException e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="搜索attr",tags={"masterfile_attr"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/search")
    public JsonResult search(@RequestBody SearchBean bean){
        try {
            Specification<DbResAttr> spec = Convertor.<DbResAttr>convertSpecification(bean);
            List<DbResAttr> attrList = repo.findAll(spec,PageRequest.of(bean.getPageIndex(),bean.getPageSize())).getContent();
            List<ResultBean> resultBeans = new LinkedList<>();
            for (DbResAttr resAttr : attrList) {
                //attr数据展示
                List<Map> attrDataMap = new LinkedList<>();
                ResultBean resultBean = new ResultBean();
                BeanUtils.copyProperties(resAttr,resultBean);
                resultBean.setCreateAccountName(getAccountName(resAttr.getCreateBy()));
                resultBean.setUpdateAccountName(getAccountName(resAttr.getUpdateBy()));
                String[] attrValues = new String[resAttr.getAttrAttrValueList().size()];
                long[] attrValueIds = new long[resAttr.getAttrAttrValueList().size()];

                String[] value = new String[resAttr.getAttrAttrValueList().size()];
                for(int i=0;i<resAttr.getAttrAttrValueList().size();i++) {
                    DbResAttrValue attrValue = resAttr.getAttrAttrValueList().get(i).getAttrValue();
                    attrValues[i] = attrValue.getAttrValue() != null ? attrValue.getAttrValue() : attrValue.getValueFrom()+"~"+attrValue.getValueTo();
                    attrValueIds[i] = attrValue.getId();
                    value[i] = attrValue.getAttrValue();
                }
                Map<String,Object> map = new HashMap<>();
                map.put("attrName",resAttr.getAttrName());
                map.put("attrValue",attrValues);
                attrDataMap.add(map);
                resultBean.setAttrData(attrDataMap);
                resultBean.setId(resAttr.getId());
                resultBean.setAttrValueNames(attrValues);
                resultBean.setAttrValues(attrValueIds);
                System.out.println("result:" + resultBean);
                resultBeans.add(resultBean);
            }

            return JsonResult.success(resultBeans,repo.count(spec));
        } catch (IllegalArgumentException | IllegalAccessException | BeansException e) {
           return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="根据attr搜索attrValue",tags={"masterfile_attr"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/attrSearch")
    public JsonResult attrSearch(@RequestBody EditBean bean) {
        try {
            List<ResultBean> resultBeans = new LinkedList<>();
            for (long id : bean.getIds()) {
                DbResAttr attr = repo.findById(id).get();

                ResultBean resultBean = new ResultBean();
                BeanUtils.copyProperties(attr,resultBean);
                String[] attrValues = new String[attr.getAttrAttrValueList().size()];
                long[] attrValueIds = new long[attr.getAttrAttrValueList().size()];
                for(int i=0;i<attr.getAttrAttrValueList().size();i++) {
                    DbResAttrValue attrValue = attr.getAttrAttrValueList().get(i).getAttrValue();
                    attrValues[i] = attrValue.getAttrValue();
                    attrValueIds[i] = attrValue.getId();
                }
                resultBean.setAttrValueNames(attrValues);
                resultBean.setAttrValues(attrValueIds);
                resultBeans.add(resultBean);
            }

            return JsonResult.success(resultBeans);
        } catch (BeansException e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="禁用attr",tags={"masterfile_attr"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, MasterFile_AttrCtrl.class,specAttrRepository);
    }

    @ApiOperation(value="启用attr",tags={"masterfile_attr"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj,BaseRepository... repo) {
        ResSpecAttrRepository tRepo = (ResSpecAttrRepository)repo[0];
        BaseDeleteBean bean = (BaseDeleteBean)obj;
        for (Long id : bean.getIds()) {
            DbResAttrValue attrValue = new DbResAttrValue();
            attrValue.setId(id);
            List<DbResSpecAttr> specAttrs = tRepo.findDbResSpecAttrsByAttrId(id+"");
            if ( specAttrs != null && specAttrs.size()>0 ) {
                return id;
            }
        }
        return 0;
    }
}
