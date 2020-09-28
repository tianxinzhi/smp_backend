package com.pccw.backend.ctrl;


import com.pccw.backend.annotation.NoAuthorized;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.interfaceForOrdering.InputBean;
import com.pccw.backend.bean.interfaceForOrdering.InputItemBean;
import com.pccw.backend.bean.interfaceForOrdering.OutputItemBean;
import com.pccw.backend.bean.interfaceForOrdering.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.CollectionBuilder;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/api/external/ppos/v1")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@NoAuthorized
public class InterfaceForOrderingCtrl extends BaseStockCtrl<DbResLogMgt> {

    @Autowired
    private ResLogMgtRepository repo;

    @Autowired
    ResSkuRepoRepository skuRepoRepository;

    @Autowired
    ResRepoRepository repoRepository;

    @Autowired
    ResSkuRepository skuRepository;

    @Autowired
    ResItemRepository itemRepository;

    @ApiOperation(value="创建stock_update",tags={"stock_update"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/stock_update")
    public Map create(@RequestBody InputBean b){
        try {
            //输入验证
            String s1 = validateParam(b.getItem_details());
            if(!s1.equals("")) return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "500").put("msg", s1).put("data", null).build();
            long t = new Date().getTime();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            DbResLogMgt mgt= new DbResLogMgt();
            mgt.setSourceSystem(b.getOrder_system());mgt.setLogOrderId(b.getOrder_id());
            mgt.setStaffNumber(b.getSales_id());mgt.setLogOrderNature(b.getRequest_nature());
            mgt.setRemark(b.getRemarks());mgt.setLogOrderType("N");
            mgt.setTxDate(b.getTx_date());
            mgt.setLogType(StaticVariable.LOGTYPE_ORDER);mgt.setActive("Y");
            mgt.setUpdateAt(sdf.parse(b.getBiz_date()).getTime());mgt.setCreateAt(sdf.parse(b.getBiz_date()).getTime());
            mgt.setCreateBy(getAccount());mgt.setUpdateBy(getAccount());
            List<DbResLogMgtDtl> logMgtDtls=new ArrayList<>();
            List<Object> list = new ArrayList<>();
            for (InputItemBean item : b.getItem_details()) {
                //Output数据
                OutputItemBean outputItem= new OutputItemBean(item.getDetail_id(),item.getSku_code(),String.valueOf(item.getQuantity()),
                        item.getItem_code(), item.getRepo_id(),item.getCcc(),item.getWo());
                list.add(outputItem);

                DbResSku s = skuRepository.findFirst1BySkuCode(item.getSku_code());
                DbResRepo r = repoRepository.findFirst1ByRepoCode(item.getRepo_id());
//                DbResItem i = itemRepository.findFirst1ByItemCode(item.getItem_code());
                Long skuId = Objects.isNull(s) ? null : s.getId();
                Long repoId = Objects.isNull(r) ? null : r.getId();
//                Long itemId = Objects.isNull(i) ? null : i.getId();

                DbResLogMgtDtl mgtDtl=new DbResLogMgtDtl();
                mgtDtl.setDtlSkuId(skuId);
//                mgtDtl.setDtlItemId(itemId);
                mgtDtl.setDtlRepoId(repoId);mgtDtl.setDtlQty(Long.parseLong(item.getQuantity()));
                mgtDtl.setItemCode(item.getItem_code());mgtDtl.setDetailId(item.getDetail_id());
                mgtDtl.setResLogMgt(mgt);
                mgtDtl.setActive("Y");mgtDtl.setUpdateAt(t);mgtDtl.setCreateAt(t);
                mgtDtl.setCreateBy(getAccount());mgtDtl.setUpdateBy(getAccount());
                String dnNum = "";
                if(b.getRequest_nature().equals("ASG")){
                    mgtDtl.setDtlAction("D");
                    mgtDtl.setStatus("AVL");
                    mgtDtl.setDtlSubin("Good");
                    dnNum = "S";
                }else if(b.getRequest_nature().equals("RET")){
                    mgtDtl.setDtlAction("A");
                    mgtDtl.setStatus("FAU");
                    mgtDtl.setDtlSubin("Faulty");
                    dnNum = "E";
                }else if(b.getRequest_nature().equals("EXC")){
                    mgtDtl.setDtlAction("A");
                    mgtDtl.setStatus("FAU");
                    mgtDtl.setDtlSubin("Faulty");
                    dnNum = "X";
                    DbResLogMgtDtl secRorDtl=new DbResLogMgtDtl();
                    BeanUtils.copyProperties(mgtDtl,secRorDtl);
                    secRorDtl.setDtlAction("D");
                    secRorDtl.setStatus("AVL");
                    secRorDtl.setDtlSubin("Good");
                    String txtNum = new SimpleDateFormat("yyMMdd hhmmss").format(new Date()).replace(" ", dnNum+mgtDtl.getDtlRepoId());
                    secRorDtl.setLogTxtBum(txtNum);
                    logMgtDtls.add(secRorDtl);
                } else if(b.getRequest_nature().equals("RES")){
                    dnNum = "RV";
                    String txtNum = new SimpleDateFormat("yyMMdd hhmmss").format(new Date()).replace(" ", dnNum+mgtDtl.getDtlRepoId());

                    mgtDtl.setLogTxtBum(txtNum);
                    mgtDtl.setStatus(StaticVariable.STATUS_RESERVED);
                    mgtDtl.setDtlAction(StaticVariable.DTLACTION_ADD);
                    mgtDtl.setDtlSubin(StaticVariable.DTLSUBIN_RESERVED);

                    DbResLogMgtDtl mgtDtl2 = new DbResLogMgtDtl();
                    BeanUtils.copyProperties(mgtDtl,mgtDtl2);
                    mgtDtl2.setStatus(StaticVariable.STATUS_AVAILABLE);
                    mgtDtl2.setDtlAction(StaticVariable.DTLACTION_DEDUCT);
                    mgtDtl2.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);

                    logMgtDtls.add(mgtDtl2);
                    String s2 = Stock_ReservationCtrl.class.newInstance().outerApi(b, txtNum);
                    if(!s2.equals("")) {
                        return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "500").put("msg", s2).put("data", null).build();
                    }
                }
                String txtNum = new SimpleDateFormat("yyMMdd hhmmss").format(new Date()).replace(" ", dnNum+mgtDtl.getDtlRepoId());
                mgtDtl.setLogTxtBum(txtNum);
                mgt.setLogTxtBum(txtNum);
                logMgtDtls.add(mgtDtl);
            }
            mgt.setLine(logMgtDtls);
            repo.saveAndFlush(mgt);

            Map outputdata = CollectionBuilder.builder(new HashMap<>()).put("item_details",list).put("tx_id",mgt.getLogTxtBum()).build();
            Map jsonResult = CollectionBuilder.builder(new HashMap<>()).put("state", "success").put("code", "200").put("msg", "stock update successfully").put("data", outputdata).build();
            return jsonResult;
        } catch (Exception e) {
            e.printStackTrace();
            return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "500").put("msg", e.getMessage()).put("data", null).build();
        }
    }


    @ApiOperation(value="创建stock_update",tags={"stock_update"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/stock_update_ao")
    public Map createAO(@RequestBody InputBean b){
        try {
            //输入验证
            String s1 = validateParam(b.getItem_details());
            if(!s1.equals("")) return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "500").put("msg", s1).put("data", null).build();
            long t = new Date().getTime();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            DbResLogMgt mgt= new DbResLogMgt();
            mgt.setSourceSystem(b.getOrder_system());mgt.setLogOrderId(b.getOrder_id());
            mgt.setStaffNumber(b.getSales_id());mgt.setLogOrderNature(b.getRequest_nature());
            mgt.setRemark(b.getRemarks());mgt.setLogOrderType("N");
            mgt.setTxDate(b.getTx_date());
            mgt.setLogType(StaticVariable.LOGTYPE_ORDER);mgt.setActive("Y");
            mgt.setUpdateAt(sdf.parse(b.getBiz_date()).getTime());mgt.setCreateAt(sdf.parse(b.getBiz_date()).getTime());
            mgt.setCreateBy(getAccount());mgt.setUpdateBy(getAccount());
            List<DbResLogMgtDtl> logMgtDtls=new ArrayList<>();
            List<Object> list = new ArrayList<>();
            for(InputItemBean item:b.getItem_details()){
                //Output数据
                OutputItemBean outputItem= new OutputItemBean(item.getDetail_id(),item.getSku_code(),String.valueOf(item.getQuantity()),
                        item.getItem_code(), item.getRepo_id(),item.getCcc(),item.getWo());
                list.add(outputItem);

                DbResSku s = skuRepository.findFirst1BySkuCode(item.getSku_code());
                DbResRepo r = repoRepository.findFirst1ByRepoCode(item.getRepo_id());
//                DbResItem i = itemRepository.findFirst1ByItemCode(item.getItem_code());
                Long skuId = Objects.isNull(s) ? null : s.getId();Long repoId = Objects.isNull(r) ? null : r.getId();
//                Long itemId = Objects.isNull(i) ? null : r.getId();
                DbResLogMgtDtl mgtDtl=new DbResLogMgtDtl();
                mgtDtl.setDtlSkuId(skuId);
//                mgtDtl.setDtlItemId(itemId);
                mgtDtl.setDtlRepoId(repoId);mgtDtl.setDtlQty(Long.parseLong(item.getQuantity()));
                mgtDtl.setItemCode(item.getItem_code());mgtDtl.setDetailId(item.getDetail_id());
                mgtDtl.setResLogMgt(mgt);
                mgtDtl.setActive("Y");mgtDtl.setUpdateAt(t);mgtDtl.setCreateAt(t);
                mgtDtl.setCreateBy(getAccount());mgtDtl.setUpdateBy(getAccount());
                String dnNum = "";
                if(b.getRequest_nature().equals("ARS")){
                    dnNum = "T";
                    String txtNum = new SimpleDateFormat("yyMMdd hhmmss").format(new Date()).replace(" ", dnNum+mgtDtl.getDtlRepoId());
                    mgtDtl.setLogTxtBum(txtNum);mgtDtl.setStatus("OST");
                    mgtDtl.setDtlAction(StaticVariable.DTLACTION_ADD);mgtDtl.setDtlSubin(StaticVariable.DTLSUBIN_RESERVED);

                    DbResLogMgtDtl mgtDtl2 = new DbResLogMgtDtl();
                    BeanUtils.copyProperties(mgtDtl,mgtDtl2);
                    mgtDtl2.setDtlAction(StaticVariable.DTLACTION_DEDUCT);
                    mgtDtl2.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);

                    logMgtDtls.add(mgtDtl2);
                    String s2 = Stock_ReservationCtrl.class.newInstance().outerApi(b, txtNum);
                    if(!s2.equals("")) {
                        return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "500").put("msg", s2).put("data", null).build();
                    }
                }else if(b.getRequest_nature().equals("CARS")){
                    mgtDtl.setDtlAction("D");
                    mgtDtl.setStatus("OST");
                    mgtDtl.setDtlSubin("");
                    dnNum = "W";
                }else if(b.getRequest_nature().equals("CARSW")){
                    mgtDtl.setDtlAction("A");
                    mgtDtl.setStatus("AVL");
                    mgtDtl.setDtlSubin("");
                    dnNum = "Y";
                    DbResLogMgtDtl secRorDtl=new DbResLogMgtDtl();
                    BeanUtils.copyProperties(mgtDtl,secRorDtl);
                    secRorDtl.setDtlAction("D");
                    secRorDtl.setStatus("ARE");
                    String txtNum = new SimpleDateFormat("yyMMdd hhmmss").format(new Date()).replace(" ", dnNum+mgtDtl.getDtlRepoId());
                    secRorDtl.setLogTxtBum(txtNum);
                    logMgtDtls.add(secRorDtl);

                }else if(b.getRequest_nature().equals("APU")){
                    mgtDtl.setDtlAction("D");
                    mgtDtl.setStatus("ARE");
                    mgtDtl.setDtlSubin("Good");
                    dnNum = "U";
                }
                String txtNum = new SimpleDateFormat("yyMMdd hhmmss").format(new Date()).replace(" ", dnNum+mgtDtl.getDtlRepoId());
                mgtDtl.setLogTxtBum(txtNum);
                mgt.setLogTxtBum(txtNum);
                logMgtDtls.add(mgtDtl);
            }
            mgt.setLine(logMgtDtls);
            repo.saveAndFlush(mgt);

            Map outputdata = CollectionBuilder.builder(new HashMap<>()).put("tx_id",mgt.getLogTxtBum()).put("item_details",list).build();
            Map jsonResult = CollectionBuilder.builder(new HashMap<>()).put("state", "success").put("code", "200").put("msg", "stock update successfully").put("data", outputdata).build();
            return jsonResult;

        } catch (Exception e) {
            return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "200").put("msg", "stock update failed").put("data", null).build();
        }
    }

    @ApiOperation(value="创建Normal_Reserve",tags={"Normal_Reserve"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/stock_update_nr")
    public Map createNR(@RequestBody InputBean b){
        try {
            List<Object> list = new ArrayList<>();
            String s1 = validateParam(b.getItem_details());
            if(!s1.equals("")) return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "500").put("msg", s1).put("data", null).build();
            long t = new Date().getTime();
            DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            DbResLogMgt mgt= new DbResLogMgt();
            mgt.setSourceSystem(b.getOrder_system());mgt.setLogOrderId(b.getOrder_id());
            mgt.setStaffNumber(b.getSales_id());mgt.setLogOrderNature(b.getRequest_nature());
            mgt.setRemark(b.getRemarks());mgt.setLogOrderType("N");
            mgt.setTxDate(b.getTx_date());
            mgt.setLogType(StaticVariable.LOGTYPE_ORDER);mgt.setActive("Y");
            mgt.setUpdateAt(sdf.parse(b.getBiz_date()).getTime());mgt.setCreateAt(sdf.parse(b.getBiz_date()).getTime());
            mgt.setCreateBy(getAccount());mgt.setUpdateBy(getAccount());
            List<DbResLogMgtDtl> logMgtDtls=new ArrayList<>();
            for(InputItemBean item:b.getItem_details()){
                //Output数据
                OutputItemBean outputItem= new OutputItemBean(item.getDetail_id(),item.getSku_code(),String.valueOf(item.getQuantity()),
                        item.getItem_code(), item.getRepo_id(),item.getCcc(),item.getWo());
                list.add(outputItem);

                DbResSku s = skuRepository.findFirst1BySkuCode(item.getSku_code());
                DbResRepo r = repoRepository.findFirst1ByRepoCode(item.getRepo_id());
//                DbResItem i = itemRepository.findFirst1ByItemCode(item.getItem_code());
                Long skuId = Objects.isNull(s) ? null : s.getId();
                Long repoId = Objects.isNull(r) ? null : r.getId();
//                Long itemId = Objects.isNull(i) ? null : r.getId();

                DbResLogMgtDtl mgtDtl=new DbResLogMgtDtl();
                mgtDtl.setDtlSkuId(skuId);
//                mgtDtl.setDtlItemId(itemId);
                mgtDtl.setDtlRepoId(repoId);mgtDtl.setDtlQty(Long.parseLong(item.getQuantity()));
                mgtDtl.setItemCode(item.getItem_code());mgtDtl.setDetailId(item.getDetail_id());
                mgtDtl.setResLogMgt(mgt);
                mgtDtl.setActive("Y");mgtDtl.setUpdateAt(t);mgtDtl.setCreateAt(t);
                mgtDtl.setCreateBy(getAccount());mgtDtl.setUpdateBy(getAccount());
                String dnNum = "";
                if("NRS".equals(b.getRequest_nature())){
                    mgtDtl.setDtlAction("A");
                    mgtDtl.setStatus("NRE");
                    dnNum = "N";
                }else if("CNRS".equals(b.getRequest_nature())){
                    mgtDtl.setDtlAction("A");
                    mgtDtl.setStatus("AVL");
                    dnNum = "F";
                }else {
                    mgtDtl.setDtlAction("D");
                    mgtDtl.setStatus("NRE");
                    mgtDtl.setLisStatus("Good");
                    dnNum = "K";
                }
                String txtNum = new SimpleDateFormat("yyMMdd hhmmss").format(new Date()).replace(" ", dnNum+mgtDtl.getDtlRepoId());
                mgtDtl.setLogTxtBum(txtNum);
                logMgtDtls.add(mgtDtl);
                if(!"NPU".equals(b.getRequest_nature())){
                    DbResLogMgtDtl mgtDtl1 = new DbResLogMgtDtl();
                    BeanUtils.copyProperties(mgtDtl,mgtDtl1);
                    if("NRS".equals(b.getRequest_nature())) {
                        String s2 = Stock_ReservationCtrl.class.newInstance().outerApi(b, txtNum);
                        if(!s2.equals("")) {
                            return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "500").put("msg", s2).put("data", null).build();
                        }
                        mgtDtl1.setDtlAction("D");
                        mgtDtl1.setStatus("AVL");
                        mgtDtl1.setLogTxtBum(txtNum);
                    }else if("CNRS".equals(b.getRequest_nature())){
                        mgtDtl1.setDtlAction("D");
                        mgtDtl1.setStatus("NRE");
                        mgtDtl1.setLogTxtBum(txtNum);
                    }
                    logMgtDtls.add(mgtDtl1);
                }
                mgt.setLogTxtBum(txtNum);
            }
            mgt.setLine(logMgtDtls);
            repo.saveAndFlush(mgt);

            Map outputdata = CollectionBuilder.builder(new HashMap<>()).put("tx_id",mgt.getLogTxtBum()).put("item_details",list).build();
            Map jsonResult = CollectionBuilder.builder(new HashMap<>()).put("state", "success").put("code", "200").put("msg", "stock update successfully").put("data", outputdata).build();
            return jsonResult;
        } catch (Exception e) {
            return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "200").put("msg", "stock update failed").put("data", null).build();
        }
    }

    @ApiOperation(value="查询Level_Enquiry",tags={"Level_Enquiry"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/stock_level")
    public Map levelEnquiry(@RequestBody SearchBean b){
        try {
            DbResSku s = skuRepository.findFirst1BySkuCode(b.getSku_code());
            DbResRepo r = repoRepository.findFirst1ByRepoCode(b.getRepo_id());
//            DbResItem i = itemRepository.findFirst1ByItemCode(b.getItem_code());
            Long skuId = Objects.isNull(s) ? null : s.getId();
            Long repoId = Objects.isNull(r) ? null : r.getId();
//            Long itemId = Objects.isNull(i) ? null : r.getId();
            Long qty = 0l;
//            if(Objects.isNull(itemId)){
                qty = skuRepoRepository.findQtyByRepoAndSkuAndType(repoId, skuId, 3l);
//            }else {
//                qty = skuRepoRepository.findQtyByRepoAndSkuAndItemAndType(repoId, skuId, itemId, 3l);
//            }
            Map outputdata = CollectionBuilder.builder(new HashMap<>()).put("tx_id","").put("repo_id",b.getRepo_id()).put("quantity",qty).put("sku_code",b.getSku_code()).put("item_code",b.getItem_code()).build();
            Map jsonResult = CollectionBuilder.builder(new HashMap<>()).put("state", "success").put("code", "200").put("msg", "stock level enquiry successfully").put("data", outputdata).build();
            return jsonResult;
        } catch (Exception e) {
            return CollectionBuilder.builder(new HashMap<>()).put("state", "failed").put("code", "200").put("msg", "stock level enquiry failed").put("data", null).build();
        }
    }

    public String validateParam(Object o){
        if(o instanceof List){
            List<InputItemBean> list = (List<InputItemBean>) o;
            for (InputItemBean item : list) {
                if(item.getSku_code()==null||skuRepository.findFirst1BySkuCode(item.getSku_code())==null){
                    return "the SKU code: "+item.getSku_code()+" is not exist!";
                }
                if(item.getRepo_id()==null||repoRepository.findFirst1ByRepoCode(item.getRepo_id())==null){
                    return "the Repo id: "+item.getRepo_id()+" is not exist!";
                }
            }
        }
        return "";
    }

    /**
     *  JPASpecification 实现复杂分页查询
     * @param b
     * @return
     */
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public JsonResult findAll(@RequestBody com.pccw.backend.bean.stock_movement.SearchBean b) {
        Specification specification = (Specification) (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if(!StringUtils.isEmpty(b.getRepoId())){
                predicate.getExpressions().add( criteriaBuilder.equal(root.get("logRepoOut"),b.getRepoId()));
            }
            if(!StringUtils.isEmpty(b.getToRepoId())){
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("logRepoIn"),b.getToRepoId()));
            }
            Path line = root.get("line");
            if(!StringUtils.isEmpty(b.getSkuNum())){
                CriteriaBuilder.In in = criteriaBuilder.in(line.get("dtlSkuId"));
                for (String s : b.getSkuNum()) {
                    in.value(s);
                }
                predicate.getExpressions().add(in);
            }
            if(!StringUtils.isEmpty(b.getCreateAt())){
                predicate.getExpressions().add(criteriaBuilder.between(root.get("createAt"),b.getCreateAt()[0],b.getCreateAt()[1]));
            }
            query.where(predicate);
            return predicate;
        };
        Page all = repo.findAll(specification, PageRequest.of(b.getPageIndex(), b.getPageSize()));
        return JsonResult.success(Arrays.asList(all.get().collect(Collectors.toList())));
    }

}
