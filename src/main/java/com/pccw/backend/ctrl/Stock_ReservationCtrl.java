package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.interfaceForOrdering.InputBean;
import com.pccw.backend.bean.interfaceForOrdering.InputItemBean;
import com.pccw.backend.bean.stock_reservation.CreateBean;
import com.pccw.backend.bean.stock_reservation.EditBean;
import com.pccw.backend.bean.stock_reservation.SearchBean;
import com.pccw.backend.cusinterface.IOuterApi;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/stock_reservation")
@Api(value="Stock_ReservationCtrl",tags={"stock_reservation"})
public class Stock_ReservationCtrl extends BaseStockCtrl<DbResReservation> implements IOuterApi {

    @Autowired
    ResLogMgtRepository logMgtRepository;
    @Autowired
    ResSkuRepoRepository skuRepoRepository;
    @Autowired
    ResReservationRepository reservationRepository;
    @Autowired
    ResReservationRuleRepository ruleRepository;
    @Autowired
    ResSkuRepository skuRepository;
    @Autowired
    ResRepoRepository repoRepository;

    @ApiOperation(value="预留",tags={"stock_reservation"},notes="查询")
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public JsonResult search(@RequestBody SearchBean bean) {
        try {
            System.out.println(bean.toString());
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.DESC,"selected"));
            orders.add(new Sort.Order(Sort.Direction.ASC,"orderDate"));
            Specification<DbResReservation> spec = Convertor.<DbResReservation>convertSpecification(bean);
            List<DbResReservation> list = reservationRepository.findAll(spec, PageRequest.of(bean.getPageIndex(),bean.getPageSize(),Sort.by(orders))).getContent();
            //sortByRule
            if(bean.getSortByRule()!=null&&bean.getSortByRule().equals("Y")){
                List<DbResReservation> sortList = new ArrayList<>();
                for (int i=list.size()-1;i>=0;i--) {
                    DbResReservation reserve = list.get(i);
                    List<DbResReservationRule> rules = ruleRepository.getDbResReservationRulesBySkuIdAndPaymentStatusAndCustomerType(reserve.getSkuId(), reserve.getPaymentStatus(), reserve.getCustomerType());
                    if(rules!=null && rules.size()>0){
                        list.remove(reserve);
                        reserve.setUpdateAccountName(rules.get(0).getPriority()+"");
                        sortList.add(reserve);
                    }
                }
                sortList = sortList.stream().sorted(Comparator.comparing(DbResReservation::getUpdateAccountName)).collect(Collectors.toList());
                for (DbResReservation dbResReservation : sortList) {
                    dbResReservation.setUpdateAccountName(null);
                }
                list.addAll(0,sortList);
            }
            //search condition
//            if(bean.getCustomerType()!=null&&!bean.getCustomerType().equals("")){
//                list = list.stream().filter(b -> bean.getCustomerType().equals(b.getCustomerType())).collect(Collectors.toList());
//            }
//            if(bean.getPaymentStatus()!=null&&!bean.getPaymentStatus().equals("")){
//                list = list.stream().filter(b -> b.getPaymentStatus().equals(bean.getPaymentStatus())).collect(Collectors.toList());
//            }
//            if(bean.getRepoId()!=null){
//                list = list.stream().filter(b -> b.getRepoId().toString().equals(bean.getRepoId()+"")).collect(Collectors.toList());
//            }
//            if (bean.getSku() != null && bean.getSku().size() > 0 ) {
//                list = list.stream().filter(map ->{
//                    for (Long s : bean.getSku()) {
//                        if(map.getSkuId() != null && s==map.getSkuId()){
//                            return true;
//                        }
//                    }
//                    return false;
//                }).collect(Collectors.toList());
//            }
            return JsonResult.success(list,reservationRepository.count(spec));
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="预留",tags={"stock_reservation"},notes="新增")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    @Transactional(rollbackOn = Exception.class)
    public JsonResult create(@RequestBody CreateBean bean) {
        try {
            //保存 reservation
            if(bean.getSelected()==null) bean.setSelected("false");
            bean.setLogTxtBum(genTranNum(new Date(),"RV",repoRepository.findById(bean.getRepoId()).get().getRepoCode()));
            this.create(reservationRepository,DbResReservation.class,bean);
            DbResSku sku = new DbResSku();sku.setId(bean.getSkuId());
            DbResRepo repo = new DbResRepo();repo.setId(bean.getRepoId());
            DbResStockType stockType = new DbResStockType();stockType.setId(3L);
            DbResSkuRepo skuRepo = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
            long time = System.currentTimeMillis();
            //扣减available qty加到reservered qty
            if(skuRepo != null){
                DbResStockType stockType2 = new DbResStockType();stockType2.setId(4L);
                DbResSkuRepo skuRepo2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType2);
                //存在reserved，修改qty
                if(skuRepo2 != null){
                    skuRepo2.setUpdateBy(getAccount());
                    skuRepo2.setUpdateAt(time);
                    skuRepo2.setQty(skuRepo2.getQty()+bean.getQty());
                    skuRepoRepository.saveAndFlush(skuRepo2);
                } else {
                    //不存在reserved，新增一条
                    DbResSkuRepo value = new DbResSkuRepo();
                    value.setRepo(repo);
                    value.setSku(sku);
                    value.setStockType(stockType2);
                    value.setQty(bean.getQty());
                    value.setRemark(bean.getRemark());
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
            logMgt.setLogRepoOut(bean.getRepoId());
            logMgt.setLogTxtBum(bean.getLogTxtBum());
            logMgt.setLogType(StaticVariable.LOGTYPE_MANAGEMENT);
            logMgt.setLogOrderNature(StaticVariable.LOGORDERNATURE_STOCK_RESERVE);
            logMgt.setStatus(StaticVariable.STATUS_WAITING);
            logMgt.setStaffNumber(bean.getStaffId());
            //logMgt.setReservationId(reservationRepository.findAll().stream().max(Comparator.comparing(DbResReservation::getId)).get().getId());
            logMgt.setRemark(bean.getRemark());
            logMgt.setCreateAt(time);
            logMgt.setUpdateAt(time);
            logMgt.setCreateBy(getAccount());
            logMgt.setUpdateBy(getAccount());
            logMgt.setActive("Y");

            List<DbResLogMgtDtl> line = new LinkedList<>();
            DbResLogMgtDtl dtl = new DbResLogMgtDtl();
            dtl.setDtlRepoId(bean.getRepoId());
            dtl.setDtlSkuId(bean.getSkuId());
            dtl.setDtlQty(bean.getQty());
            dtl.setLogTxtBum(bean.getLogTxtBum());
            dtl.setStatus(StaticVariable.STATUS_RESERVED);
//            dtl.setLisStatus(StaticVariable.LISSTATUS_WAITING);
            dtl.setDtlAction(StaticVariable.DTLACTION_ADD);
            dtl.setDtlSubin(StaticVariable.DTLSUBIN_RESERVED);
            dtl.setCreateAt(time);
            dtl.setUpdateAt(time);
            dtl.setCreateBy(getAccount());
            dtl.setUpdateBy(getAccount());
            dtl.setActive("Y");

            DbResLogMgtDtl dtl2 = new DbResLogMgtDtl();
            dtl2.setDtlRepoId(bean.getRepoId());
            dtl2.setDtlSkuId(bean.getSkuId());
            dtl2.setDtlQty(bean.getQty());
            dtl2.setLogTxtBum(bean.getLogTxtBum());
            dtl2.setStatus(StaticVariable.STATUS_AVAILABLE);
//            dtl2.setLisStatus(StaticVariable.LISSTATUS_WAITING);
            dtl2.setDtlAction(StaticVariable.DTLACTION_DEDUCT);
            dtl2.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);
            dtl2.setCreateAt(time);
            dtl2.setUpdateAt(time);
            dtl2.setCreateBy(getAccount());
            dtl2.setUpdateBy(getAccount());
            dtl2.setActive("Y");

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

    @ApiOperation(value="删除",tags={"stock_reservation"},notes="")
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    @Transactional(rollbackOn = Exception.class)
    public JsonResult delete(@RequestBody EditBean bean) {
        try {
            System.out.println(bean);
            long time = System.currentTimeMillis();
            DbResReservation dbResReservation = reservationRepository.findById(bean.getId()).get();
            dbResReservation.setUpdateAt(time);
            dbResReservation.setUpdateBy(getAccount());
            dbResReservation.setActive("N");
            reservationRepository.saveAndFlush(dbResReservation);

            DbResSku sku = new DbResSku();sku.setId(bean.getSkuId());
            DbResRepo repo = new DbResRepo();repo.setId(bean.getRepoId());
            DbResStockType stockType = new DbResStockType();stockType.setId(3L);
            DbResSkuRepo skuRepo = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
            //删除时，扣减之前的reserved qty 加到available qty
            if(skuRepo != null){
                skuRepo.setQty(skuRepo.getQty()+dbResReservation.getQty());
                skuRepo.setUpdateAt(time);
                skuRepo.setUpdateBy(getAccount());
                skuRepoRepository.saveAndFlush(skuRepo);
            }
            DbResStockType stockType2 = new DbResStockType();stockType2.setId(4L);
            DbResSkuRepo skuRepo2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType2);
            //扣减之前reserved的qty
            if(skuRepo2 != null){
                skuRepo2.setQty(skuRepo2.getQty()-dbResReservation.getQty());
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

    @ApiOperation(value="修改",tags={"stock_reservation"},notes="")
    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    @Transactional(rollbackOn = Exception.class)
    public JsonResult edit(@RequestBody EditBean bean) {
        try {
            System.out.println(bean.toString());
            DbResReservation dbResReservation = reservationRepository.findById(bean.getId()).get();
            long time = System.currentTimeMillis();
            //没有修改repo和sku，即扣减之前reserved的qty，添加新的reserved qty
            if(bean.getRepoId() == dbResReservation.getRepoId() &&
                bean.getSkuId() == dbResReservation.getSkuId()){
                DbResSku sku = new DbResSku();sku.setId(bean.getSkuId());
                DbResRepo repo = new DbResRepo();repo.setId(bean.getRepoId());
                DbResStockType stockType = new DbResStockType();stockType.setId(3L);

                DbResSkuRepo skuRepo = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType);
                //avalible的加上之前reserved的qty，扣减新的reserved qty
                if(skuRepo!=null){
                    skuRepo.setQty(skuRepo.getQty()+dbResReservation.getQty()-bean.getQty());
                    skuRepo.setUpdateAt(time);
                    skuRepo.setUpdateBy(getAccount());
                    skuRepoRepository.saveAndFlush(skuRepo);
                }
                //reserved的减去之前的qty加上现在的qty
                DbResStockType stockType2 = new DbResStockType();stockType2.setId(4L);
                DbResSkuRepo skuRepo2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo, sku, stockType2);
                if(skuRepo2 != null){
                    skuRepo2.setUpdateAt(time);
                    skuRepo2.setUpdateBy(getAccount());
                    skuRepo2.setQty(skuRepo2.getQty()-dbResReservation.getQty()+bean.getQty());
                    skuRepoRepository.saveAndFlush(skuRepo2);
                }
            } else {
            //修改了repo或者sku
                DbResSku sku2 = new DbResSku();sku2.setId(dbResReservation.getSkuId());
                DbResRepo repo2 = new DbResRepo();repo2.setId(dbResReservation.getRepoId());
                DbResStockType stockType2 = new DbResStockType();stockType2.setId(3L);
                DbResSkuRepo value = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku2, stockType2);
                //找到之前的available加上之前的resereved qty
                if(value != null) {
                    value.setQty(value.getQty()+dbResReservation.getQty());
                    value.setUpdateAt(time);
                    value.setUpdateBy(getAccount());
                    skuRepoRepository.saveAndFlush(value);

                    DbResSku sku3 = new DbResSku();sku3.setId(bean.getSkuId());
                    DbResRepo repo3 = new DbResRepo();repo3.setId(bean.getRepoId());
                    DbResSkuRepo value2 = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo3, sku3, stockType2);
                    //找到现在的available，减去现在的qty
                    if(value2!=null){
                        value2.setQty(value2.getQty()-bean.getQty());
                        value2.setUpdateAt(time);
                        value2.setUpdateBy(getAccount());
                        skuRepoRepository.saveAndFlush(value2);

                        DbResStockType stockType3 = new DbResStockType();stockType3.setId(4L);
                        //找到之前的reserved减去之前qty
                        DbResSkuRepo resereved = skuRepoRepository.findDbResSkuRepoByRepoAndSkuAndStockType(repo2, sku2, stockType3);
                        if(resereved!=null){
                            resereved.setUpdateAt(time);
                            resereved.setUpdateBy(getAccount());
                            resereved.setQty(resereved.getQty()-dbResReservation.getQty());
                            skuRepoRepository.saveAndFlush(resereved);

                            //找到现在的reserved加上现在的qty
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
                                newReserved.setRemark(bean.getRemark());
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
            this.edit(reservationRepository, DbResReservation.class, bean);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
        return JsonResult.success(Arrays.asList());
    }

    @Override
    public String outerApi(Object o, String logTxtNum) {
        try {
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            long time = System.currentTimeMillis();
            if(o instanceof InputBean) {
                InputBean bean = (InputBean) o;
                for (InputItemBean item : bean.getItem_details()) {
                    DbResReservation res = new DbResReservation();
                    res.setStaffId(bean.getSales_id());
                    res.setOrderNo(bean.getOrder_id());
                    res.setCustomerName(bean.getOrder_system());
                    res.setRemark(bean.getRemarks());
                    DbResSku sku = skuRepository.findFirst1BySkuCode(item.getSku_code());
                    res.setSkuId(sku.getId());
                    DbResRepo repo = repoRepository.findFirst1ByRepoCode(item.getRepo_id());
                    long qty = Long.parseLong(item.getQuantity());
                    res.setRepoId(repo.getId());
                    res.setLogTxtBum(logTxtNum);
                    res.setQty(qty);
                    res.setOrderDate(sdf.parse(bean.getBiz_date()).getTime());
                    res.setReservationDate(sdf.parse(bean.getTx_date()).getTime());
                    res.setDays(Long.parseLong(sku.getMaxReserveDays()));
                    res.setActive("Y");
                    res.setUpdateAt(sdf.parse(bean.getBiz_date()).getTime());
                    res.setCreateAt(sdf.parse(bean.getBiz_date()).getTime());
                    res.setCreateBy(getAccount());
                    res.setUpdateBy(getAccount());


                    DbResSkuRepo avaliable = skuRepoRepository.findQtyByRepoAndShopAndType(repo.getId(), sku.getId(), 3L);
                    if(avaliable == null || avaliable.getQty()<qty){
                        return "The SKU:"+item.getSku_code()+" in the channel:"+item.getRepo_id()+" is not enough,Available qty is "+(avaliable==null?0:avaliable.getQty());
                    }
                    reservationRepository.saveAndFlush(res);
                    avaliable.setQty(avaliable.getQty()-Long.parseLong(item.getQuantity()));
                    avaliable.setUpdateAt(time);
                    avaliable.setUpdateBy(getAccount());
                    skuRepoRepository.saveAndFlush(avaliable);
                    DbResSkuRepo reserve = skuRepoRepository.findQtyByRepoAndShopAndType(repo.getId(), sku.getId(), 4L);
                    if(reserve!=null){
                        reserve.setQty(reserve.getQty()+qty);
                        reserve.setUpdateAt(time);
                        reserve.setUpdateBy(getAccount());
                        skuRepoRepository.saveAndFlush(reserve);
                    } else {
                        DbResSkuRepo value = new DbResSkuRepo();
                        value.setRepo(repo);
                        value.setSku(sku);
                        DbResStockType type = new DbResStockType();type.setId(4L);
                        value.setStockType(type);
                        value.setQty(qty);
                        value.setRemark(bean.getRemarks());
                        value.setCreateAt(time);
                        value.setUpdateAt(time);
                        value.setCreateBy(getAccount());
                        value.setUpdateBy(getAccount());
                        value.setActive("Y");
                        skuRepoRepository.saveAndFlush(value);
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return "";
    }
}
