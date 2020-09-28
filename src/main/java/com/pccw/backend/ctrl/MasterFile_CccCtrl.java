package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_ccc.CreateBean;
import com.pccw.backend.bean.masterfile_ccc.EditBean;
import com.pccw.backend.bean.masterfile_ccc.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResCcc;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResCccRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/masterfile_ccc")
@Api(value = "MasterFile_CccCtrl",tags = "masterfile_ccc")
public class MasterFile_CccCtrl extends BaseCtrl<DbResCcc> implements ICheck {

    @Autowired
    ResCccRepository resCccRepository;
  @ApiOperation(value = "搜索ccc",tags = "masterfile_ccc",notes = "注意问题点")
  @RequestMapping(method = RequestMethod.POST,path = "/search")
  public JsonResult search(@RequestBody SearchBean b){

      log.info(b.toString());
    return this.search(resCccRepository,b);
  }

    @ApiOperation(value = "创建ccc",tags = "masterfile_ccc",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/create")
    public JsonResult creat(@RequestBody CreateBean b){
      return this.create(resCccRepository, DbResCcc.class,b);
    }

  @ApiOperation(value = "修改ccc",tags = "masterfile_ccc",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/edit")
    public JsonResult edit(@RequestBody EditBean b){
      return this.edit(resCccRepository, DbResCcc.class,b);
    }

  @ApiOperation(value = "删除ccc",tags = "masterfile_ccc",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids){
      return this.delete(resCccRepository,ids);
    }

  @ApiOperation(value="禁用ccc",tags={"masterfile_ccc"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/disable")
  public JsonResult disable(@RequestBody BaseDeleteBean ids) {
    return this.disable(resCccRepository,ids, MasterFile_CccCtrl.class);
  }

  @ApiOperation(value="启用ccc",tags={"masterfile_ccc"},notes="注意问题点")
  @RequestMapping(method = RequestMethod.POST,value = "/enable")
  public JsonResult enable(@RequestBody BaseDeleteBean ids) {
    return this.enable(resCccRepository,ids);
  }

  @Override
  public long checkCanDisable(Object obj, BaseRepository... check) {
    return 0;
  }
}
