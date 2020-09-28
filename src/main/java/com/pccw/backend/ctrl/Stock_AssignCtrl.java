package com.pccw.backend.ctrl;


import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.ResultRecode;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.stock_assign.SearchBean;
import com.pccw.backend.entity.DbResLogMgt;
import com.pccw.backend.entity.DbResProcess;
import com.pccw.backend.repository.ResLogMgtRepository;
import com.pccw.backend.repository.ResRepoRepository;
import com.pccw.backend.repository.ResSkuRepoRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/stock_assign")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Stock_AssignCtrl extends BaseCtrl<DbResLogMgt> {



    @Autowired
    private ResSkuRepoRepository repo;

    @Autowired
    ResRepoRepository repoRepository;

    @Autowired
    Process_ProcessCtrl processProcessCtrl;



    @ApiOperation(value="",tags={""},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/search")
    public JsonResult search(@RequestBody SearchBean sc) {
        try {
            log.info(sc.toString());
            String repoNum = Objects.isNull(sc.getRepoNum()) ? "" : sc.getRepoNum();
            String skuNum = Objects.isNull(sc.getSkuNum()) ? "" : sc.getSkuNum();
            List<Map> res = repo.getStockBalanceInfo(skuNum,repoNum);
            List<Map> result = ResultRecode.returnResult(res);
            return JsonResult.success(result);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="创建stock_out",tags={"stock_out"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/create")
    public JsonResult create(@RequestBody SearchBean b){
        try {
            return this.search(repo,  b);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }




}