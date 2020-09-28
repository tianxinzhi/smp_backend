package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_attr_value.CreateBean;
import com.pccw.backend.bean.masterfile_attr_value.EditBean;
import com.pccw.backend.bean.masterfile_attr_value.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResAttrAttrValue;
import com.pccw.backend.entity.DbResAttrValue;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResAttrAttrValueRepository;
import com.pccw.backend.repository.ResAttrValueRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("masterfile_attr_value")
@Api(value = "MasterFile_AttrValueCtrl" ,tags = {"masterfile_attr_value"})
public class MasterFile_AttrValueCtrl extends BaseCtrl<DbResAttrValue> implements ICheck {


    @Autowired
    ResAttrValueRepository repo;
    @Autowired
    ResAttrAttrValueRepository attrAttrValueRepository;

    @ApiOperation(value="创建attr_value",tags={"masterfile_attr_value"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public JsonResult create(@RequestBody CreateBean bean) {
        return this.create(repo,DbResAttrValue.class,bean);
    }

    @ApiOperation(value="删除attr_value",tags={"masterfile_attr_value"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids) {
        return this.delete(repo,ids);
    }

    @ApiOperation(value="修改attr_value",tags={"masterfile_attr_value"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    public JsonResult edit(@RequestBody EditBean b) {
        return this.edit(repo, DbResAttrValue.class, b);
    }

    @ApiOperation(value="搜索attr_value",tags={"masterfile_attr_value"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/search")
    public JsonResult search(@RequestBody SearchBean bean) {
        return this.search(repo,bean);
    }

    @ApiOperation(value="禁用attr_value",tags={"masterfile_attr_value"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, MasterFile_AttrValueCtrl.class,attrAttrValueRepository);
    }

    @ApiOperation(value="启用attr_value",tags={"masterfile_attr_value"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj,BaseRepository... repo) {
        ResAttrAttrValueRepository tRepo = (ResAttrAttrValueRepository)repo[0];
        BaseDeleteBean bean = (BaseDeleteBean)obj;
        for (Long id : bean.getIds()) {
            DbResAttrValue attrValue = new DbResAttrValue();
            attrValue.setId(id);
            List<DbResAttrAttrValue> attrAttrValues = tRepo.findDbResAttrAttrValuesByAttrValue(attrValue);
            if ( attrAttrValues != null && attrAttrValues.size()>0 ) {
                return id;
            }
        }
        return 0;
    }
}
