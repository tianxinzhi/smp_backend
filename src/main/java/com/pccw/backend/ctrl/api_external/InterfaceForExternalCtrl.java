package com.pccw.backend.ctrl.api_external;

import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.ResultRecode;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.api_external.CreateSIWPOBean;
import com.pccw.backend.bean.api_external.SearchStockBalanceBean;
import com.pccw.backend.bean.api_external.api_stock_in.CreateSIFSBean;
import com.pccw.backend.bean.api_external.api_stock_out.ApiStockBean;
import com.pccw.backend.bean.api_external.api_stock_out.ApiStockDtlBean;
import com.pccw.backend.ctrl.BaseCtrl;
import com.pccw.backend.ctrl.BaseStockCtrl;
import com.pccw.backend.entity.DbResLogMgt;
import com.pccw.backend.entity.DbResLogMgtDtl;
import com.pccw.backend.entity.DbResRepo;
import com.pccw.backend.entity.DbResSku;
import com.pccw.backend.repository.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-01 14:20
 **/
@Slf4j
@RestController
@RequestMapping("/api/external/v1")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class InterfaceForExternalCtrl extends BaseStockCtrl<DbResLogMgt> {


    @Autowired
    ResStockInRepository resStockInRepository;

    @Autowired
    ResRepoRepository resRepoRepository;

    @Autowired
    ResSkuRepository resSkuRepository;

    @Autowired
    ResSkuRepoRepository resSkuRepoRepository;

    @Autowired
    ResLogMgtRepository repo;


    @ApiOperation(value="stock_in_without_PO",tags={"stock_in_without_PO"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/createSIWPO")
    public JsonResult createSIWPO(@RequestBody @Validated CreateSIWPOBean bean){

        try {
            bean.setCreateBy(0);

            Date date = new Date();
            long t = date.getTime();

            String transationNumber = genTranNum(date,"I",bean.getChannelCodeTo());
            bean.setLogTxtBum(transationNumber);


            DbResRepo repo = resRepoRepository.findDbResRepoByRepoCode(bean.getChannelCodeTo());
            if (Objects.isNull(repo)) {
                return JsonResult.fail("Can't find the channel");
            }else {
                bean.setLogRepoIn(repo.getId());
            }

            bean.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_IN_WITHOUT_PO_STW);
            List<DbResLogMgtDtl> lineList = bean.getLine();
            for (int i = 0; i <lineList.size() ; i++) {
                DbResSku sku = resSkuRepository.findDbResSkuBySkuCode(lineList.get(i).getSkuCode());
                if (Objects.isNull(sku)) {
                    return JsonResult.fail("Can't find the skuCode"+lineList.get(i).getSkuCode());
                }else {
                    lineList.get(i).setDtlSkuId(sku.getId());
                }


                lineList.get(i).setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);
                lineList.get(i).setDtlAction(StaticVariable.DTLACTION_ADD);
                lineList.get(i).setStatus(StaticVariable.STATUS_AVAILABLE);
                lineList.get(i).setLisStatus(StaticVariable.LISSTATUS_WAITING);
                lineList.get(i).setLogTxtBum(bean.getLogTxtBum());
                lineList.get(i).setDtlRepoId(bean.getLogRepoIn());
    //            lineList.get(i).setDtlRepoId(repo.getId());
                lineList.get(i).setCreateAt(t);
                lineList.get(i).setUpdateAt(t);
                lineList.get(i).setId(null);
                lineList.get(i).setActive("Y");
            }

            bean.setLine(lineList);

            JsonResult result = this.create(resStockInRepository, DbResLogMgt.class, bean);
            if(result.getCode().equals("000")){
                List outputData = new ArrayList();
                HashMap<String, Object> map = new HashMap<>();
                map.put("txtnum",transationNumber);
                outputData.add(map);
                result.setData(outputData);
            }
            return result;
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }


    @ApiOperation(value="stock_in_PO",tags={"stock_in_PO"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/createSIFS")
    public JsonResult createPO(@RequestBody @Validated CreateSIFSBean sisfBean) {
        try {
            if (sisfBean.getDnNumber() == null) {
                return JsonResult.fail(" DeliveryNumber cannot be null ");
            }


            List<DbResLogMgt> logMgts = repo.findByDeliveryNumber(sisfBean.getDnNumber());

            if (logMgts.size() > 0) {
                return JsonResult.fail("DeliveryNumber cannot be repeated ");
            }


            List list = new ArrayList();

            for(CreateSIWPOBean bean:sisfBean.getBatch()){
                DbResRepo repoTo = resRepoRepository.findDbResRepoByRepoCode(bean.getChannelCodeTo());
                if (Objects.isNull(repoTo)) {
                    return JsonResult.fail("Can't find the channel" + bean.getChannelCodeTo());
                } else {
                    bean.setLogRepoIn(repoTo.getId());
                }

                DbResRepo repoFrom = resRepoRepository.findDbResRepoByRepoCode(bean.getChannelCodeFrom());
                if (Objects.isNull(repoFrom)) {
                    return JsonResult.fail("Can't find the channel" + bean.getChannelCodeFrom());
                } else {
                    bean.setLogRepoIn(repoFrom.getId());
                }

                bean.setCreateBy(0);
                bean.setDeliveryNumber(sisfBean.getDnNumber());
                Date date = new Date();
                long t = date.getTime();

                String transationNumber = genTranNum(date, "I", bean.getChannelCodeTo());
                bean.setLogTxtBum(transationNumber);

                bean.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_IN_STS);
                List<DbResLogMgtDtl> lineList = bean.getLine();
                for (int i = 0; i < lineList.size(); i++) {

                    DbResSku sku = resSkuRepository.findDbResSkuBySkuCode(lineList.get(i).getSkuCode());
                    if (Objects.isNull(sku)) {
                        return JsonResult.fail("Can't find the skuCode" + lineList.get(i).getSkuCode());
                    } else {
                        lineList.get(i).setDtlSkuId(sku.getId());
                    }

                    lineList.get(i).setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);
                    lineList.get(i).setDtlAction(StaticVariable.DTLACTION_ADD);
                    lineList.get(i).setStatus(StaticVariable.STATUS_AVAILABLE);
                    lineList.get(i).setLisStatus(StaticVariable.LISSTATUS_WAITING);
                    lineList.get(i).setCreateAt(t);
                    lineList.get(i).setUpdateAt(t);
                    lineList.get(i).setId(null);
                }
                bean.setLine(lineList);

                JsonResult result = this.create(repo, DbResLogMgt.class, bean);
                HashMap<String, Object> map = new HashMap<>();
                if (result.getCode().equals("000")) {
                    map.put("txtnum", transationNumber);
                    list.add(map);
                } else {
                    return result;
                }
            }






            return JsonResult.success(list);


        }catch (Exception e){
            return JsonResult.fail(e);
        }
    }


    @RequestMapping(method=RequestMethod.POST,path="/searchStockBalance")
    public JsonResult<Map> searchStockBalance(@RequestBody SearchStockBalanceBean sc)
    {
        try {
            String repoNum = "";
            String skuNum = Objects.isNull(sc.getSkuNum()) ? "" : sc.getSkuNum();
            if (Objects.nonNull(sc.getChannelCode())) {

                DbResRepo repo = resRepoRepository.findDbResRepoByRepoCode(sc.getChannelCode());
                if (Objects.nonNull(repo)) {
                    repoNum = String.valueOf(repo.getId());

                }else {
                    return JsonResult.fail("Can't find the channel");
                }
            }

            if (Objects.nonNull(sc.getSkuNum())){

                DbResSku sku = resSkuRepository.findDbResSkuBySkuCode(skuNum);
                if (Objects.isNull(sku)){
                    return JsonResult.fail("Can't find the skuCode "+skuNum);
                }
            }


            List<Map> res = resSkuRepoRepository.getStockBalanceInfo(skuNum,repoNum);
            List<Map> result = ResultRecode.returnResult(res);
            return JsonResult.success(result);
        } catch (Exception e) {
            // log.error(e, t);
            return JsonResult.fail(e);
        }
    }



    @ApiOperation(value="创建stock_out",tags={"stock_out"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/createStockOut")
    public JsonResult create(@RequestBody ApiStockBean b){
         String errorMsg="";
        try {
            long t = new Date().getTime();
            DbResRepo out = resRepoRepository.findFirst1ByRepoCode(b.getChannelOutCode());
            DbResRepo in = resRepoRepository.findFirst1ByRepoCode(b.getChannelInCode());
            if(out == null){
                errorMsg = "Can't find the from channel: "+b.getChannelOutCode();
                return JsonResult.fail(errorMsg);
            }
            if(in == null){
                errorMsg = "Can't find the to channel: "+b.getChannelInCode();
                return JsonResult.fail(errorMsg);
            }
            for (ApiStockDtlBean apiStockDtlBean : b.getLine()) {
                List<DbResSku> skus = resSkuRepository.getDbResSkusBySkuCode(apiStockDtlBean.getDtlSkuCode());
                if(skus==null||skus.size()<=0){
                    errorMsg = "Can't find the sku: "+apiStockDtlBean.getDtlSkuCode();
                    return JsonResult.fail(errorMsg);
                }
            }

            String transNum = genTranNum(new Date(),"O",b.getChannelOutCode());
            DbResLogMgt mgt = new DbResLogMgt();
            mgt.setLogRepoOut(out.getId());
            mgt.setLogRepoIn(in.getId());
            mgt.setLogOrderNature(b.getChannelType().equals("S") ?StaticVariable.LOGORDERNATURE_STOCK_OUT_STS:StaticVariable.LOGORDERNATURE_STOCK_OUT_STW);
            mgt.setLogTxtBum(transNum);
            mgt.setLogType(StaticVariable.LOGTYPE_MANAGEMENT);
            mgt.setRemark(b.getRemark());
            mgt.setStatus(StaticVariable.STATUS_WAITING);
            mgt.setUpdateAt(t);
            mgt.setCreateAt(t);
            mgt.setCreateBy(0L);
            mgt.setUpdateBy(0L);
            mgt.setActive("Y");

            List<DbResLogMgtDtl> dtls = new LinkedList<>();

            for (ApiStockDtlBean dtl : b.getLine()) {
                DbResSku sku = resSkuRepository.getDbResSkusBySkuCode(dtl.getDtlSkuCode()).get(0);
                DbResLogMgtDtl va = new DbResLogMgtDtl();
                va.setDtlRepoId(out.getId());
                va.setDtlSkuId(sku.getId());
                va.setDtlQty(dtl.getDtlQty());
                va.setDtlSubin(StaticVariable.DTLSUBIN_GOOD);
                va.setDtlAction(StaticVariable.DTLACTION_DEDUCT);
                va.setLisStatus(StaticVariable.LISSTATUS_WAITING);
                va.setStatus(StaticVariable.STATUS_AVAILABLE);
                va.setLogTxtBum(transNum);
                va.setUpdateAt(t);
                va.setCreateAt(t);
                va.setCreateBy(0L);
                va.setUpdateBy(0L);
                va.setActive("Y");


                DbResLogMgtDtl va2 = new DbResLogMgtDtl();
                va2.setDtlRepoId(in.getId());
                va2.setDtlSkuId(sku.getId());
                va2.setDtlQty(dtl.getDtlQty());
                va2.setDtlSubin("Intra");
                va2.setDtlAction(StaticVariable.DTLACTION_ADD);
                va2.setLisStatus(StaticVariable.LISSTATUS_WAITING);
                va2.setStatus("INT");
                va2.setLogTxtBum(transNum);
                va2.setUpdateAt(t);
                va2.setCreateAt(t);
                va2.setCreateBy(0L);
                va2.setUpdateBy(0L);
                va2.setActive("Y");

                dtls.add(va);
                dtls.add(va2);
            }

            mgt.setLine(dtls);
            repo.saveAndFlush(mgt);
            List outputData = new ArrayList();
            HashMap<String, Object> map = new HashMap<>();
            map.put("txtnum",transNum);
            outputData.add(map);
            return JsonResult.success(outputData);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

}
