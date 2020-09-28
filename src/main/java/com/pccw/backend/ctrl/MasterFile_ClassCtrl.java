package com.pccw.backend.ctrl;


import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_class.CreateBean;
import com.pccw.backend.bean.masterfile_class.EditBean;
import com.pccw.backend.bean.masterfile_class.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResClass;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResClassRepository;
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

/**
 * AuthRightCtrl
 */

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST, origins = "*", allowCredentials = "false")
@RequestMapping("/masterfile_class")
@Api(value="MasterFile_ClassCtrl",tags={"masterfile_class"})
public class MasterFile_ClassCtrl extends BaseCtrl<DbResClass> implements ICheck {

    @Autowired
    ResClassRepository repo;

    @ApiOperation(value="搜索class",tags={"masterfile_class"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    public JsonResult search(@RequestBody SearchBean b) {
        log.info(b.toString());
        try {
            Specification spec = Convertor.convertSpecification(b);
            Sort sort = new Sort(Sort.Direction.DESC,"id");
            ArrayList<SearchBean> list = new ArrayList<>();
            List<DbResClass> res =repo.findAll(spec, PageRequest.of(b.getPageIndex(),b.getPageSize(),sort)).getContent();
            res.forEach(d->{
                SearchBean searchBean = new SearchBean();
                BeanUtils.copyProperties(d,searchBean);
                searchBean.setCreateAccountName(getAccountName(d.getCreateBy()));
                searchBean.setUpdateAccountName(getAccountName(d.getUpdateBy()));
                list.add(searchBean);
            });
            return JsonResult.success(list,repo.count(spec));
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @ApiOperation(value="删除class",tags={"masterfile_class"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids) {
        for(Long id:ids.getIds()){
            List<DbResClass> list = repo.getDbResClasssByParentClassId(String.valueOf(id));
            if(Objects.nonNull(list) && list.size() > 0){
                for(DbResClass cla: list){
                    DbResClass c = repo.findById(cla.getId()).get();
                    c.setParentClassId("0");
                }
            }
        }
        return this.delete(repo, ids);
    }
    @Transactional(rollbackOn = Exception.class)
    @ApiOperation(value="创建class",tags={"masterfile_class"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/create")
    public JsonResult create(@RequestBody CreateBean b) {
        try {
            long t = new Date().getTime();
            b.setCreateAt(t);
            b.setCreateBy(getAccount());
            b.setUpdateAt(t);
            b.setUpdateBy(getAccount());
            b.setActive("Y");
            if(StringUtils.isEmpty(b.getParentClassId())){
                b.setParentClassId("0");
            }
            DbResClass dbResClass = new DbResClass();
            BeanUtils.copyProperties(b, dbResClass);
            repo.saveAndFlush(dbResClass);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @ApiOperation(value="编辑class",tags={"masterfile_class"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/edit")
    public JsonResult edit(@RequestBody EditBean b) {
        try {
            Optional<DbResClass> opt = repo.findById(b.getId());
            DbResClass dbResClass = opt.get();
            b.setUpdateAt(new Date().getTime());
            b.setUpdateBy(getAccount());
            b.setCreateAt(dbResClass.getCreateAt());
            b.setCreateBy(dbResClass.getCreateBy());
            b.setActive(dbResClass.getActive());
            if(StringUtils.isEmpty(b.getParentClassId())){
                b.setParentClassId("0");
            }
            BeanUtils.copyProperties(b, dbResClass);
            repo.saveAndFlush(dbResClass);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="禁用class",tags={"masterfile_class"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, MasterFile_ClassCtrl.class,repo);
    }

    @ApiOperation(value="启用class",tags={"masterfile_class"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj, BaseRepository... check) {
        ResClassRepository tRepo = (ResClassRepository)check[0];
        BaseDeleteBean bean = (BaseDeleteBean)obj;
        for (Long id : bean.getIds()) {
                DbResClass dbResClass = tRepo.findById(id).get();
            if (dbResClass.getRelationOfTypeClass() != null && dbResClass.getRelationOfTypeClass().size()>0 ) {
                return id;
            }
        }
        return 0;
    }
}
