package com.pccw.backend.ctrl;

import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.interfaceForOrdering.InputBean;
import com.pccw.backend.bean.interfaceForOrdering.InputItemBean;
import com.pccw.backend.bean.stock_return.CreateBean;
import com.pccw.backend.bean.stock_return.EditBean;
import com.pccw.backend.bean.stock_return.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/stock_return")
@Api(value="Stock_ReturnCtrl",tags={"stock_return"})
public class Stock_ReturnCtrl extends BaseStockCtrl<DbResStockReturn> {

    @Autowired
    ResLogMgtRepository logMgtRepository;
    @Autowired
    ResSkuRepoRepository skuRepoRepository;
    @Autowired
    ResStockReturnRepository returnRepository;
    @Autowired
    ResReservationRuleRepository ruleRepository;
    @Autowired
    ResSkuRepository skuRepository;
    @Autowired
    ResRepoRepository repoRepository;

    @ApiOperation(value="退货",tags={"stock_return"},notes="查询")
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public JsonResult search(@RequestBody SearchBean bean) {
        try {
            System.out.println(bean.toString());
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.ASC,"returnDate"));
            Specification<DbResStockReturn> spec = Convertor.<DbResStockReturn>convertSpecification(bean);
            List<DbResStockReturn> list = returnRepository.findAll(spec, PageRequest.of(bean.getPageIndex(),bean.getPageSize(),Sort.by(orders))).getContent();

            return JsonResult.success(list,returnRepository.count(spec));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="退货",tags={"stock_return"},notes="新增")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public JsonResult create(@RequestBody CreateBean bean) {
        try {
            bean.setLogTxtNum(genTranNum(new Date(),"RT",repoRepository.findById(bean.getFromChannel()).get().getRepoCode()));
            long time = System.currentTimeMillis();
            for (DbResStockReturnSerial serial : bean.getLine()) {
                serial.setSkuId(bean.getSkuId());
                serial.setCreateAt(time);
                serial.setCreateBy(getAccount());
                serial.setUpdateAt(time);
                serial.setUpdateBy(getAccount());
                serial.setActive("Y");
            }
            this.create(returnRepository,DbResStockReturn.class,bean);
            DbResSku sku = new DbResSku();sku.setId(bean.getSkuId());
            DbResRepo repo = new DbResRepo();repo.setId(bean.getFromChannel());
            DbResStockType stockType = new DbResStockType();stockType.setId(3L);
            DbResSkuRepo skuRepo = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
            //扣减available qty加到faulty qty
            if(skuRepo != null){
                DbResStockType stockType2 = new DbResStockType();stockType2.setId(2L);
                DbResSkuRepo skuRepo2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType2);
                //存在faulty，修改qty
                if(skuRepo2 != null){
                    skuRepo2.setUpdateBy(getAccount());
                    skuRepo2.setUpdateAt(time);
                    skuRepo2.setQty(skuRepo2.getQty()+bean.getQty());
                    skuRepoRepository.saveAndFlush(skuRepo2);
                } else {
                    //不存在faulty，新增一条
                    DbResSkuRepo value = new DbResSkuRepo();
                    value.setRepo(repo);
                    value.setSku(sku);
                    value.setStockType(stockType2);
                    value.setQty(bean.getQty());
                    value.setRemark(bean.getRemarks());
                    value.setCreateAt(time);
                    value.setUpdateAt(time);
                    value.setCreateBy(getAccount());
                    value.setUpdateBy(getAccount());
                    value.setActive("Y");
                    skuRepoRepository.saveAndFlush(value);
                }
                //扣除对应available的qty
                skuRepo.setQty(skuRepo.getQty()-bean.getQty());
                skuRepo.setUpdateBy(getAccount());
                skuRepo.setUpdateAt(time);
                skuRepoRepository.saveAndFlush(skuRepo);
            }
            //插入日志
            DbResLogMgt logMgt = new DbResLogMgt();
            logMgt.setLogRepoOut(bean.getFromChannel());
            logMgt.setLogRepoIn(bean.getToWareHouse());
            logMgt.setLogTxtBum(bean.getLogTxtNum());
            logMgt.setLogType(StaticVariable.LOGTYPE_MANAGEMENT);
            logMgt.setLogOrderNature(StaticVariable.LOGORDERNATURE_RETURN);
            logMgt.setStatus(StaticVariable.STATUS_WAITING);
            logMgt.setRemark(bean.getRemarks());
            logMgt.setCreateAt(time);
            logMgt.setUpdateAt(time);
            logMgt.setCreateBy(getAccount());
            logMgt.setUpdateBy(getAccount());
            logMgt.setActive("Y");

            List<DbResLogMgtDtl> line = new LinkedList<>();
            DbResLogMgtDtl dtl = new DbResLogMgtDtl();
            dtl.setDtlRepoId(bean.getToWareHouse());
            dtl.setDtlSkuId(bean.getSkuId());
            dtl.setDtlQty(bean.getQty());
            dtl.setLogTxtBum(bean.getLogTxtNum());
            dtl.setStatus(StaticVariable.STATUS_FAULTY);
            dtl.setLisStatus(StaticVariable.LISSTATUS_WAITING);
            dtl.setDtlAction(StaticVariable.DTLACTION_ADD);
            dtl.setDtlSubin(StaticVariable.DTLSUBIN_FAULTY);
            dtl.setCreateAt(time);
            dtl.setUpdateAt(time);
            dtl.setCreateBy(getAccount());
            dtl.setUpdateBy(getAccount());
            dtl.setActive("Y");

            DbResLogMgtDtl dtl2 = new DbResLogMgtDtl();
            BeanUtils.copyProperties(dtl,dtl2);
            dtl2.setDtlRepoId(bean.getFromChannel());
            dtl2.setStatus(StaticVariable.STATUS_AVAILABLE);
            dtl2.setLisStatus(StaticVariable.LISSTATUS_WAITING);
            dtl2.setDtlAction(StaticVariable.DTLACTION_DEDUCT);
            dtl2.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);


            line.add(dtl);
            line.add(dtl2);
            logMgt.setLine(line);
            logMgtRepository.saveAndFlush(logMgt);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
        return JsonResult.success(Arrays.asList());
    }

    @ApiOperation(value="删除",tags={"stock_return"},notes="")
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    @Transactional(rollbackOn = Exception.class)
    public JsonResult delete(@RequestBody EditBean bean) {
        try {
            System.out.println(bean);
            long time = System.currentTimeMillis();
            DbResStockReturn DbResStockReturn = returnRepository.findById(bean.getId()).get();
            DbResStockReturn.setUpdateAt(time);
            DbResStockReturn.setUpdateBy(getAccount());
            DbResStockReturn.setActive("N");
            returnRepository.saveAndFlush(DbResStockReturn);

            DbResSku sku = new DbResSku();sku.setId(bean.getSkuId());
            DbResRepo repo = new DbResRepo();repo.setId(bean.getFromChannel());
            DbResStockType stockType = new DbResStockType();stockType.setId(3L);
            DbResSkuRepo skuRepo = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
            //删除时，扣减之前的faulty qty 加到available qty
            if(skuRepo != null){
                skuRepo.setQty(skuRepo.getQty()+DbResStockReturn.getQty());
                skuRepo.setUpdateAt(time);
                skuRepo.setUpdateBy(getAccount());
                skuRepoRepository.saveAndFlush(skuRepo);
            }
            DbResStockType stockType2 = new DbResStockType();stockType2.setId(2L);
            DbResSkuRepo skuRepo2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType2);
            //扣减之前faulty的qty
            if(skuRepo2 != null){
                skuRepo2.setQty(skuRepo2.getQty()-DbResStockReturn.getQty());
                skuRepo2.setUpdateAt(time);
                skuRepo2.setUpdateBy(getAccount());
                skuRepoRepository.saveAndFlush(skuRepo2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
        return JsonResult.success(Arrays.asList());
    }

    @ApiOperation(value="修改",tags={"stock_return"},notes="")
    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    @Transactional(rollbackOn = Exception.class)
    public JsonResult edit(@RequestBody EditBean bean) {
        try {
            System.out.println(bean.toString());
            DbResStockReturn stockReturn = returnRepository.findById(bean.getId()).get();
            long time = System.currentTimeMillis();
            //没有修改repo和sku，即扣减之前faulty的qty，添加新的faulty qty
            if(bean.getFromChannel() == stockReturn.getFromChannel() &&
                bean.getSkuId() == stockReturn.getSkuId()){
                DbResSku sku = new DbResSku();sku.setId(bean.getSkuId());
                DbResRepo repo = new DbResRepo();repo.setId(bean.getFromChannel());
                DbResStockType stockType = new DbResStockType();stockType.setId(3L);

                DbResSkuRepo skuRepo = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
                //avalible的加上之前faulty的qty，扣减新的faulty qty
                if(skuRepo!=null){
                    skuRepo.setQty(skuRepo.getQty()+stockReturn.getQty()-bean.getQty());
                    skuRepo.setUpdateAt(time);
                    skuRepo.setUpdateBy(getAccount());
                    skuRepoRepository.saveAndFlush(skuRepo);
                }
                //faulty的减去之前的qty加上现在的qty
                DbResStockType stockType2 = new DbResStockType();stockType2.setId(2L);
                DbResSkuRepo skuRepo2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType2);
                if(skuRepo2 != null){
                    skuRepo2.setUpdateAt(time);
                    skuRepo2.setUpdateBy(getAccount());
                    skuRepo2.setQty(skuRepo2.getQty()-stockReturn.getQty()+bean.getQty());
                    skuRepoRepository.saveAndFlush(skuRepo2);
                }
            } else {
            //修改了repo或者sku
                DbResSku sku2 = new DbResSku();sku2.setId(stockReturn.getSkuId());
                DbResRepo repo2 = new DbResRepo();repo2.setId(stockReturn.getFromChannel());
                DbResStockType stockType2 = new DbResStockType();stockType2.setId(3L);
                DbResSkuRepo value = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku2, stockType2);
                //找到之前的available加上之前的faulty qty
                if(value != null) {
                    value.setQty(value.getQty()+stockReturn.getQty());
                    value.setUpdateAt(time);
                    value.setUpdateBy(getAccount());
                    skuRepoRepository.saveAndFlush(value);

                    DbResSku sku3 = new DbResSku();sku3.setId(bean.getSkuId());
                    DbResRepo repo3 = new DbResRepo();repo3.setId(bean.getFromChannel());
                    DbResSkuRepo value2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo3, sku3, stockType2);
                    //找到现在的available，减去现在的qty
                    if(value2!=null){
                        value2.setQty(value2.getQty()-bean.getQty());
                        value2.setUpdateAt(time);
                        value2.setUpdateBy(getAccount());
                        skuRepoRepository.saveAndFlush(value2);

                        DbResStockType stockType3 = new DbResStockType();stockType3.setId(2L);
                        //找到之前的faulty减去之前qty
                        DbResSkuRepo resereved = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku2, stockType3);
                        if(resereved!=null){
                            resereved.setUpdateAt(time);
                            resereved.setUpdateBy(getAccount());
                            resereved.setQty(resereved.getQty()-stockReturn.getQty());
                            skuRepoRepository.saveAndFlush(resereved);

                            //找到现在的faulty加上现在的qty
                            DbResSkuRepo resereved1 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo3, sku3, stockType3);
                            if(resereved1 != null){
                                resereved1.setUpdateAt(time);
                                resereved1.setUpdateBy(getAccount());
                                resereved1.setQty(resereved1.getQty()+bean.getQty());
                                skuRepoRepository.saveAndFlush(value2);
                            } else {
                                DbResSkuRepo newReserved = new DbResSkuRepo();
                                newReserved.setRepo(repo3);
                                newReserved.setSku(sku3);
                                newReserved.setStockType(stockType3);
                                newReserved.setQty(bean.getQty());
                                newReserved.setRemark(bean.getRemarks());
                                newReserved.setCreateAt(time);
                                newReserved.setUpdateAt(time);
                                newReserved.setCreateBy(getAccount());
                                newReserved.setUpdateBy(getAccount());
                                newReserved.setActive("Y");
                                skuRepoRepository.saveAndFlush(value);
                            }
                        }
                    }
                }
            }
            stockReturn.getLine().clear();
            for (DbResStockReturnSerial serial : bean.getLine()) {
                serial.setSkuId(bean.getSkuId());
                serial.setCreateAt(time);
                serial.setCreateBy(getAccount());
                serial.setUpdateAt(time);
                serial.setUpdateBy(getAccount());
                serial.setActive("Y");
                stockReturn.getLine().add(serial);
            }
            this.edit(returnRepository, DbResStockReturn.class, bean);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
        return JsonResult.success(Arrays.asList());
    }


    /**
     * 给第三方api调用
     * @param b
     * @param txtNum
     * @return
     */
    public String outerApi(InputBean b,String txtNum){
        return "";
    }

}
