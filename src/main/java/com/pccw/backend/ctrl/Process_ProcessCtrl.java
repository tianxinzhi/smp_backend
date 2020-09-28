package com.pccw.backend.ctrl;

import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.StaticVariable;
import com.pccw.backend.bean.process_process.*;
import com.pccw.backend.entity.*;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.Convertor;
import com.pccw.backend.util.Session;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/process")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Process_ProcessCtrl extends BaseCtrl {

    @Autowired
    ResProcessRepository processRepository;

    @Autowired
    ResRoleRepository roleRepository;

    @Autowired
    ResLogMgtRepository logMgtRepository;

    @Autowired
    ResLogReplRepository logReplRepository;

    @Autowired
    ResAccountRepository accountRepository;

    @Autowired
    Session<Map> session;

    @Autowired
    ResFlowRepository repoFlow;

    @Autowired
    Stock_OutCtrl outCtrl;

    @Autowired
    Stock_InCtrl inCtrl;

    @Autowired
    Stock_AdjustmentCtrl adjCtrl;

    @Autowired
    Stock_CategoryCtrl categoryCtrl;

    @Autowired
    Stock_ReplenishmentCtrl replenishmentCtrl;

    @ApiOperation(value="process",tags={"process"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/edit")
    public JsonResult edit(@RequestBody EditBean b){
        try{
            log.info(b.toString());
            long t = new Date().getTime();
            Map user = session.getUser();
            Long accountId = Long.parseLong(user.get("account").toString());
            String remark = b.getRemark();
            Optional<DbResProcess> optional = processRepository.findById(b.getId());
            DbResProcess dbResProcess = optional.get();
            BeanUtils.copyProperties(dbResProcess,b);
            b.setUpdateAt(t);
            b.setUpdateBy(accountId);
            b.setStatus(b.getStatusPro());
            b.setRemark(remark);
            for(int i=0;i<b.getSteps().size();i++) {
                for(int j=0;j<b.getProcessDtls().size();j++) {
                    if (b.getProcessDtls().get(j).getId().equals(b.getSteps().get(i).getProcessDtlsId()) ) {
                          b.getProcessDtls().get(j).setUpdateAt(t);
                          b.getProcessDtls().get(j).setUpdateBy(accountId);
                          b.getProcessDtls().get(j).setActive("Y");
                          b.getProcessDtls().get(j).setRemark(b.getSteps().get(i).getRemark());
                          b.getProcessDtls().get(j).setStatus(b.getSteps().get(i).getStatus());
                    }
                }
            }

            JsonResult result = this.edit(processRepository, DbResProcess.class, b);

            //审批流程修改成功，且最后一步审批通过，将log信息存入skuRepo表
            if(b.getStatusPro().equals(StaticVariable.PROCESS_APPROVED_STATUS) && result.getCode().equals("000")){
                //根据LogOrderNature判断从哪个ctrl更新数据
                if(b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_STOCK_OUT_STS)||b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_STOCK_OUT_STW)){
                    outCtrl.UpdateSkuRepoQty(b.getLogTxtBum());
                }else if(b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_STOCK_IN_STS)||b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_STOCK_IN_WITHOUT_PO_STW)){
                    inCtrl.UpdateSkuRepoQty(b.getLogTxtBum());
                }else if(b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_STOCK_IN_FROM_WAREHOUSE)){
                    inCtrl.UpdateSkuRepoQty(b.getLogTxtBum());
                } else if(b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_STOCK_TAKE_ADJUSTMENT)){
                    adjCtrl.UpdateSkuRepoQty(b.getLogTxtBum());
                }else if(b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_REPLENISHMENT_REQUEST)){
                    inCtrl.UpdateSkuRepoQty(b.getLogTxtBum());
                }else if(b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_STOCK_CATEGORY)){
                    categoryCtrl.UpdateSkuRepoQty(b.getLogTxtBum());
                }else if(b.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_REPLENISHMENT_RECEIVEN)){
                    replenishmentCtrl.UpdateSkuRepoQty(b.getLogTxtBum());
                }
            }
//            String stockCtrl = new String();
//            Class<?> stockClass = Class.forName(stockCtrl);
//            Method stockMethod = stockClass.getMethod("UpdateSkuRepoQty",String.class);
//            Object stockObject = stockClass.newInstance();
//            stockMethod.invoke(stockObject,b.getLogTxtBum());
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }

    }


    @ApiOperation(value = "搜索recode",tags = "Process",notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/search")
    public JsonResult search(@RequestBody SearchBean bean){
        try {
            Map user = session.getUser();
            //String roles = user.get("role").toString();
            List<Integer> useRoles = (List<Integer>)user.get("role");
            List<String> roles = useRoles.stream().map(x ->String.valueOf(x)).collect(Collectors.toList());
            List<DbResProcess> res  =new ArrayList<DbResProcess>();
            timeRangeHandle(bean);
            if(bean.getFilter()!=null&&bean.getFilter().equals("Pending For Me")){
                String nature = bean.getLogOrderNature() == null ? "" : bean.getLogOrderNature();
                String repoId = bean.getRepoId() == null ? "" : String.valueOf(bean.getRepoId());
                String txtNum = bean.getLogTxtBum() == null ? "" : bean.getLogTxtBum();
                Long accountId =  Long.parseLong(user.get("account").toString());
                //查询当前登陆人的role 可处理 审批step当前状态是PENDING的process
                List<Long> ids = processRepository.findIdsByPending(accountId,nature,repoId,txtNum,Long.parseLong(bean.getCreateAt()[0]),Long.parseLong(bean.getCreateAt()[1]));
                res = processRepository.findDbResProcessesByIdIn(ids);
            }else if(bean.getFilter()!=null&&bean.getFilter().equals("My Request")){
                ReqOrPedSearchBean reqOrPed=new ReqOrPedSearchBean();
                BeanUtils.copyProperties(bean,reqOrPed);
                reqOrPed.setCreateBy(Long.parseLong(user.get("account").toString()));
                res =processRepository.findAll(Convertor.convertSpecification(reqOrPed));
            }else {
                res = processRepository.findAll(Convertor.convertSpecification(bean));
            }
            List<RecodeBean> list = getRecodes(res,roles).stream().sorted(Comparator.comparing(RecodeBean::getCreateAt)).collect(Collectors.toList());
            return JsonResult.success(list);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }


//    @ApiOperation(value = "搜索my request",tags = "Process",notes = "注意问题点")
//    @RequestMapping(method = RequestMethod.POST,path = "/myReqSearch")
//    public JsonResult  myReqSearch(@RequestBody ReqOrPedSearchBean bean){
//        try {
//            Map user = session.getUser();
//            bean.setCreateBy(Long.parseLong(user.get("account").toString()));
//            List<DbResProcess> res = getDbResProcesses(bean);
//            String roles = user.get("role").toString();
//            List<RecodeBean> list = getRecodes(res,roles);
//            //return list;
//            return JsonResult.success(list);
//        } catch (Exception e) {
//            return JsonResult.fail(e);
//        }
//    }
//
//    @ApiOperation(value = "搜索pending for me",tags = "Process",notes = "注意问题点")
//    @RequestMapping(method = RequestMethod.POST,path = "/myPendingSearch")
//    public JsonResult myPendingSearch(@RequestBody ReqOrPedSearchBean bean){
//        try {
//            timeRangeHandle(bean);
//
//            String nature = bean.getLogOrderNature() == null ? "" : bean.getLogOrderNature();
//            String repoId = bean.getRepoId() == null ? "" : String.valueOf(bean.getRepoId());
//            String txtNum = bean.getLogTxtBum() == null ? "" : bean.getLogTxtBum();
//            Map user = session.getUser();
//            Long accountId =  Long.parseLong(user.get("account").toString());
//            //查询当前登陆人的role 可处理 审批step当前状态是PENDING的process
//            List<Long> ids = processRepository.findIdsByPending(accountId,nature,repoId,txtNum,Long.parseLong(bean.getCreateAt()[0]),Long.parseLong(bean.getCreateAt()[1]));
//
//            List<DbResProcess> res = processRepository.findDbResProcessesByIdIn(ids);
//            String roles = user.get("role").toString();
//            List<RecodeBean> list = getRecodes(res,roles);
//
//            return JsonResult.success(list);
//        } catch (Exception e) {
//            log.info(e.toString());
//            return JsonResult.fail(e);
//        }
//    }

//    /**
//     * 通过查询条件查询Process和Process明细数据
//     * @param bean
//     * @return
//     * @throws IllegalAccessException
//     */
//    private List<DbResProcess> getDbResProcesses(@RequestBody SearchBean bean) throws IllegalAccessException {
//        timeRangeHandle(bean);
//        log.info(bean.toString());
//
//        return processRepository.findAll(Convertor.convertSpecification(bean));
//    }

    /**
     * 处理获取的时间范围字段
     * @param bean
     */
    private void timeRangeHandle(@RequestBody SearchBean bean) {
        Date[] dateRange = Objects.nonNull(bean.getDate()) && bean.getDate().length>0 ? bean.getDate() : new Date[2];

        dateRange[0] = Convertor.beginOfDay(dateRange[0]);
        dateRange[1] = Convertor.endOfDay(dateRange[1]);

        String[] timeRange = {Objects.nonNull(dateRange[0]) ? ""+dateRange[0].getTime() : "1576339200239",Objects.nonNull(dateRange[1]) ? ""+dateRange[1].getTime() : "4070966399164"};
        bean.setCreateAt(timeRange);
    }

    /**
     * 将字段转为对应的step状态
     * @param str
     * @return
     */
    private String getStepActive(String str) {
        String status = "";

        switch (str){
            case StaticVariable.PROCESS_PENDING_STATUS:
                status = "process";
                break;
            case StaticVariable.PROCESS_APPROVED_STATUS:
                status = "finish";
                break;
            case StaticVariable.PROCESS_WAITING_STATUS:
                status = "wait";
                break;
            case StaticVariable.PROCESS_REJECTED_STATUS:
                status = "error";
                break;
        }

        return status;
    }

    /**
     * 封装返回页面的数据格式
     * @param res
     * @return
     */
    private List<RecodeBean> getRecodes(List<DbResProcess> res, List<String> roles) {
        return res.stream().map(r -> {
            //判断操作人是否具有当前审批权限
            List<DbResProcessDtl> collect = r.getProcessDtls().stream().filter(item ->
                    //判断当前状态为pending，且当前行的roleId存在于操作人的roleId中
                    item.getStatus().equals(StaticVariable.PROCESS_PENDING_STATUS) && roles.contains(item.getRoleId().toString())
            ).collect(Collectors.toList());
            boolean checkable = collect.size() > 0 ;
            //log表信息 根据nature从相应的entity取process详情页展示log信息
            LogProDtlBean logDtls =new LogProDtlBean();
            if(r.getLogOrderNature().equals(StaticVariable.LOGORDERNATURE_REPLENISHMENT_REQUEST )){
                DbResLogRepl resLog = logReplRepository.findDbResLogReplByLogTxtBum(r.getLogTxtBum());
                logDtls.setCreateAt(resLog.getCreateAt());
                logDtls.setLogRepoIn(resLog.getRepoIdTo());
                logDtls.setLogRepoOut(resLog.getRepoIdFrom());
                logDtls.setLogTxtBum(resLog.getLogTxtBum());
                logDtls.setRemark(resLog.getRemark());
                 List<LogProDtlLineBean> logLine =resLog.getLine().stream().map(item ->{
                        return new LogProDtlLineBean(item.getDtlSkuId(),item.getDtlQty());
                        }).collect(Collectors.toList());
                logDtls.setLine(logLine);
            }else{
                DbResLogMgt resLog = logMgtRepository.findDbResLogMgtsByLogTxtBum(r.getLogTxtBum()).get(0);
                logDtls.setCreateAt(resLog.getCreateAt());
                logDtls.setLogRepoIn(resLog.getLogRepoIn());
                logDtls.setLogRepoOut(resLog.getLogRepoOut());
                logDtls.setLogTxtBum(resLog.getLogTxtBum());
                logDtls.setRemark(resLog.getRemark());
                //stock out 只取一行
                List<DbResLogMgtDtl>  line= new ArrayList<>();
                if(resLog.getLogOrderNature().indexOf("Stock Out")!=-1){
                    line= resLog.getLine().stream().filter( resLine ->"Good".equals(resLine.getDtlSubin()) ).collect(Collectors.toList());
                }else {
                    line=resLog.getLine();
                }
                List<LogProDtlLineBean> logLine =line.stream().map(item ->{
                        return new LogProDtlLineBean(item.getDtlSkuId(),item.getDtlQty());
                        }).collect(Collectors.toList());
                logDtls.setLine(logLine);
            }
            //获取rolename 封装step数据
            List<Step> stepList = r.getProcessDtls().stream().sorted(Comparator.comparing(DbResProcessDtl::getStepNum)).map(item -> {
                String roleName = roleRepository.findById(item.getRoleId()).get().getRoleName();
                String accountName = this.getAccountName(item.getUpdateBy());
                //已审批的desc 显示审批意见，未操作数据显示roleName
                if(item.getStatus().equals(StaticVariable.PROCESS_APPROVED_STATUS)||item.getStatus().equals(StaticVariable.PROCESS_REJECTED_STATUS)){
                    return new Step(accountName, item.getRemark(), getStepActive(item.getStatus()), item.getStepNum(),item.getId(),item.getRemark(),item.getStatus(),item.getUpdateAt());
                }else {
                     return new Step(roleName, "", getStepActive(item.getStatus()), item.getStepNum(),item.getId(),item.getRemark(),item.getStatus(),null);
                }
            }).collect(Collectors.toList());
            //封装返回页面数据
            RecodeBean recodeBean = new RecodeBean();
            BeanUtils.copyProperties(r,recodeBean);
            recodeBean.setLogDtls(logDtls);
            recodeBean.setSteps(stepList);
            recodeBean.setCheckable(checkable);
            return recodeBean;
        }).collect(Collectors.toList());
    }

    /**
     *生成工作流数据
     * @param process
     */
    public void joinToProcess(DbResProcess process) {
        long t = new Date().getTime();
        process.setStatus(StaticVariable.PROCESS_PENDING_STATUS);
        Map user = session.getUser();
        Long accountId = Long.parseLong(user.get("account").toString());
        process.setCreateAt(t);
        process.setCreateBy(accountId);
        process.setActive("Y");
        //根据OrderNature查询flow,flow和nature一对一关系
        DbResFlow resFlow =repoFlow.findByFlowNature(process.getLogOrderNature());
        process.setFlowId(resFlow.getId());
        ArrayList<DbResProcessDtl> processDtls= new ArrayList<>();
        for(int i=0;i<resFlow.getResFlowStepList().size();i++) {
            DbResProcessDtl resProcessDtl  =new DbResProcessDtl();
            resProcessDtl.setFlowId(resFlow.getId());
            resProcessDtl.setStepId(resFlow.getResFlowStepList().get(i).getId());
            resProcessDtl.setStepNum(resFlow.getResFlowStepList().get(i).getStepNum());
            resProcessDtl.setRoleId(resFlow.getResFlowStepList().get(i).getRoleId());
            resProcessDtl.setCreateAt(t);
            resProcessDtl.setCreateBy(accountId);
            //审批流初始化数据StepNum1默认PENDING,其他StepNum默认WAITING
            if(resFlow.getResFlowStepList().get(i).getStepNum().equals("1")) {
                resProcessDtl.setStatus(StaticVariable.PROCESS_PENDING_STATUS);
            }else {
                resProcessDtl.setStatus(StaticVariable.PROCESS_WAITING_STATUS);
            }
            processDtls.add(resProcessDtl);
        }
        process.setProcessDtls(processDtls);
        processRepository.saveAndFlush(process);
    }


}
