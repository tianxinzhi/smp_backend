package com.pccw.backend.ctrl;


import com.alibaba.fastjson.JSONObject;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.stock_movement.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * AuthRightCtrl
 */

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST, origins = "*", allowCredentials = "false")
@RequestMapping("/stock_movement")
@Api(value="Stock_MovementCtrl",tags={"stock_movement"})
public class Stock_MovementCtrl extends BaseCtrl<DbResLogMgt> {

    @Autowired
    ResProcessRepository processRepo;
    @Autowired
    ResRepoRepository repoRepo;
    @Autowired
    ResLogReplRepository logReplRepo;
    @Autowired
    ResLogMgtRepository logMgtRepo;
    @Autowired
    ResLogMgtDtlSerialRepository serialRepo;
    @Autowired
    ResSkuRepository skuRepo;

    @Autowired
    private EntityManager entityManager;


    @ApiOperation(value = "搜索Stock_Movement", tags = {"stock_movement"}, notes = "注意问题点")
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public JsonResult getHistoryInfo(@RequestBody SearchBean b) {
        try {
            String skuSearch="";
            if (b.getSkuNum() != null && b.getSkuNum().size()>0) {
                int i =0;
                skuSearch += "where sku.id in(";
                for (String s : b.getSkuNum()) {
                    if(s.equals("0"))continue;
                    skuSearch += "'"+s+"',";
                    i++;
                }
                skuSearch = skuSearch.substring(0,skuSearch.length()-1);
                skuSearch += ")";
                if(i<1&&!skuSearch.contains(",")){
                    skuSearch = "";
                }
            }
            StringBuffer baseSql = new StringBuffer(" select t1.id \"mgtId\", t4.id \"id\",t1.CREATE_AT \"createAt\",a1.account_name \"createAccountName\", \n" +
                    "t1.log_txt_num \"logTxtBum\",t1.log_order_nature \"logOrderNature\",t1.remark \"reason\", \n" +
                    "t1.approval \"approval\",t1.approval_by \"approvalBy\",t1.staff_number \"staff\",t1.remark \"remark\" ,\n" +
                    "t1.courier \"courier\",t1.serial \"serial\",t1.iccId \"iccID\",t1.imei \"imei\",t1.mobile_number \"mobileNumber\", \n" +
                    "t1.source_system \"sourceSystem\",t1.source_txn_header \"txnHeader\",t1.source_line \"txnLine\",t1.publish_to_lis \"pubToLis\",t1.receive_from_lis \"recFromLis\", \n" +
                    "r1.id \"repoId\" ,r1.repo_code \"fromChannel\",r2.id \"toRepoId\" ,r2.repo_code \"toChannel\",\n" +
                    " t4.skuId \"skuId\",t4.sku \"sku\",t4.skuDesc \"skuDesc\",t4.uom \"uom\",t4.qty \"qty\",t4.fromStatus \"fromStatus\"" +
                    " ,t4.id \"dtlId\" from res_log_mgt t1 left JOIN RES_ACCOUNT a1 on a1.id = t1.create_by\n" +
                    "left join res_repo r1 on r1.id = t1.log_repo_out\n" +
                    "left join res_repo r2 on r2.id = t1.log_repo_in right join\n" +
                    "(select dtl.id, \n" +
                    "dtl.log_mgt_id , sku.id skuId,sku.sku_name sku,sku.sku_desc skuDesc,sku.uom uom,dtl.DTL_QTY qty,dtl.DTL_SUBIN fromStatus\n" +
                    "\tfrom RES_LOG_MGT_DTL dtl \n" +
                    " inner join res_sku sku on dtl.dtl_sku_id = sku.id "+skuSearch+" ) t4 on t1.id = t4.log_mgt_id where 1=1");

            if(!StringUtils.isEmpty(b.getLogOrderNature())) {
                baseSql.append(" and t1.log_order_nature="+"'"+b.getLogOrderNature()+"'");
            }
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (!StringUtils.isEmpty(b.getCreateAt())&&b.getCreateAt().length>0) {
                baseSql.append("and t1.CREATE_AT between "+sdf.parse(b.getCreateAt()[0]).getTime()+" and "+sdf.parse(b.getCreateAt()[1]).getTime());
            }
            if(b.getRepoId()!=null&&b.getRepoId()!=0){
                baseSql.append(" and r1.id="+b.getRepoId());
            }
            if(b.getToRepoId()!=null&&b.getToRepoId()!=0){
                baseSql.append(" and r2.id="+b.getToRepoId());
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
            List<Map> content = dataQuery.getResultList();
            for (Map map : content) {
                if(map.get("logOrderNature") !=null && (map.get("logOrderNature").toString().equals(StaticVariable.LOGORDERNATURE_STOCK_CATEGORY)
                 || map.get("logOrderNature").toString().equals(StaticVariable.LOGORDERNATURE_STOCK_OUT_STS)
                 || map.get("logOrderNature").toString().equals(StaticVariable.LOGORDERNATURE_STOCK_OUT_STS)
                 || map.get("logOrderNature").toString().equals(StaticVariable.LOGORDERNATURE_REPLENISHMENT_REQUEST)
                 || map.get("logOrderNature").toString().equals(StaticVariable.LOGORDERNATURE_STOCK_RESERVE)
                 || map.get("logOrderNature").toString().equals(StaticVariable.LOGORDERNATURE_RETURN)
                ) ) {
                    List<DbResLogMgtDtl> ids = logMgtRepo.findById(Long.parseLong(map.get("mgtId").toString())).get().getLine();
                    if(ids!=null&&ids.size()>1)
                    map.put("toStatus",ids.get(1).getDtlSubin());
                }
                if(map.get("id") !=null){
                    List<DbResLogMgtDtlSerial> dtlId = serialRepo.findAllByDtlId(Long.parseLong(map.get("id").toString()));
                    List<Map> line = new LinkedList<>();
                    for (DbResLogMgtDtlSerial serial : dtlId) {
                        Map linemap = new HashMap();
                        linemap.put("courier",serial.getCourier());
                        linemap.put("serial",serial.getSerial());
                        linemap.put("expiryDate",serial.getExpiryDate());

                        line.add(linemap);
                    }
                    map.put("line",line);
                }
            }
            return JsonResult.success(content,count.longValue());
        } catch (NumberFormatException | ParseException e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value = "搜索Stock_Movement", tags = {"stock_movement"}, notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST, path = "/search2")
    public JsonResult search(@RequestBody SearchBean b) {
        log.info(b.toString());
        try {
            List<Map> list = new ArrayList<>();
            for (DbResLogMgt dbResLogMgt : logMgtRepo.findAll()) {
                Map map = new HashMap();

                map.put("createAccountName", getAccountName(dbResLogMgt.getCreateBy()));
                map.put("createAt", dbResLogMgt.getCreateAt());
                map.put("id", dbResLogMgt.getId());
                map.put("logTxtBum", dbResLogMgt.getLogTxtBum());
                map.put("logOrderNature", dbResLogMgt.getLogOrderNature());
                map.put("reason", dbResLogMgt.getRemark());
                map.put("approval", dbResLogMgt.getApproval());
                map.put("approvalBy", dbResLogMgt.getApprovalBy());

                map.put("staff", dbResLogMgt.getStaffNumber());
                map.put("remark", dbResLogMgt.getRemark());
                map.put("courier", dbResLogMgt.getCourier());
                map.put("serial", dbResLogMgt.getSerial());
                map.put("iccID", dbResLogMgt.getIccID());
                map.put("imei", dbResLogMgt.getImei());
                map.put("mobileNumber", dbResLogMgt.getMobileNumber());
                map.put("sourceSystem", dbResLogMgt.getSourceSystem());
                map.put("txnHeader", dbResLogMgt.getSourceTxnHeader());
                map.put("txnLine", dbResLogMgt.getSourceTxnLine());

                if ( Objects.nonNull(dbResLogMgt.getLogRepoOut()) && dbResLogMgt.getLogRepoOut() != 0 ) {
                    DbResRepo fromName2 = repoRepo.findById(dbResLogMgt.getLogRepoOut()).get();
                    map.put("fromChannel", fromName2.getRepoCode());
                    map.put("repoId", fromName2.getId());
                }
                if ( Objects.nonNull(dbResLogMgt.getLogRepoIn()) && dbResLogMgt.getLogRepoIn() != 0 ) {
                    DbResRepo toName2 = repoRepo.findById(dbResLogMgt.getLogRepoIn()).get();
                    map.put("toChannel", toName2.getRepoCode());
                    map.put("toRepoId", toName2.getId());
                }
                List<DbResLogMgtDtl> line = dbResLogMgt.getLine();

                List itemList = new ArrayList<>();
                for (int i = 0; i < line.size(); i++) {
                    String skuQtyString = "";
                    DbResSku sku = skuRepo.findById(line.get(i).getDtlSkuId()).get();
                    map.put("sku", sku.getSkuName());
                    map.put("skuId", sku.getId());
                    map.put("skuDesc", sku.getSkuDesc());
                    map.put("qty", line.get(i).getDtlQty());
                    map.put("fromStatus", line.get(i).getDtlSubin());

                    String itemCodes = line.get(i).getItemCode();

                   if ( Objects.nonNull(itemCodes) ) {
                        List<String> items = Arrays.asList(itemCodes.split(","));
                        for (int j = 0; j < items.size(); j = j + 1) {
                            itemList.add(skuQtyString + " , itemCode" + (j + 1) + ": " + items.get(j));
                        }
                    }
                    if ( StaticVariable.LOGORDERNATURE_STOCK_CATEGORY.equals(dbResLogMgt.getLogOrderNature()) ) {
                        map.put("toStatus", line.get(i + 1).getDtlSubin());
                        i++;
                    }
                }
                if ( Objects.nonNull(itemList) && itemList.size() > 0 ) {
                    map.put("itemCode", itemList);
                }
                list.add(map);
            }

            long[] count = {logMgtRepo.count()};
            //搜索filter
            if ( b.getCreateAt() != null ) {
                list = list.stream().filter(map -> {
                    long dte = Long.parseLong(map.get("createAt").toString());
                    if(dte >= Long.parseLong(b.getCreateAt()[0]) && dte <= Long.parseLong(b.getCreateAt()[1]))
                        return true;
                    else {
                        count[0] -= 1;
                        return false;
                    }
                }).collect(Collectors.toList());
            }

            if ( b.getLogOrderNature() != null && !b.getLogOrderNature().equals("") ) {
                list = list.stream().filter(map -> {
                    if(map.get("logOrderNature") != null && map.get("logOrderNature").toString().equals(b.getLogOrderNature())){
                        return true;
                    } else {
                        count[0] --;
                        return false;
                    }
                }).collect(Collectors.toList());
            }
            if ( b.getSkuNum() != null && b.getSkuNum().size() > 0 ) {
                list = list.stream().filter(map ->{
                    for (String s : b.getSkuNum()) {
                        if(map.get("skuId") != null && s.equals(map.get("skuId").toString())){
                            return true;
                        }
                    }
                    count[0] --;
                    return false;
                }).collect(Collectors.toList());
            }
            if ( b.getRepoId() != null && b.getRepoId() != 0 ) {
                list = list.stream().filter(map -> {
                    if(map.get("repoId") != null && Long.parseLong(map.get("repoId").toString()) == b.getRepoId()){
                        return true;
                    } else {
                        count[0] --;
                        return false;
                    }
                }).collect(Collectors.toList());
            }
            if ( b.getToRepoId() != 0 ) {
                list = list.stream().filter(map -> {
                    if(map.get("toRepoId") != null && Long.parseLong(map.get("toRepoId").toString()) == b.getToRepoId()){
                        return true;
                    } else {
                        count[0] --;
                        return false;
                    }
                } ).collect(Collectors.toList());
            }
            Collections.sort(list, (o1, o2) -> {
                if ( o1.get("sku") != null && o2.get("sku") != null )
                    return o2.get("sku").toString().toUpperCase().compareTo(o1.get("sku").toString().toUpperCase());
                        return 0;
                    }
            );
//            (0,10) (1,10)
 //           b.getPageIndex()*b.getPageSize(),(b.getPageIndex()+1)*b.getPageSize();
            int begin = b.getPageIndex()*b.getPageSize();
            int end = (b.getPageIndex()+1)*b.getPageSize()>list.size()? list.size() : (b.getPageIndex()+1)*b.getPageSize();
            for (int i=list.size()-1;i>=0;i--) {
                Map map = list.get(i);
                if(list.indexOf(map)<begin||
                    list.indexOf(map)>=end){
                    list.remove(map);
                }
            }
            return JsonResult.success(list,count[0]);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

}
