package com.pccw.backend.ctrl;


import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.stock_threshold.EditBean;
import com.pccw.backend.bean.stock_threshold.SearchBean;
import com.pccw.backend.bean.stock_threshold.ThresholdLogMgtBean;
import com.pccw.backend.entity.DbResLogMgt;
import com.pccw.backend.entity.DbResProcess;
import com.pccw.backend.repository.ResLogMgtRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/stock_threshold")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Stock_ThresholdCtrl extends BaseCtrl<DbResLogMgt> {

    @Autowired
    private ResLogMgtRepository resLogMgtRepository;
    @Autowired
    Process_ProcessCtrl processProcessCtrl;

    @ApiOperation(value="创建stock_threshold",tags={"stock_threshold"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/create")
    public JsonResult create(@RequestBody ThresholdLogMgtBean b){
        try {
            long t = new Date().getTime();
            if(Objects.nonNull(b.getLine()) && b.getLine().size() > 0){
                b.getLine().forEach(l->{
                    l.setCreateAt(t);
                    l.setCreateBy(getAccount());
                    l.setUpdateAt(t);
                    l.setUpdateBy(getAccount());
                    l.setActive("Y");
                });
            }
            JsonResult jsonResult = this.create(resLogMgtRepository, DbResLogMgt.class, b);
            //创建工作流对象
            if(jsonResult.getCode().equals("000")) {
//                DbResProcess process = new DbResProcess();
//                process.setLogTxtBum(b.getLogTxtBum());
//                process.setRepoId(b.getLogRepoOut());
//                process.setRemark(b.getRemark());
//                process.setCreateAt(t);
//                process.setUpdateAt(t);
//                process.setLogOrderNature(b.getLogOrderNature());
//                //生成工作流数据
//                processProcessCtrl.joinToProcess(process);

            }
            return jsonResult;
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="搜索threshold",tags={"stock_threshold"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    public JsonResult search(@RequestBody SearchBean b) {
        try {
            Long repoId = Objects.isNull(b.getRepoId()) ? 0L : Long.parseLong(b.getRepoId());
            List<String> skuId = b.getSkuId() == null || b.getSkuId().size() == 0  ? null : b.getSkuId();
            //查询所有预警数据
//            List<Map> list = resLogMgtRepository.getStockThreshold(repoId,skuId);
            List<Map> list = resLogMgtRepository.getStockThreshold(repoId);
            if (skuId != null) {
                list = list.stream().filter(map -> skuId.contains(map.get("skuId").toString())).collect(Collectors.toList());
            }
            return JsonResult.success(list);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="编辑threshold",tags={"stock_threshold"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/edit")
    public JsonResult edit(@RequestBody EditBean b) {
        try {
            long t = new Date().getTime();
            resLogMgtRepository.updateDbResLogMgtDtlById(b.getQtyy(),b.getId());
            //创建工作流对象
//            DbResProcess dbResProcess = new DbResProcess();
//            dbResProcess.setLogTxtBum(b.getLogTxtBum());
//            dbResProcess.setRepoId(b.getRepoId());
//            dbResProcess.setRemark(b.getRemark());
//            dbResProcess.setCreateAt(t);
//            dbResProcess.setUpdateAt(t);
//            dbResProcess.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_THRESHOLD);
//            //生成工作流数据
//            processProcessCtrl.joinToProcess(dbResProcess);

            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value = "禁用threshold", tags = {"stock_threshold"}, notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST, value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        for (Long id: ids.getIds()){
            DbResLogMgt dbResLogMgt = resLogMgtRepository.findById(id).get();
            dbResLogMgt.setActive("N");
            dbResLogMgt.getLine().get(0).setActive("N");
            resLogMgtRepository.saveAndFlush(dbResLogMgt);
        }
        return JsonResult.success(Arrays.asList());
    }

    @ApiOperation(value="启用threshold",tags={"stock_threshold"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        for (Long id: ids.getIds()){
            DbResLogMgt dbResLogMgt = resLogMgtRepository.findById(id).get();
            dbResLogMgt.setActive("Y");
            dbResLogMgt.getLine().get(0).setActive("Y");
            resLogMgtRepository.saveAndFlush(dbResLogMgt);
        }
        return JsonResult.success(Arrays.asList());
    }

}
