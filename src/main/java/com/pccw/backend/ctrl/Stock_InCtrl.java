package com.pccw.backend.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.stock_in.CreateBean;
import com.pccw.backend.bean.stock_in.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.ResLogMgtRepository;
import com.pccw.backend.repository.ResSkuRepoRepository;
import com.pccw.backend.repository.ResSkuRepoSerialRepository;
import com.pccw.backend.repository.ResStockInRepository;
import com.pccw.backend.util.Session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/stock_in")
@Api(value="Stock_InCtrl",tags={"stock_in"})
public class Stock_InCtrl extends BaseCtrl<DbResLogMgt> {

    @Autowired
    ResStockInRepository rsipo;

    @Autowired
    ResSkuRepoRepository skuRepoRepository;

    @Autowired
    Process_ProcessCtrl processProcessCtrl;

    @Autowired
    ResLogMgtRepository logMgtRepository;

    @Autowired
    ResSkuRepoSerialRepository serialRepository;

    @Autowired
    Session session;

    @ApiOperation(value="stock_in",tags={"stock_in"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    @Transactional(rollbackFor = Exception.class)
    public JsonResult create(@RequestBody CreateBean bean) {
        try {
            long t = new Date().getTime();
            bean.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_IN_WITHOUT_PO_STW);
            List<DbResLogMgtDtl> lineList = bean.getLine();
            for (DbResLogMgtDtl dtl : lineList) {
                dtl.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);
                dtl.setDtlAction(StaticVariable.DTLACTION_ADD);
                dtl.setStatus(StaticVariable.STATUS_AVAILABLE);
                dtl.setLisStatus(StaticVariable.LISSTATUS_WAITING);
                dtl.setLogTxtBum(bean.getLogTxtBum());
                dtl.setDtlRepoId(bean.getLogRepoIn());
                dtl.setCreateAt(t);dtl.setUpdateAt(t);
                dtl.setCreateBy(getAccount());dtl.setUpdateBy(getAccount());
                dtl.setActive("Y");
                dtl.setId(null);
                if(dtl.getSerials() != null && dtl.getSerials().size()>0){
                    for (DbResLogMgtDtlSerial serial : dtl.getSerials()) {
                        serial.setCreateAt(t);serial.setUpdateAt(t);
                        serial.setCreateBy(getAccount());serial.setUpdateBy(getAccount());
                        serial.setActive("Y");
                    }
                }
            }

            bean.setLine(lineList);

            JsonResult result = this.create(rsipo, DbResLogMgt.class, bean);

            if(result.getCode().equals("000")){
                //创建工作流对象
//                DbResProcess process = new DbResProcess();
//
//                process.setLogTxtBum(bean.getLogTxtBum());
//                process.setRepoId(bean.getLogRepoIn());
//                process.setRemark(bean.getRemark());
//                process.setCreateAt(t);
//                process.setUpdateAt(t);
//                process.setLogOrderNature(bean.getLogOrderNature());
//
//                //生成工作流数据
//                processProcessCtrl.joinToProcess(process);

                //生成sku serial
                this.UpdateSkuRepoQty(bean.getLogTxtBum());
            }

            return result;
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    /**
     * stock in without PO 和 stock in 有PO 存skuRepo表
     * @param logTxtBum
     */
    public void UpdateSkuRepoQty(String logTxtBum) {

        try {
            DbResLogMgt bean = rsipo.findDbResLogMgtByLogTxtBum(logTxtBum);

            for (DbResLogMgtDtl line:bean.getLine()){
                DbResSku dbResSku = new DbResSku();
                dbResSku.setId(line.getDtlSkuId());
                DbResRepo dbResRepo = new DbResRepo();
                dbResRepo.setId(bean.getLogRepoIn());
                DbResStockType dbResStockType = new DbResStockType();
                dbResStockType.setId(3L);
                DbResSkuRepo skuRepo = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(dbResRepo, dbResSku, dbResStockType);

                List<DbResSkuRepoSerial> serials = null;
                if (Objects.isNull(skuRepo)){

                    skuRepo = new DbResSkuRepo();
                    skuRepo.setRepo(dbResRepo);skuRepo.setSku(dbResSku);skuRepo.setStockType(dbResStockType);
                    skuRepo.setQty(line.getDtlQty());skuRepo.setActive("Y");
                    skuRepo.setCreateBy(bean.getCreateBy());
                    skuRepo.setCreateAt(bean.getCreateAt());
                    skuRepo.setUpdateAt(bean.getCreateAt());
                    skuRepo.setUpdateBy(bean.getUpdateBy());
                    serials = new LinkedList<>();
                    //skuRepo = skuRepoRepository.saveAndFlush(skuRepo);
                }else {
                    skuRepo.setUpdateAt(System.currentTimeMillis());
                    skuRepo.setUpdateBy(getAccount());
                    skuRepo.setQty(skuRepo.getQty()+line.getDtlQty());
                    serials = skuRepo.getSerials();
                    //skuRepo = skuRepoRepository.saveAndFlush(skuRepo);
                }
                if(line.getSerials() != null && line.getSerials().size()>0){

                    for (DbResLogMgtDtlSerial serial : line.getSerials()) {
                        DbResSkuRepoSerial skuSerial = new DbResSkuRepoSerial();
                        BeanUtils.copyProperties(serial,skuSerial);
                        skuSerial.setId(null);
                        skuSerial.setSkuRepo(skuRepo);

                        serials.add(skuSerial);
                    }
                    skuRepo.setSerials(serials);
                }
                skuRepoRepository.saveAndFlush(skuRepo);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value="stock_out_info",tags={"searchStockOutInfo"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,path="/searchStockOutInfo")
    public JsonResult searchStockOutInfo(@RequestBody SearchBean bean){
        try {
//            List stockOutInfo = rsipo.getStockOutInfo(bean.getLogTxtNum());
//            List res = ResultRecode.returnHumpNameForList(stockOutInfo);
            Object sessionUser = session.getUser();
            Map<String,List> map = JSONObject.parseObject(JSONObject.toJSONString(sessionUser), Map.class);
            List<Integer> orgIds = new ArrayList(map.get("orgIds"));
            List<Long> ids = orgIds.stream().map(id -> {
                return Long.parseLong(id.toString());
            }).collect(Collectors.toList());

            List<String> natures = new ArrayList<>();
//            natures.add("SOTS");
//            natures.add("SOTW");
            natures.add(StaticVariable.LOGORDERNATURE_STOCK_OUT_STS);
            natures.add(StaticVariable.LOGORDERNATURE_STOCK_OUT_STW);

            List<DbResLogMgt> collect = new ArrayList<>();
            if (bean.getDeliveryNumber() == null) {
                collect = rsipo.findAllByLogOrderNatureInAndLogRepoInIn(natures, ids);
            }else {
                collect = rsipo.findAllByLogOrderNatureInAndLogRepoInInAndDeliveryNumberLike(natures,ids,"%"+bean.getDeliveryNumber()+"%");
            }
            List<Object> res = collect.stream().map(r -> {
                List<DbResLogMgtDtl> Line = r.getLine().stream().filter(line -> line.getStatus().equals("INT")).collect(Collectors.toList());
                r.setLine(Line);
                return r;
            }).collect(Collectors.toList());
            return JsonResult.success(res);
        }catch (Exception e){
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="stock_in_PO",tags={"stock_in_PO"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/createPOInfos")
    public JsonResult createPOInfos(@RequestBody CreateBean bean) {
        try {
            long t = new Date().getTime();
            bean.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_IN_STS);
            List<DbResLogMgtDtl> lineList = bean.getLine();
            List<DbResSkuRepo> skuRepoList = new ArrayList<DbResSkuRepo>();
            for (int i = 0; i <lineList.size() ; i++) {
                lineList.get(i).setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);
                lineList.get(i).setDtlAction(StaticVariable.DTLACTION_ADD);
                lineList.get(i).setStatus(StaticVariable.STATUS_AVAILABLE);
                lineList.get(i).setLisStatus(StaticVariable.LISSTATUS_WAITING);
                lineList.get(i).setCreateAt(t);
                lineList.get(i).setUpdateAt(t);
                lineList.get(i).setId(null);
            }
            bean.setLine(lineList);

            JsonResult result = this.create(rsipo, DbResLogMgt.class, bean);

            if(result.getCode().equals("000")){


                DbResLogMgt originMgt = logMgtRepository.getOne(bean.getOriginId());
                originMgt.setStatus("C");
                logMgtRepository.saveAndFlush(originMgt);

                //创建工作流对象
//                DbResProcess process = new DbResProcess();
//
//                process.setLogTxtBum(bean.getLogTxtBum());
//                process.setRepoId(bean.getLogRepoOut());
//                process.setRemark(bean.getRemark());
//                process.setCreateAt(t);
//                process.setUpdateAt(t);
//                process.setLogOrderNature(bean.getLogOrderNature());

                //生成工作流数据
//                processProcessCtrl.joinToProcess(process);

                this.UpdateSkuRepoQty(bean.getLogTxtBum());
            }

            return result;


        }catch (Exception e){
            return JsonResult.fail(e);
        }
    }
}
