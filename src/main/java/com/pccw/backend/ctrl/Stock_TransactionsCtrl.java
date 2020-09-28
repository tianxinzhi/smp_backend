package com.pccw.backend.ctrl;

import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.stock_category.CategoryLogMgtBean;
import com.pccw.backend.bean.stock_transactions.CreateBean;
import com.pccw.backend.bean.stock_transactions.SearchBean;
import com.pccw.backend.bean.stock_transactions.SearchViewBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.ResLogMgtRepository;
import com.pccw.backend.repository.ResSkuRepoRepository;
import com.pccw.backend.repository.ResSkuRepository;
import com.pccw.backend.repository.ResStockTypeRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-06-12 14:48
 **/
@Slf4j
@RestController
@RequestMapping("/stock_transactions")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Stock_TransactionsCtrl {

    @Autowired
    ResSkuRepoRepository skuRepoRepository;

    @Autowired
    ResLogMgtRepository logMgtRepository;

    @Autowired
    ResSkuRepository skuRepository;

    @Autowired
    ResStockTypeRepository stockTypeRepository;

    @ApiOperation(value="创建stock_category",tags={"stock_category"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/create")
    public JsonResult create(@RequestBody CreateBean b){
        long typeId = 0;
//        JsonResult result = null;
        switch (b.getLogOrderNature()){
            case "IN":
                typeId = Long.parseLong(b.getToStockTypeId());
                break;
            case "OUT":
                typeId = Long.parseLong(b.getFromStockTypeId());
//                result = qtyCheck(b, typeId);
                break;
            case "TRANSFER":
                typeId = Long.parseLong(b.getFromStockTypeId());
//                result = qtyCheck(b, typeId);
                break;
        }
        JsonResult result = qtyCheck(b, typeId);
        if (result != null) return result;

        updataBalance(b, typeId);

        return JsonResult.success(Arrays.asList());
    }

    @Transactional(rollbackFor = Exception.class)
    public void updataBalance(@RequestBody CreateBean b, long typeId) {
        //将传入信息存入log表
        //商店和Type存入头表，其余存入行标
        //行表中Subinventory From/To存入dtlSubin，dtlAction字段A表示From，D表示To
        DbResLogMgt dbResLogMgt = new DbResLogMgt();
        ArrayList list= new ArrayList<>();
        long t = new Date().getTime();
        b.setCreateAt(t);
        b.setUpdateAt(t);
        b.setActive("Y");
        BeanUtils.copyProperties(b,dbResLogMgt);
        DbResLogMgtDtl dbResLogMgtDtlFrom = new DbResLogMgtDtl();
        dbResLogMgtDtlFrom.setDtlSubin(b.getFromStockTypeId());
        dbResLogMgtDtlFrom.setDtlSkuId(b.getDtlSkuId());
        dbResLogMgtDtlFrom.setDtlAction("A");
        dbResLogMgtDtlFrom.setDtlQty(b.getDtlQty());

        DbResLogMgtDtl dbResLogMgtDtlTo = new DbResLogMgtDtl();
        dbResLogMgtDtlTo.setDtlSkuId(b.getDtlSkuId());
        dbResLogMgtDtlTo.setDtlSubin(b.getToStockTypeId());
        dbResLogMgtDtlTo.setDtlAction("D");
        dbResLogMgtDtlTo.setDtlQty(b.getDtlQty());

        list.add(dbResLogMgtDtlFrom);
        list.add(dbResLogMgtDtlTo);
        dbResLogMgt.setLine(list);

        logMgtRepository.saveAndFlush(dbResLogMgt);

        //修改sku_repo表的数量（暂时按照POC的现有逻辑）
        //IN是to的加
        //OUT是from的减
        //TRANSFER是from减并且to加

        DbResSkuRepo toBalance,fromBalance;
        long dtlQty = b.getDtlQty();
        switch (b.getLogOrderNature()){
            case "IN":
                toBalance = skuRepoRepository.findQtyByRepoAndShopAndType(b.getLogRepoOut(),b.getDtlSkuId(), typeId);
                if (toBalance == null) {
                    toBalance = new DbResSkuRepo();

                    toBalance.setQty(dtlQty);

                    DbResSku dbResSku = new DbResSku();
                    dbResSku.setId(b.getDtlSkuId());
                    toBalance.setSku(dbResSku);

                    DbResStockType stockType = new DbResStockType();
                    stockType.setId(Long.parseLong(b.getToStockTypeId()));
                    toBalance.setStockType(stockType);

                    DbResRepo dbResRepo = new DbResRepo();
                    dbResRepo.setId(b.getLogRepoOut());
                    toBalance.setRepo(dbResRepo);

                }else {
                    long qtyOnhand = toBalance.getQty();
                    toBalance.setQty(qtyOnhand+dtlQty);
                }
                skuRepoRepository.saveAndFlush(toBalance);
                break;
            case "OUT":
                fromBalance = skuRepoRepository.findQtyByRepoAndShopAndType(b.getLogRepoOut(),b.getDtlSkuId(), typeId);
                long qtyOnhand = fromBalance.getQty();
                fromBalance.setQty(qtyOnhand-dtlQty);
                skuRepoRepository.saveAndFlush(fromBalance);
                break;
            case "TRANSFER":
                fromBalance = skuRepoRepository.findQtyByRepoAndShopAndType(b.getLogRepoOut(),b.getDtlSkuId(), typeId);
                toBalance = skuRepoRepository.findQtyByRepoAndShopAndType(b.getLogRepoOut(),b.getDtlSkuId(), Long.parseLong(b.getToStockTypeId()));
                if (toBalance == null) {
                    toBalance = new DbResSkuRepo();

                    toBalance.setQty(dtlQty);

                    DbResSku dbResSku = new DbResSku();
                    dbResSku.setId(b.getDtlSkuId());
                    toBalance.setSku(dbResSku);

                    DbResStockType stockType = new DbResStockType();
                    stockType.setId(Long.parseLong(b.getToStockTypeId()));
                    toBalance.setStockType(stockType);

                    DbResRepo dbResRepo = new DbResRepo();
                    dbResRepo.setId(b.getLogRepoOut());
                    toBalance.setRepo(dbResRepo);

                }else {
                    long qtyToOnHand = toBalance.getQty();
                    toBalance.setQty(qtyToOnHand+dtlQty);
                }
                long qtyFromOnHand = fromBalance.getQty();
                fromBalance.setQty(qtyFromOnHand-dtlQty);

                skuRepoRepository.saveAndFlush(toBalance);
                skuRepoRepository.saveAndFlush(fromBalance);

                break;
        }
    }

    private JsonResult qtyCheck(@RequestBody CreateBean b, long typeId) {
        //查询所选的repo、sku、type在sku_repo表中的qty是否大于传入的数量
        Long dbResSkuRepo = skuRepoRepository.findQtyByRepoAndSkuAndType(b.getLogRepoOut(),b.getDtlSkuId(), typeId);
        if (dbResSkuRepo != null) {
            if (dbResSkuRepo.longValue() <= b.getDtlQty()) {
                return new JsonResult("success", "888","Insufficient quantity ", null);
            }
        }else {
            return new JsonResult("success", "888","Stork Item can not use", null);
        }
        return null;
    }

//    @PostMapping("/availableSku")
//    public JsonResult seachSku(@RequestBody long skuid){
//        System.out.println(skuid);
//        return JsonResult.success(Arrays.asList());
//    }

    @ApiOperation(value="查询stock_category",tags={"stock_category"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/search")
    public JsonResult<SearchViewBean> search(@RequestBody SearchBean bean){
        List<String> natures = new ArrayList<>();
        if (bean.getNature() == null) {
            String[] status = {"OUT","IN","TRANSFER"};
             natures = Arrays.asList(status);
        }else {
            natures = Arrays.asList(bean.getNature());
        }
//        Long skuId = bean.getSkuId() == 0 ? null : bean.getSkuId();

        List<DbResLogMgt> logMgtList = bean.getSkuId() == 0 ? logMgtRepository.findByLogOrderNatureIn(natures):logMgtRepository.findByLogOrderNatureInAndSkuId(natures,bean.getSkuId());


        List<SearchViewBean> viewBeans = logMgtList.stream().map(logMgt -> {
            SearchViewBean searchViewBean = new SearchViewBean();
            long dtlSkuId = logMgt.getLine().get(0).getDtlSkuId();

            DbResSku dbResSku = skuRepository.findById(dtlSkuId).get();
            String skuCode = dbResSku.getSkuCode();
            searchViewBean.setId(logMgt.getId());
            searchViewBean.setReference("");
            searchViewBean.setType(logMgt.getLogOrderNature());
            searchViewBean.setItemCode(skuCode);
            searchViewBean.setQty(logMgt.getLine().get(0).getDtlQty());

            DbResStockType stockType0 = stockTypeRepository.findById(Long.parseLong(logMgt.getLine().get(0).getDtlSubin())).get();
            DbResStockType stockType1 = stockTypeRepository.findById(Long.parseLong(logMgt.getLine().get(1).getDtlSubin())).get();

            if (logMgt.getLine().get(0).getDtlAction().equals("A")) {
                searchViewBean.setFromStockType(stockType0.getStockTypeName());
            } else {
                searchViewBean.setToStockType(stockType0.getStockTypeName());
            }

            if (logMgt.getLine().get(1).getDtlAction().equals("A")) {
                searchViewBean.setFromStockType(stockType1.getStockTypeName());
            } else {
                searchViewBean.setToStockType(stockType1.getStockTypeName());
            }

            return searchViewBean;
        }).sorted(Comparator.comparing(SearchViewBean::getId).reversed()).collect(Collectors.toList());

        return JsonResult.success(viewBeans);
    }

}
