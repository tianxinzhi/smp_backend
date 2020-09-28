package com.pccw.backend.ctrl;


import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.ResultRecode;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.stock_out.CreateBean;
import com.pccw.backend.bean.stock_out.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.ResLogMgtRepository;
import com.pccw.backend.repository.ResRepoRepository;
import com.pccw.backend.repository.ResSkuRepoRepository;
import com.pccw.backend.repository.ResSkuRepoSerialRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/stock_out")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Stock_OutCtrl  extends BaseCtrl<DbResLogMgt> {

    @Autowired
    private ResLogMgtRepository repo;

    @Autowired
    ResSkuRepoRepository skuRepoRepository;

    @Autowired
    ResSkuRepoSerialRepository serialRepository;

    @Autowired
    ResRepoRepository repoRepository;

    @Autowired
    Process_ProcessCtrl processProcessCtrl;



    @ApiOperation(value="",tags={""},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/search")
    public JsonResult search(@RequestBody SearchBean b) {
        log.info(b.toString());
        try {
            List<Map<String, Object>> list = skuRepoRepository.findByTypeIdAndRepoId(b.getFromRepoId(),b.getRepoType());
            List<Map<String, Object>> humpNameForList = ResultRecode.returnHumpNameForList(list);
            return JsonResult.success(humpNameForList);

        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="创建stock_out",tags={"stock_out"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/create")
    @Transactional(rollbackFor = Exception.class)
    public JsonResult create(@RequestBody CreateBean b){
        try {
            long t = new Date().getTime();
            b.setCreateAt(t);
            b.setActive("Y");
            b.setStatus(StaticVariable.STATUS_WAITING);
            for (DbResLogMgtDtl dtl : b.getLine()) {
                dtl.setUpdateAt(t);
                dtl.setCreateAt(t);
                dtl.setCreateBy(getAccount());dtl.setUpdateBy(getAccount());
                dtl.setActive("Y");
                dtl.setLogTxtBum(b.getLogTxtBum());
                if(dtl.getDtlAction().equals("D")){
                    dtl.setDtlRepoId(b.getLogRepoOut());
                }else {
                    dtl.setDtlRepoId(b.getLogRepoIn());
                    if(b.getRepoType().equals("W")) {
                        b.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_OUT_STW);
                    }else {
                        b.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_OUT_STS);
                    }

                }
                if(dtl.getSerials() != null && dtl.getSerials().size()>0){
                    for (DbResLogMgtDtlSerial serial : dtl.getSerials()) {
                        serial.setCreateAt(t);serial.setUpdateAt(t);
                        serial.setCreateBy(getAccount());serial.setUpdateBy(getAccount());
                        serial.setActive("Y");
                    }
                }
            }
            JsonResult result =this.create(repo, DbResLogMgt.class, b);
            if(result.getCode().equals("000")){
                //创建工作流对象
//                DbResProcess process = new DbResProcess();
//                process.setLogTxtBum(b.getLogTxtBum());
//                process.setRepoId(b.getLogRepoOut());
//                process.setRemark(b.getRemark());
//                process.setCreateAt(t);
//                process.setUpdateAt(t);
//                process.setLogOrderNature(b.getLogOrderNature());
//                //生成工作流数据
//                processProcessCtrl.joinToProcess(process);

                this.UpdateSkuRepoQty(b.getLogTxtBum());

            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    /**
     * 审批流完成后更新sku_repo
     * @param logTxtNum
     */
    public void UpdateSkuRepoQty(String logTxtNum) {
        try {
            DbResLogMgt b =repo.findDbResLogMgtByLogTxtBum(logTxtNum);
            long time = System.currentTimeMillis();
            for (DbResLogMgtDtl dtl : b.getLine()) {
                if (dtl.getDtlAction().equals("D")) {
                    DbResSkuRepo outSkuRepo = skuRepoRepository.findQtyByRepoAndShopAndType(b.getLogRepoOut(), dtl.getDtlSkuId(), 3L);
                    outSkuRepo.setQty(outSkuRepo.getQty()-dtl.getDtlQty());
                    outSkuRepo.setUpdateAt(time);
                    outSkuRepo.setUpdateBy(getAccount());
                    skuRepoRepository.saveAndFlush(outSkuRepo);
                    //skuRepoRepository.updateQtyByRepoAndShopAndTypeAndQty(b.getLogRepoOut(),dtl.getDtlSkuId(),3,-dtl.getDtlQty());
                } else {
                    DbResSkuRepo inSkuRepo = skuRepoRepository.findQtyByRepoAndShopAndType(b.getLogRepoIn(), dtl.getDtlSkuId(), 3L);
                    List<DbResSkuRepoSerial> serials = null;
                    if(inSkuRepo==null){
                        inSkuRepo = new DbResSkuRepo();
                        DbResRepo repo = new DbResRepo();repo.setId(b.getLogRepoIn());
                        inSkuRepo.setRepo(repo);
                        DbResSku sku = new DbResSku();sku.setId(dtl.getDtlSkuId());
                        inSkuRepo.setSku(sku);
                        DbResStockType stockType = new DbResStockType();stockType.setId(3L);
                        inSkuRepo.setStockType(stockType);
                        inSkuRepo.setQty(dtl.getDtlQty());
                        inSkuRepo.setRemark(b.getRemark());
                        inSkuRepo.setCreateAt(time);
                        inSkuRepo.setUpdateAt(time);
                        inSkuRepo.setCreateBy(getAccount());
                        inSkuRepo.setUpdateBy(getAccount());
                        inSkuRepo.setActive("Y");
                        serials = new LinkedList<>();
                    } else {
                        inSkuRepo.setQty(inSkuRepo.getQty()+dtl.getDtlQty());
                        inSkuRepo.setUpdateAt(time);
                        inSkuRepo.setUpdateBy(getAccount());
                        serials = inSkuRepo.getSerials();
                    }

                    if(dtl.getSerials() != null && dtl.getSerials().size()>0){
                        for (DbResLogMgtDtlSerial serial : dtl.getSerials()) {
                            DbResSkuRepoSerial skuSerial = new DbResSkuRepoSerial();
                            BeanUtils.copyProperties(serial,skuSerial);
                            skuSerial.setId(null);
                            skuSerial.setSkuRepo(inSkuRepo);
                            serials.add(skuSerial);
                        }
                        inSkuRepo.setSerials(serials);
                    }
                    skuRepoRepository.saveAndFlush(inSkuRepo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
