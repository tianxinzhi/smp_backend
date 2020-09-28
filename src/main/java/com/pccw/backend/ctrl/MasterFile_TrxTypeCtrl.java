package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_trx_type.CreateBean;
import com.pccw.backend.bean.masterfile_trx_type.EditBean;
import com.pccw.backend.bean.masterfile_trx_type.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResAttrAttrValue;
import com.pccw.backend.entity.DbResAttrValue;
import com.pccw.backend.entity.DbResTrxType;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResAttrAttrValueRepository;
import com.pccw.backend.repository.ResAttrValueRepository;
import com.pccw.backend.repository.ResTrxTypeRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("masterfile_trx_type")
@Api(value = "MasterFile_TrxTypeCtrl" ,tags = {"masterfile_trx_type"})
public class MasterFile_TrxTypeCtrl extends BaseCtrl<DbResTrxType> implements ICheck {

    @Autowired
    ResTrxTypeRepository repo;

    @ApiOperation(value="创建trx_type",tags={"masterfile_trx_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public JsonResult create(@RequestBody CreateBean bean) {
        return this.create(repo,DbResTrxType.class,bean);
    }

    @ApiOperation(value="删除trx_type",tags={"masterfile_trx_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids) {
        return this.delete(repo,ids);
    }

    @ApiOperation(value="修改trx_type",tags={"masterfile_trx_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    public JsonResult edit(@RequestBody EditBean b) {
        return this.edit(repo, DbResTrxType.class, b);
    }

    @ApiOperation(value="搜索trx_type",tags={"masterfile_trx_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/search")
    public JsonResult search(@RequestBody SearchBean bean) {
        return this.search(repo,bean);
    }

    @ApiOperation(value="禁用trx_type",tags={"masterfile_trx_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, MasterFile_TrxTypeCtrl.class);
    }

    @ApiOperation(value="启用trx_type",tags={"masterfile_trx_type"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj,BaseRepository... repo) {
        return 0;
    }
}
