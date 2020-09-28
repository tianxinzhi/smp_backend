package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_productline.CreateBean;
import com.pccw.backend.bean.masterfile_productline.EditBean;
import com.pccw.backend.bean.masterfile_productline.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResProductLine;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResProductLineRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/masterfile_productline")
@Api(value = "MasterFile_ProductLineCtrl",tags = "masterfile_productline")
public class MasterFile_ProductLineCtrl extends BaseCtrl<DbResProductLine> implements ICheck {
    @Autowired
    ResProductLineRepository resProductLineRepository;
  @ApiOperation(value = "搜索productline",tags = "masterfile_productline",notes = "注意问题点")
  @RequestMapping(method = RequestMethod.POST,path = "/search")
  public JsonResult search(@RequestBody SearchBean b){
      log.info(b.toString());
    return this.search(resProductLineRepository,b);
  }

    @ApiOperation(value = "创建productline",tags = "masterfile_productline",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/create")
    public JsonResult creat(@RequestBody CreateBean b){
      return this.create(resProductLineRepository, DbResProductLine.class,b);
    }

  @ApiOperation(value = "修改productline",tags = "masterfile_productline",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/edit")
    public JsonResult edit(@RequestBody EditBean b){
      return this.edit(resProductLineRepository, DbResProductLine.class,b);
    }

  @ApiOperation(value = "删除productline",tags = "masterfile_productline",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids){
      return this.delete(resProductLineRepository,ids);
    }

  @ApiOperation(value="禁用productline",tags={"masterfile_productline"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/disable")
  public JsonResult disable(@RequestBody BaseDeleteBean ids) {
    return this.disable(resProductLineRepository,ids, MasterFile_ProductLineCtrl.class);
  }

  @ApiOperation(value="启用productline",tags={"masterfile_productline"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/enable")
  public JsonResult enable(@RequestBody BaseDeleteBean ids) {
    return this.enable(resProductLineRepository,ids);
  }

  @Override
  public long checkCanDisable(Object obj, BaseRepository... check) {
    return 0;
  }
}
