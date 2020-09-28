package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_workorder.CreateBean;
import com.pccw.backend.bean.masterfile_workorder.EditBean;
import com.pccw.backend.bean.masterfile_workorder.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResWorkOrder;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResWorkOrderRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/masterfile_workorder")
@Api(value = "MasterFile_WorkOrderCtrl",tags = "masterfile_workorder")
public class MasterFile_WorkOrderCtrl extends BaseCtrl<DbResWorkOrder> implements ICheck {
    @Autowired
    ResWorkOrderRepository resWorkOrderRepository;
  @ApiOperation(value = "搜索workorder",tags = "masterfile_workorder",notes = "注意问题点")
  @RequestMapping(method = RequestMethod.POST,path = "/search")
  public JsonResult search(@RequestBody SearchBean b){

      log.info(b.toString());
    return this.search(resWorkOrderRepository,b);
  }

    @ApiOperation(value = "创建workorder",tags = "masterfile_workorder",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/create")
    public JsonResult creat(@RequestBody CreateBean b){
      return this.create(resWorkOrderRepository, DbResWorkOrder.class,b);
    }

  @ApiOperation(value = "修改workorder",tags = "masterfile_workorder",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/edit")
    public JsonResult edit(@RequestBody EditBean b){
      return this.edit(resWorkOrderRepository, DbResWorkOrder.class,b);
    }

  @ApiOperation(value = "删除workorder",tags = "masterfile_workorder",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids){
      return this.delete(resWorkOrderRepository,ids);
    }

  @ApiOperation(value="禁用workorder",tags={"masterfile_workorder"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/disable")
  public JsonResult disable(@RequestBody BaseDeleteBean ids) {
    return this.disable(resWorkOrderRepository,ids, MasterFile_WorkOrderCtrl.class);
  }

  @ApiOperation(value="启用workorder",tags={"masterfile_workorder"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/enable")
  public JsonResult enable(@RequestBody BaseDeleteBean ids) {
    return this.enable(resWorkOrderRepository,ids);
  }

  @Override
  public long checkCanDisable(Object obj, BaseRepository... check) {
    return 0;
  }
}
