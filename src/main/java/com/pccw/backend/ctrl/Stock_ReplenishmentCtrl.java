package com.pccw.backend.ctrl;


import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.stock_replenishment.CreateReplBean;
import com.pccw.backend.bean.stock_replenishment.EditBean;
import com.pccw.backend.bean.stock_replenishment.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@RestController
@RequestMapping("/stock_replenishment")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Stock_ReplenishmentCtrl extends BaseStockCtrl<DbResStockReplenishmentHeader> {

    @Autowired
    ResLogMgtRepository logMgtRepository;
    @Autowired
    ResSkuRepoRepository rsRepo;
    @Autowired
    ResRepoRepository rRepo;
    @Autowired
    ResStockTypeRepository rstRepo;
    @Autowired
    ResSkuRepository resSkuRepo;
    @Autowired
    ResStockReplenishmentHeaderRepository headerRepository;

    @Autowired
    private EntityManager entityManager;

    @ApiOperation(value="补货搜索",tags={"stock_replenishment"},notes="查询")
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public JsonResult search(@RequestBody SearchBean b) {
        try{

            StringBuffer baseSql = new StringBuffer(" select t1.id \"id\",t1.create_at \"createAt\",t3.account_name \"createAccountName\" ,t1.log_txt_num \"logTxtNum\",\n" +
                    "t1.from_channel_id \"fromChannelId\",t1.status \"status\"\n" +
                    "from res_stock_replenishment_header t1 left join res_stock_replenishment t2\n" +
                    "on t2.replenishment_header_id = t1.id left join res_account t3 on t1.create_by= t3.id where 1=1");

            if(!StringUtils.isEmpty(b.getSkuId())) {
                baseSql.append(" and t2.sku_id="+"'"+b.getSkuId()+"'");
            }
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (!StringUtils.isEmpty(b.getCreateAt())&&b.getCreateAt().length>0) {
                baseSql.append(" and t1.CREATE_AT between "+sdf.parse(b.getCreateAt()[0]).getTime()+" and "+sdf.parse(b.getCreateAt()[1]).getTime());
            }
            if(b.getLogTxtNum()!=null && !b.getLogTxtNum().equals("")){
                baseSql.append(" and t1.log_txt_num like '%"+b.getLogTxtNum()+"%'");
            }

            StringBuffer countBuffer = new StringBuffer(
                    "select count(*) from ("+baseSql+")t");
            baseSql.append(" order by t1.create_at desc");

            Query dataQuery = entityManager.createNativeQuery(baseSql.toString());
            Query countQuery = entityManager.createNativeQuery(countBuffer.toString());

            dataQuery.setFirstResult(b.getPageIndex()*b.getPageSize());
            dataQuery.setMaxResults(b.getPageSize());
            dataQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            BigInteger count = (BigInteger) countQuery.getSingleResult();
            Long total = count.longValue();
            List<Map> content = dataQuery.getResultList();
            for (Map map : content) {
                Query nativeQuery = entityManager.createNativeQuery("select t2.to_channel_id \"toChannelId\",\n" +
                        "t2.sku_id \"skuId\",t2.qty \"qty\",t2.request_date \"requestDate\",t2.stock \"stock\",t2.suggested_qty_1 \"suggestedQty1\",\n" +
                        "t2.suggested_qty_2 \"suggestedQty2\",t2.suggested_qty_3 \"suggestedQty3\" from res_stock_replenishment t2 where t2.replenishment_header_id="+Long.parseLong(map.get("id").toString()));
                nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map> resultList = nativeQuery.getResultList();
                map.put("line",resultList);
            }
            return JsonResult.success(content,total);
        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    /**
     * 收货
     * @param bean
     * @return
     */
    @ApiOperation(value="创建replenishment",tags={"stock_replenishment"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/create")
    @Transactional(rollbackOn = Exception.class)
    public JsonResult create(@RequestBody CreateReplBean bean) {
        try {
            long time = System.currentTimeMillis();
            bean.setLogTxtNum(genTranNum(new Date(),"RP",rRepo.findById(bean.getFromChannelId()).get().getRepoCode()));
            for (DbResStockReplenishment replenishment : bean.getLine()) {
                replenishment.setLogTxtNum(bean.getLogTxtNum());
                replenishment.setCreateAt(time);
                replenishment.setUpdateAt(time);
                replenishment.setCreateBy(getAccount());
                replenishment.setUpdateBy(getAccount());
                replenishment.setActive("Y");
                DbResSku sku = new DbResSku();sku.setId(replenishment.getSkuId());
                DbResRepo repo = new DbResRepo();repo.setId(bean.getFromChannelId());
                DbResRepo repo2 = new DbResRepo();repo2.setId(replenishment.getToChannelId());
                DbResStockType stockType = new DbResStockType();stockType.setId(3L);
                DbResSkuRepo skuRepo = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
                //扣减fromchannel available qty加到tochanel
                if(skuRepo != null){
                    DbResSkuRepo skuRepo2 = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku, stockType);
                    //to channel 存在avaible，修改qty
                    if(skuRepo2 != null){
                        skuRepo2.setUpdateBy(getAccount());
                        skuRepo2.setUpdateAt(time);
                        skuRepo2.setQty(skuRepo2.getQty()+replenishment.getQty());
                        rsRepo.saveAndFlush(skuRepo2);
                    } else {
                        //不存在avaible，新增一条
                        DbResSkuRepo value = new DbResSkuRepo();
                        value.setRepo(repo2);
                        value.setSku(sku);
                        value.setStockType(stockType);
                        value.setQty(replenishment.getQty());
//                    value.setRemark(bean.get);
                        value.setCreateAt(time);
                        value.setUpdateAt(time);
                        value.setCreateBy(getAccount());
                        value.setUpdateBy(getAccount());
                        value.setActive("Y");
                        rsRepo.saveAndFlush(value);
                    }
                    //扣除对应available的qty
                    skuRepo.setQty(skuRepo.getQty()-replenishment.getQty());
                    skuRepo.setUpdateBy(getAccount());
                    skuRepo.setUpdateAt(time);
                    rsRepo.saveAndFlush(skuRepo);
                }
                //插入日志
                DbResLogMgt logMgt = new DbResLogMgt();
                logMgt.setLogRepoOut(bean.getFromChannelId());
                logMgt.setLogRepoIn(replenishment.getToChannelId());
                logMgt.setLogTxtBum(bean.getLogTxtNum());
                logMgt.setLogType(StaticVariable.LOGTYPE_REPL);
                logMgt.setLogOrderNature(StaticVariable.LOGORDERNATURE_REPLENISHMENT_REQUEST);
                logMgt.setStatus(StaticVariable.STATUS_WAITING);
//            logMgt.setRemark(bean.getRemarks());
                logMgt.setCreateAt(time);
                logMgt.setUpdateAt(time);
                logMgt.setCreateBy(getAccount());
                logMgt.setUpdateBy(getAccount());
                logMgt.setActive("Y");

                List<DbResLogMgtDtl> line = new LinkedList<>();
                DbResLogMgtDtl dtl = new DbResLogMgtDtl();
                dtl.setDtlRepoId(replenishment.getToChannelId());
                dtl.setDtlSkuId(replenishment.getSkuId());
                dtl.setDtlQty(replenishment.getQty());
                dtl.setLogTxtBum(bean.getLogTxtNum());
                dtl.setStatus(StaticVariable.STATUS_AVAILABLE);
                dtl.setLisStatus(StaticVariable.LISSTATUS_WAITING);
                dtl.setDtlAction(StaticVariable.DTLACTION_ADD);
                dtl.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);
                dtl.setCreateAt(time);
                dtl.setUpdateAt(time);
                dtl.setCreateBy(getAccount());
                dtl.setUpdateBy(getAccount());
                dtl.setActive("Y");

                DbResLogMgtDtl dtl2 = new DbResLogMgtDtl();
                BeanUtils.copyProperties(dtl,dtl2);
                dtl2.setDtlRepoId(bean.getFromChannelId());
                dtl2.setStatus(StaticVariable.STATUS_AVAILABLE);
                dtl2.setLisStatus(StaticVariable.LISSTATUS_WAITING);
                dtl2.setDtlAction(StaticVariable.DTLACTION_DEDUCT);
                dtl2.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);

                line.add(dtl);
                line.add(dtl2);
                logMgt.setLine(line);
                logMgtRepository.saveAndFlush(logMgt);
            }
            return this.create(headerRepository,DbResStockReplenishmentHeader.class,bean);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }


    @ApiOperation(value="删除",tags={"stock_replenishment"},notes="")
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    @Transactional(rollbackOn = Exception.class)
    public JsonResult delete(@RequestBody EditBean bean) {
        try {
            System.out.println(bean);
            long time = System.currentTimeMillis();
            DbResStockReplenishmentHeader replenishmentHeader = headerRepository.findById(bean.getId()).get();
            replenishmentHeader.setUpdateAt(time);
            replenishmentHeader.setUpdateBy(getAccount());
            replenishmentHeader.setActive("N");
            headerRepository.saveAndFlush(replenishmentHeader);

            for (DbResStockReplenishment replenishment : replenishmentHeader.getLine()) {
                DbResSku sku = new DbResSku();sku.setId(replenishment.getSkuId());
                DbResRepo repo = new DbResRepo();repo.setId(replenishmentHeader.getFromChannelId());
                DbResRepo repo2 = new DbResRepo();repo2.setId(replenishment.getToChannelId());
                DbResStockType stockType = new DbResStockType();stockType.setId(3L);
                DbResSkuRepo skuRepo = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
                //之前扣减的加到available qty
                if (skuRepo != null) {
                    skuRepo.setQty(skuRepo.getQty()+replenishment.getQty());
                    skuRepo.setUpdateAt(time);
                    skuRepo.setUpdateBy(getAccount());
                    rsRepo.saveAndFlush(skuRepo);
                }
                DbResSkuRepo skuRepo2 = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku, stockType);
                //扣减之前available的qty
                if (skuRepo2 != null) {
                    skuRepo2.setQty(skuRepo2.getQty()-replenishment.getQty());
                    skuRepo2.setUpdateAt(time);
                    skuRepo2.setUpdateBy(getAccount());
                    rsRepo.saveAndFlush(skuRepo2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
        return JsonResult.success(Arrays.asList());
    }

    @ApiOperation(value="修改",tags={"stock_replenishment"},notes="")
    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    @Transactional(rollbackOn = Exception.class)
    public JsonResult edit(@RequestBody EditBean bean) {
        try {
            System.out.println(bean.toString());
            DbResStockReplenishmentHeader replenishmentHeader = headerRepository.findById(bean.getId()).get();
            long time = System.currentTimeMillis();
            for (DbResStockReplenishment replenishment : replenishmentHeader.getLine()) {
                DbResSku sku = new DbResSku();sku.setId(replenishment.getSkuId());
                DbResRepo repo = new DbResRepo();repo.setId(replenishmentHeader.getFromChannelId());
                DbResRepo repo2 = new DbResRepo();repo2.setId(replenishment.getToChannelId());
                DbResStockType stockType = new DbResStockType();stockType.setId(3L);

                DbResSkuRepo skuRepo = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
                //avalible的加上之前faulty的qty，扣减新的faulty qty
                if(skuRepo!=null){
                    skuRepo.setQty(skuRepo.getQty()+replenishment.getQty());
                    skuRepo.setUpdateAt(time);
                    skuRepo.setUpdateBy(getAccount());
                    rsRepo.saveAndFlush(skuRepo);
                }
                //faulty的减去之前的qty
                DbResSkuRepo skuRepo2 = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku, stockType);
                if(skuRepo2 != null){
                    skuRepo2.setUpdateAt(time);
                    skuRepo2.setUpdateBy(getAccount());
                    skuRepo2.setQty(skuRepo2.getQty()-replenishment.getQty());
                    rsRepo.saveAndFlush(skuRepo2);
                }
            }
            replenishmentHeader.getLine().clear();
            for (DbResStockReplenishment newReplenishment : bean.getLine()) {
                newReplenishment.setUpdateAt(time);
                newReplenishment.setUpdateBy(getAccount());
                //修改了repo或者sku
                DbResSku sku2 = new DbResSku();sku2.setId(newReplenishment.getSkuId());
                DbResRepo repo2 = new DbResRepo();repo2.setId(bean.getFromChannelId());
                DbResRepo repo3 = new DbResRepo();repo3.setId(newReplenishment.getToChannelId());
                DbResStockType stockType2 = new DbResStockType();stockType2.setId(3L);
                DbResSkuRepo value = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku2, stockType2);
                //找到之前的from available加上之前的faulty qty
                if(value != null) {
                    value.setQty(value.getQty()-newReplenishment.getQty());
                    value.setUpdateAt(time);
                    value.setUpdateBy(getAccount());
                    rsRepo.saveAndFlush(value);

                    DbResSkuRepo value2 = rsRepo.findDbResSkuRepoByRepoAndSkuAndStockType(repo3, sku2, stockType2);
                    //找到现在的available，减去现在的qty
                    if(value2!=null){
                        value2.setQty(value2.getQty()+newReplenishment.getQty());
                        value2.setUpdateAt(time);
                        value2.setUpdateBy(getAccount());
                        rsRepo.saveAndFlush(value2);
                    } else{
                        DbResSkuRepo newReserved = new DbResSkuRepo();
                        newReserved.setRepo(repo3);
                        newReserved.setSku(sku2);
                        newReserved.setStockType(stockType2);
                        newReserved.setQty(newReplenishment.getQty());
//                                newReserved.setRemark(bean.getRemarks());
                        newReserved.setCreateAt(time);
                        newReserved.setUpdateAt(time);
                        newReserved.setCreateBy(getAccount());
                        newReserved.setUpdateBy(getAccount());
                        newReserved.setActive("Y");
                        rsRepo.saveAndFlush(value);
                    }
                }
                replenishmentHeader.getLine().add(newReplenishment);
            }
            return this.edit(headerRepository, DbResStockReplenishmentHeader.class, bean);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    /**
     * stock replenishment 修改表 skuRepo
     * @param logTxtBum
     */
    public void UpdateSkuRepoQty(String logTxtBum) {

        DbResLogMgt cb = logMgtRepository.findDbResLogMgtByLogTxtBum(logTxtBum);
        List<DbResLogMgtDtl> line = cb.getLine();
        long t = new Date().getTime();
        //插入表res_sku_repo 添加或修改qty(工作流加入后 需要process的status为approve状态时再入库sku_repo)
        line.forEach(dtl->{
            if(!StaticVariable.LOGORDERNATURE_REPLENISHMENT_REQUEST.equals(cb.getLogOrderNature())){
                DbResSkuRepo skuShop = rsRepo.findQtyByRepoAndShopAndType(cb.getLogRepoIn(), dtl.getDtlSkuId(), 3l);
                if(!Objects.isNull(skuShop)){
                    DbResSkuRepo skuShop1 = rsRepo.findById(skuShop.getId()).get();
                    skuShop1.setQty((skuShop.getQty()+dtl.getDtlQty()));
                    skuShop1.setUpdateAt(t);
                    skuShop1.setUpdateBy(getAccount());
                }else{
                    DbResSkuRepo skuShop2 = new DbResSkuRepo();
                    skuShop2.setCreateAt(t);
                    skuShop2.setCreateBy(getAccount());
                    skuShop2.setUpdateAt(t);
                    skuShop2.setUpdateBy(getAccount());
                    skuShop2.setActive("Y");
                    skuShop2.setQty(dtl.getDtlQty());
                    skuShop2.setRepo(rRepo.findById(cb.getLogRepoIn()).get());
                    skuShop2.setStockType(rstRepo.findById(3l).get());
                    skuShop2.setSku(resSkuRepo.findById(dtl.getDtlSkuId()).get());
                    rsRepo.saveAndFlush(skuShop2);
                }
            }
        });

    }

}
