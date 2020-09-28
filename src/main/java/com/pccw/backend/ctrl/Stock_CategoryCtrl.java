package com.pccw.backend.ctrl;


import com.pccw.backend.annotation.NoAuthorized;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.stock_category.CategoryLogMgtBean;
import com.pccw.backend.bean.stock_category.SearchBean;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.Session;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Slf4j
@RestController
@RequestMapping("/stock_category")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Stock_CategoryCtrl extends BaseCtrl<DbResLogMgt> {

    @Autowired
    private ResStockTypeRepository stockTypeRepository;
    @Autowired
    private ResAccountRepository accountRepo;
    @Autowired
    private ResSkuRepository skuRepo;
    @Autowired
    private ResRepoRepository repoRepository;
    @Autowired
    private ResSkuRepoRepository skuRepoRepository;
    @Autowired
    private Session session;
    @Autowired
    private ResLogMgtRepository resLogMgtRepository;
    @Autowired
    Process_ProcessCtrl processProcessCtrl;

    @ApiOperation(value = "根据repoId和skuId搜索stockType",tags = "stock_category",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/skuStockTypeSearch")
    public JsonResult skuStockTypeSearch(@RequestBody SearchBean sb){
        try {
            ArrayList<Object> returnList = new ArrayList<>();
            DbResSku dbResSku = skuRepo.findById(sb.getSkuId()).get();
            DbResRepo dbResRepo = repoRepository.findById(sb.getRepoId()).get();
            List<DbResSkuRepo> list = skuRepoRepository.findDbResSkuRepoByRepoAndSku(dbResRepo,dbResSku);
            list.forEach(data->{
                    HashMap<Object, Object> hashMap = new HashMap<>();
                    DbResStockType stockType = data.getStockType();
                    //change category只能转available，demo，faulty 3者之间，其他不能转
                    if(stockType.getId() == 1 || stockType.getId() == 2 || stockType.getId() == 3){
                    hashMap.put("value",stockType.getId());
                    hashMap.put("label",stockType.getStockTypeName());
                    returnList.add(hashMap);
                }
            });
            return JsonResult.success(returnList);
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="创建stock_category",tags={"stock_category"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/create")
    public JsonResult create(@RequestBody CategoryLogMgtBean b){
        try {
            DbResLogMgt dbResLogMgt = new DbResLogMgt();
            ArrayList list= new ArrayList<>();
            long t = new Date().getTime();
            b.setCreateAt(t);
            b.setUpdateAt(t);
            b.setActive("Y");
            b.setCreateBy(getAccount());
            b.setUpdateBy(getAccount());
            b.getLine().forEach(dtl->{
                dtl.setCreateAt(t);
                dtl.setCreateBy(getAccount());
                dtl.setUpdateAt(t);
                dtl.setUpdateBy(getAccount());
                dtl.setActive("Y");
                DbResLogMgtDtl dbResLogMgtDtlDeduct = new DbResLogMgtDtl();
                BeanUtils.copyProperties(dtl,dbResLogMgtDtlDeduct);
                dbResLogMgtDtlDeduct.setStatus(dtl.getStatus());
                list.add(dbResLogMgtDtlDeduct);
                DbResLogMgtDtl dtlBeanDeAdd = new DbResLogMgtDtl();
                BeanUtils.copyProperties(dtl,dtlBeanDeAdd);
                dtlBeanDeAdd.setDtlAction(StaticVariable.DTLACTION_ADD);
                if(dtl.getToStockTypeId() == 3){
                    dtlBeanDeAdd.setStatus(StaticVariable.STATUS_AVAILABLE);
                    dtlBeanDeAdd.setDtlSubin(StaticVariable.DTLSUBIN_AVAILABLE);
                }else if(dtl.getToStockTypeId() == 2){
                    dtlBeanDeAdd.setStatus(StaticVariable.STATUS_FAULTY);
                    dtlBeanDeAdd.setDtlSubin(StaticVariable.DTLSUBIN_FAULTY);
                }else{
                    dtlBeanDeAdd.setStatus(StaticVariable.STATUS_DEMO);
                    dtlBeanDeAdd.setDtlSubin(StaticVariable.DTLSUBIN_DEMO);
                }
                list.add(dtlBeanDeAdd);
            });
            BeanUtils.copyProperties(b,dbResLogMgt);
            dbResLogMgt.setLine(list);
            resLogMgtRepository.saveAndFlush(dbResLogMgt);

            this.UpdateSkuRepoQty(b.getLogTxtBum());
            //创建工作流对象
//            DbResProcess process = new DbResProcess();
//
//            process.setLogTxtBum(b.getLogTxtBum());
//            process.setRepoId(b.getLogRepoOut());
//            process.setRemark(b.getRemark());
//            process.setCreateAt(t);
//            process.setUpdateAt(t);
//            process.setLogOrderNature(b.getLogOrderNature());
//
//            //生成工作流数据
//            processProcessCtrl.joinToProcess(process);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    /**
     * stock category 修改表 skuRepo
     * @param logTxtBum
     */
    public void UpdateSkuRepoQty(String logTxtBum) {

        DbResLogMgt cb = resLogMgtRepository.findDbResLogMgtByLogTxtBum(logTxtBum);
        List<DbResLogMgtDtl> line = cb.getLine();
        line.forEach(l->{
                long t = new Date().getTime();
                long stockTyepId = 0;
                if(StaticVariable.DTLSUBIN_AVAILABLE.equals(l.getDtlSubin())){
                    stockTyepId = 3l;
                }else if(StaticVariable.DTLSUBIN_FAULTY.equals(l.getDtlSubin())){
                    stockTyepId = 2l;
                }else {
                    stockTyepId = 1l;
                }
//            String[] itemCodeArr = l.getItemCode().split(",");
            //dtlAction为D时  skuRepo对应的sku的qty做减法
            if(StaticVariable.DTLACTION_DEDUCT.equals(l.getDtlAction())){
                DbResSkuRepo sk = skuRepoRepository.findQtyByRepoAndShopAndType(l.getDtlRepoId(), l.getDtlSkuId(),stockTyepId);
                DbResSkuRepo skuRepo = skuRepoRepository.findById(sk.getId()).get();
                skuRepo.setUpdateAt(t);
                skuRepo.setUpdateBy(getAccount());
                Long qty =  (skuRepo.getQty() - l.getDtlQty());
                skuRepo.setQty(qty);
                //处理sku_repo表关联的itemcode表数据

//                List<DbResSkuRepoItem> skuRepoItemList = skuRepo.getSkuRepoItemList();
//                for (DbResSkuRepoItem item:skuRepoItemList){
//                    if(Arrays.asList(itemCodeArr).contains(item.getItemCode())){
//                        skuRepoItemList.remove(item);
//                    }
//                }
//                if(skuRepoItemList.size()>0){
//                skuRepo.setSkuRepoItemList(skuRepoItemList);
//                }
                skuRepoRepository.saveAndFlush(skuRepo);
            }else {
                //dtlAction为A时  skuRepo对应的sku的qty做加法
                DbResSkuRepo dbsr = skuRepoRepository.findQtyByRepoAndShopAndType(l.getDtlRepoId(), l.getDtlSkuId(),stockTyepId);
                DbResStockType dbResStockType = stockTypeRepository.findById(stockTyepId).get();
                DbResSku dbResSku = skuRepo.findById(l.getDtlSkuId()).get();
                DbResRepo dbResRepo = repoRepository.findById(l.getDtlRepoId()).get();
                //如果要转成某个状态的数据不存在 则添加一条 如果存在 直接做QTY加减 To StockCategory
                if(Objects.isNull(dbsr)){
                    DbResSkuRepo toSkuRepo = new DbResSkuRepo();
                    toSkuRepo.setSku(dbResSku);
                    toSkuRepo.setRepo(dbResRepo);
                    toSkuRepo.setCreateAt(t);
                    toSkuRepo.setCreateBy(getAccount());
                    toSkuRepo.setStockType(dbResStockType);
                    toSkuRepo.setId(null);
                    toSkuRepo.setQty(l.getDtlQty());
                    //处理sku_repo表关联的itemcode表数据
//                    List skuRepoItemList = new ArrayList<>();
//                    for (String i:itemCodeArr){
//                        DbResSkuRepoItem skuRepoItem = new DbResSkuRepoItem();
//                        skuRepoItem.setActive("Y");
//                        skuRepoItem.setCreateAt(t);
//                        skuRepoItem.setCreateBy(getAccount());
//                        skuRepoItem.setItemCode(i);
//                        skuRepoItem.setSkuRepo(toSkuRepo);
//                        skuRepoItemList.add(skuRepoItem);
//                    }
//                    toSkuRepo.setSkuRepoItemList(skuRepoItemList);
                    skuRepoRepository.saveAndFlush(toSkuRepo);
                }else {
                    DbResSkuRepo skuRepo1 = skuRepoRepository.findById(dbsr.getId()).get();
                    skuRepo1.setQty((dbsr.getQty()+l.getDtlQty()));
                    skuRepo1.setUpdateAt(t);
                    skuRepo1.setUpdateBy(getAccount());
                    //处理sku_repo表关联的itemcode表数据
//                    List<DbResSkuRepoItem> skuRepoItemList = skuRepo1.getSkuRepoItemList();
//                    for (String i:itemCodeArr){
//                        DbResSkuRepoItem skuRepoItem = new DbResSkuRepoItem();
//                        skuRepoItem.setActive("Y");
//                        skuRepoItem.setCreateAt(t);
//                        skuRepoItem.setCreateBy(getAccount());
//                        skuRepoItem.setItemCode(i);
//                        skuRepoItem.setSkuRepo(skuRepo1);
//                        skuRepoItemList.add(skuRepoItem);
//                    }
//                    skuRepo1.setSkuRepoItemList(skuRepoItemList);
                    skuRepoRepository.saveAndFlush(skuRepo1);
                }
            }
        });
    }

//    @NoAuthorized
    @RequestMapping(method = RequestMethod.POST,path = "/search")
    public JsonResult search(@RequestBody SearchBean sb){
        List<String> status = new ArrayList<>();
        status.add(StaticVariable.LOGORDERNATURE_STOCK_CATEGORY);
        List<DbResLogMgt> logMgts = resLogMgtRepository.findByLogOrderNatureIn(status);
        return JsonResult.success(logMgts);
    }

}
