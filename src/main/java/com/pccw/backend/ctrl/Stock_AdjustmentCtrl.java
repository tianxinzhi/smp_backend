package com.pccw.backend.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.fasterxml.jackson.core.JsonParser;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.lis_api.CreateBean;
import com.pccw.backend.bean.lis_api.SubmitStockUpdateBean;
import com.pccw.backend.bean.stock_adjustment.LogMgtBean;
import com.pccw.backend.bean.stock_adjustment.LogMgtDtlBean;
import com.pccw.backend.bean.stock_adjustment.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.RestTemplateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
    @RequestMapping("/stock_adjustment")
@Api(value="Stock_AdjustmentCtrl",tags={"stock_adjustment"})
public class Stock_AdjustmentCtrl extends BaseCtrl<DbResLogMgt> {

    @Autowired
    ResLogMgtRepository logMgtRepository;
    @Autowired
    ResSkuRepoRepository skuRepoRepository;
    @Autowired
    ResSkuRepository skuRepository;
    @Autowired
    ResAdjustReasonRepository reasonRepository;
    @Autowired
    ResStockTypeRepository stockTypeRepository;
    @Autowired
    Process_ProcessCtrl processProcessCtrl;
    @Value("${batchjob.ip}")
    private String host;

    @ApiOperation(value="调货",tags={"stock_adjustment"},notes="说明")
    @RequestMapping("/confirm")
    public JsonResult confirm(@RequestBody LogMgtBean bean) {
        try {
            System.out.println("logMgtBean:"+bean);
            DbResLogMgt ent = new DbResLogMgt();
            ent.setLogRepoIn(bean.getLogRepoIn());
            ent.setLogRepoOut(bean.getLogRepoOut());
            ent.setLogTxtBum(bean.getTransactionNumber());
            ent.setLogType(StaticVariable.LOGTYPE_MANAGEMENT);
            ent.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_TAKE_ADJUSTMENT);
            ent.setStatus(StaticVariable.STATUS_WAITING);
            if(bean.getReason()!=0){
                ent.setAdjustReasonId(bean.getReason());
            } else {
                //自定义adjust Reason时插入新数据
                DbResAdjustReason resAdjustReason = new DbResAdjustReason();
                resAdjustReason.setAdjustReasonName("Other Reason");
                resAdjustReason.setRemark(bean.getRemark());
                resAdjustReason.setCreateAt(bean.getCreateDate());
                resAdjustReason.setUpdateAt(bean.getCreateDate());
                resAdjustReason.setCreateBy(getAccount());
                resAdjustReason.setUpdateBy(getAccount());
                resAdjustReason.setActive("Y");
                reasonRepository.saveAndFlush(resAdjustReason);
                ent.setAdjustReasonId(resAdjustReason.getId());
            }
            ent.setCreateAt(bean.getCreateDate());
            ent.setUpdateAt(bean.getCreateDate());
            ent.setActive("Y");
            ent.setCreateBy(getAccount());
            ent.setUpdateBy(getAccount());

            List<DbResLogMgtDtl> lstMgtDtl = new LinkedList<>();

            for(LogMgtDtlBean dtl:bean.getLine()) {
                DbResLogMgtDtl mgtDtl = new DbResLogMgtDtl();
                mgtDtl.setDtlRepoId(bean.getLogRepoOut());
                BeanUtils.copyProperties(dtl, mgtDtl);
                mgtDtl.setLogTxtBum(bean.getTransactionNumber());
                //mgtDtl.setDtlLogId();
                mgtDtl.setDtlAction(dtl.getDtlQty()>0 ? StaticVariable.DTLACTION_ADD : StaticVariable.DTLACTION_DEDUCT);
                mgtDtl.setLisStatus(StaticVariable.LISSTATUS_WAITING);
                mgtDtl.setCreateAt(bean.getCreateDate());
                mgtDtl.setUpdateAt(bean.getCreateDate());
                mgtDtl.setCreateBy(getAccount());
                mgtDtl.setUpdateBy(getAccount());
                mgtDtl.setActive("Y");
                mgtDtl.setResLogMgt(ent);

                DbResStockType stockType = stockTypeRepository.findById(dtl.getCatalog()).get();

                if(stockType!=null){
                    String type = stockType.getStockTypeName();
                    String status = type.substring(type.indexOf("(")+1,type.indexOf(")"));
                    String subin = type.replace(type.substring(type.indexOf("("),type.indexOf(")")+1),"");
                    log.info("tp:"+type+",subin:"+subin+",status:"+status);
                    mgtDtl.setDtlSubin(subin);
                    mgtDtl.setStatus(status);
                }
                lstMgtDtl.add(mgtDtl);
            }

            ent.setLine(lstMgtDtl);
            logMgtRepository.saveAndFlush(ent);
//            //生成工作流数据
//            DbResProcess process = new DbResProcess();
//            process.setLogTxtBum(bean.getTransactionNumber());
//            process.setRepoId(bean.getLogRepoOut());
//            process.setRemark(bean.getRemark());
//            process.setCreateAt(System.currentTimeMillis());
//            process.setUpdateAt(System.currentTimeMillis());
//            process.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_TAKE_ADJUSTMENT);
//            processProcessCtrl.joinToProcess(process);
            this.UpdateSkuRepoQty(bean.getTransactionNumber());
            //调用Lis api同步sku
//            transApi(bean);
            return JsonResult.success(Arrays.asList());
        } catch (BeansException e) {
            return JsonResult.fail(e);
        }
    }

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public ResponseEntity<String> transApi(){
        List<CreateBean> params = new LinkedList<>();
        DateFormat format = new SimpleDateFormat("YYYYMMDD");
        JSONObject target = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        for (int i=1;i<3 ; i++) {
            CreateBean b = new CreateBean();
            SubmitStockUpdateBean bean1 = new SubmitStockUpdateBean();
            bean1.setP_API_VERSION("1.0");
            bean1.setP_INIT_MSG_LIST("T");
            bean1.setP_COMMIT("T");
            bean1.setP_RECORD_TYPE("10");
            bean1.setP_SOURCE_TYPE("SMP");
            bean1.setP_FROM_WAREHOUSE("RT-CABF-FAULT");
            bean1.setP_TRANSACTION_DATE(format.format(new Date()));
            bean1.setP_TRANSACTION_QTY(String.valueOf(30-20*i));
            bean1.setP_TRANSACTION_TYPE(Integer.valueOf(bean1.getP_TRANSACTION_QTY()) >0?"ADJ-IN":"ADJ-OUT");
            bean1.setP_ITEM_CODE("4000001");
            bean1.setP_COST_CODE("CSM3");
            bean1.setP_WORK_ORDER("38881010");
            bean1.setP_BUSINESS_DATE(format.format(new Date()));
            bean1.setP_TO_WAREHOUSE("TRD-W003");
            bean1.setP_SHIPMENT_NUM("1234");
            bean1.setP_SOURCE_REF(null);
            bean1.setP_WAYBILL("21533975");
            bean1.setP_SO_TYPE_GROUPING(null);

            jsonObject.put("@xmlns","http://xmlns.oracle.com/apps/inv/rest/XXINVMT/submit_stock_update/");
            JSONObject json1 = new JSONObject();
            json1.put("@xmlns","http://xmlns.oracle.com/apps/fnd/rest/header");
            jsonObject.put("RESTHeader",json1);
            jsonObject.put("InputParameters",bean1);
            target.put("XXINVMT",jsonObject);
            b.setCreateAt(new Date());
            b.setStatus("Pending");
            b.setRequestBody(target.toJSONString());
            b.setRequestUrl("rest/XXINVMT/SUBMIT_STOCK_UPDATE/");
            params.add(b);
        }

        return RestTemplateUtils.post(host + "createNew", params, String.class);
    }

    /**
     * process完成后更新sku库存
     * @param logTxtBum
     */
    public void UpdateSkuRepoQty(String logTxtBum) {
        DbResLogMgt bean = logMgtRepository.findDbResLogMgtByLogTxtBum(logTxtBum);
        for(DbResLogMgtDtl dtl:bean.getLine()) {
            int stockType = 0;
            switch (dtl.getDtlSubin()) {
                case StaticVariable.DTLSUBIN_DEMO:
                    stockType = 1;break;
                case StaticVariable.DTLSUBIN_FAULTY:
                    stockType = 2;break;
                case StaticVariable.DTLSUBIN_AVAILABLE:
                    stockType = 3;break;
                case StaticVariable.DTLSUBIN_RESERVED:
                    stockType = 4;break;
                case StaticVariable.DTLSUBIN_INTRANSIT:
                    stockType = 5;break;
                case StaticVariable.DTLSUBIN_RESERVED_WITH_AO:
                    stockType = 6;break;
                case StaticVariable.DTLSUBIN_RESERVED_WITH_REMOTE:
                    stockType = 7;break;
            }
            DbResSkuRepo qtyByRepoAndShopAndType = skuRepoRepository.findQtyByRepoAndShopAndType(bean.getLogRepoOut(), dtl.getDtlSkuId(), stockType);
            if(qtyByRepoAndShopAndType != null)
                skuRepoRepository.updateQtyByRepoAndShopAndTypeAndQty(bean.getLogRepoOut(),dtl.getDtlSkuId(),stockType,dtl.getDtlQty());
            else{
                DbResSkuRepo skuRepo = new DbResSkuRepo();
                DbResRepo resRepo = new DbResRepo();
                resRepo.setId(bean.getLogRepoOut());
                skuRepo.setRepo(resRepo);
                DbResSku sku = new DbResSku();
                sku.setId(dtl.getDtlSkuId());
                skuRepo.setSku(sku);
                DbResStockType type = new DbResStockType();
                type.setId((long) stockType);
                skuRepo.setStockType(type);
                skuRepo.setQty(dtl.getDtlQty());
                skuRepo.setActive("Y");
                skuRepo.setCreateAt(System.currentTimeMillis());
                skuRepo.setUpdateAt(System.currentTimeMillis());
                skuRepo.setCreateBy(getAccount());
                skuRepo.setUpdateBy(getAccount());
                skuRepoRepository.saveAndFlush(skuRepo);
            }

        }
    }

    @ApiOperation(value="sku库存",tags={"stock_adjustment"},notes="说明")
    @RequestMapping("/balanceSearch")
    public JsonResult balanceSearch(@RequestBody SearchBean bean) {
        try {
            DbResSkuRepo skuRepo = skuRepoRepository.findQtyByRepoAndShopAndType(bean.getDtlRepoId(),bean.getDtlSkuId(),bean.getCatalog());
            Map beanMap = new HashMap();
            beanMap.put("qty",skuRepo.getQty());
            if(skuRepo!=null){
                DbResSku byId = skuRepository.findById(skuRepo.getSku().getId()).get();
                beanMap.put("days",byId.getMaxReserveDays());
             }
            return JsonResult.success(Arrays.asList(beanMap));
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="根据repo查询sku库存",tags={"stock_adjustment"},notes="说明")
    @RequestMapping("/searchByRepo")
    public JsonResult searchByRepo(@RequestBody SearchBean bean) {
        try {
            DbResRepo repo = new DbResRepo();
            repo.setId(bean.getDtlRepoId());
            List<DbResSkuRepo> skuRepos = skuRepoRepository.findDbResSkuRepoByRepo(repo);
            List<SearchBean> resBeans = new LinkedList<>();
            for (DbResSkuRepo skuRepo : skuRepos) {
                SearchBean res = new SearchBean();
                DbResSku sku = skuRepo.getSku();
                res.setDtlSkuId(sku.getId());
                res.setSkuCode(sku.getSkuCode());
                res.setCatalog(skuRepo.getStockType().getId());
                res.setDtlRepoId(skuRepo.getRepo().getId());
                res.setDtlQty(skuRepo.getQty());
                resBeans.add(res);
            }
            return JsonResult.success(resBeans.stream().filter(distinctByKey(resBean -> resBean.getDtlSkuId())).collect(Collectors.toList()));
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }


    @ApiOperation(value="查询调货记录",tags={"stock_adjustment"},notes="说明")
    @RequestMapping("/search")
    public JsonResult search(@RequestBody SearchBean bean) {
        List<String> status = new ArrayList<>();
        status.add(StaticVariable.LOGORDERNATURE_STOCK_TAKE_ADJUSTMENT);
        List<DbResLogMgt> logMgts = logMgtRepository.findByLogOrderNatureIn(status);
        List<DbResLogMgt> result = logMgts.stream().map(dbResLogMgt -> {
            DbResAdjustReason reason = reasonRepository.getOne(dbResLogMgt.getAdjustReasonId());
            dbResLogMgt.setRemark(reason.getRemark());
            return dbResLogMgt;
        }).collect(Collectors.toList());
        return JsonResult.success(result);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
