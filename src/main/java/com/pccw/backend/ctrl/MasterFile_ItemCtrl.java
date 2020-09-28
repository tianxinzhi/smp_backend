package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_item.CreateBean;
import com.pccw.backend.bean.masterfile_item.EditBean;
import com.pccw.backend.bean.masterfile_item.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResItem;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResItemRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/masterfile_item")
@Api(value = "MasterFile_ItemCtrl",tags = "masterfile_item")
public class MasterFile_ItemCtrl extends BaseCtrl<DbResItem> implements ICheck {
    @Autowired
    ResItemRepository resItemRepository;
  @ApiOperation(value = "搜索item",tags = "masterfile_item",notes = "注意问题点")
  @RequestMapping(method = RequestMethod.POST,path = "/search")
  public JsonResult search(@RequestBody SearchBean b){
      log.info(b.toString());
    return this.search(resItemRepository,b);
  }

    @ApiOperation(value = "创建item",tags = "masterfile_item",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/create")
    public JsonResult creat(@RequestBody CreateBean b){
      return this.create(resItemRepository, DbResItem.class,b);
    }

  @ApiOperation(value = "修改item",tags = "masterfile_item",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/edit")
    public JsonResult edit(@RequestBody EditBean b){
      return this.edit(resItemRepository, DbResItem.class,b);
    }

  @ApiOperation(value = "删除item",tags = "masterfile_item",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids){
      return this.delete(resItemRepository,ids);
    }

  @ApiOperation(value="禁用item",tags={"masterfile_item"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/disable")
  public JsonResult disable(@RequestBody BaseDeleteBean ids) {
    return this.disable(resItemRepository,ids, MasterFile_ItemCtrl.class);
  }

  @ApiOperation(value="启用item",tags={"masterfile_item"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/enable")
  public JsonResult enable(@RequestBody BaseDeleteBean ids) {
    return this.enable(resItemRepository,ids);
  }

  @Override
  public long checkCanDisable(Object obj, BaseRepository... check) {
    return 0;
  }
}
