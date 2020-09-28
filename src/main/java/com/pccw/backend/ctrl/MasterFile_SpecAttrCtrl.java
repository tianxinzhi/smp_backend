package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_spec_attr.CreateBean;
import com.pccw.backend.bean.masterfile_spec_attr.EditBean;
import com.pccw.backend.bean.masterfile_spec_attr.SearchBean;
import com.pccw.backend.entity.DbResSpecAttr;
import com.pccw.backend.repository.ResSpecAttrRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("masterfile_spec_attr")
@Api(value="MasterFile_SpecAttrCtrl",tags={"masterfile_spec_attr"})
public class MasterFile_SpecAttrCtrl extends BaseCtrl<DbResSpecAttr> {

    @Autowired
    ResSpecAttrRepository repo;

    @RequestMapping(method = RequestMethod.POST,value = "/create")
    @ApiOperation(value="创建spec_attr",tags={"masterfile_spec_attr"},notes="注意问题点")
    public JsonResult create(@RequestBody CreateBean bean) {
        return this.create(repo,DbResSpecAttr.class,bean);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    @ApiOperation(value="删除spec_attr",tags={"masterfile_spec_attr"},notes="注意问题点")
    public JsonResult delete(@RequestBody BaseDeleteBean ids) {
        return this.delete(repo,ids);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    @ApiOperation(value="修改spec_attr",tags={"masterfile_spec_attr"},notes="注意问题点")
    public JsonResult edit(@RequestBody EditBean b) {
        return this.edit(repo, DbResSpecAttr.class, b);
    }

    @RequestMapping(method = RequestMethod.POST,value = "/search")
    @ApiOperation(value="搜索spec_attr",tags={"masterfile_spec_attr"},notes="注意问题点")
    public JsonResult search(@RequestBody SearchBean bean) {
        return this.search(repo,bean);
    }
}
