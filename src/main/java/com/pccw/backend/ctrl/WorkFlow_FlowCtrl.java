package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.workflow_flow.CreateBean;
import com.pccw.backend.bean.workflow_flow.EditBean;
import com.pccw.backend.bean.workflow_flow.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResFlow;
import com.pccw.backend.entity.DbResFlowStep;
import com.pccw.backend.entity.DbResRole;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResFlowRepository;
import com.pccw.backend.repository.ResRoleRepository;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * AuthRightCtrl
 */

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/workflow_flow")
public class WorkFlow_FlowCtrl extends BaseCtrl<DbResFlow> implements ICheck {

    @Autowired
    ResFlowRepository repo;

    @Autowired
    ResRoleRepository repoRole;

    @ApiOperation(value="查询workflow_flow",tags={"workflow_flow"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,path="/search")
    public JsonResult search(@RequestBody SearchBean b) {

        try {
            Specification specification = Convertor.convertSpecification(b);
            List<DbResFlow> res =repo.findAll(specification, PageRequest.of(b.getPageIndex(),b.getPageSize())).getContent();
            ArrayList<com.pccw.backend.bean.workflow_flow.SearchBean> dbResFlow = new ArrayList<>();

            if(res != null && res.size() > 0){
                for (DbResFlow flow:res){
                    SearchBean searchBean = new SearchBean();
                    BeanUtils.copyProperties(flow, searchBean);
                    searchBean.setCreateAccountName(getAccountName(flow.getCreateBy()));
                    searchBean.setUpdateAccountName(getAccountName(flow.getUpdateBy()));
                    List<Map> stepList = new ArrayList<>();
                    List<DbResFlowStep> sortedList = flow.getResFlowStepList().stream().sorted(Comparator.comparing(DbResFlowStep::getStepNum)).collect(Collectors.toList());

                    for(int i=0;i<sortedList.size();i++) {
                        Optional<DbResRole> optional = repoRole.findById(sortedList.get(i).getRoleId());
                        DbResRole resRole = optional.get();
                        //详情
                        HashMap<Object, Object> hm = new HashMap<>();
//                        hm.put("roleName",resRole.getRoleName());
//                        hm.put("stepNum",sortedList.get(i).getStepNum());
                        hm.put("value",resRole.getRoleName());
                        hm.put("label","Step"+sortedList.get(i).getStepNum());
                        stepList.add(hm);
                    }
                    searchBean.setStepData(stepList);
                    searchBean.setResFlowStepList(sortedList);
                    dbResFlow.add(searchBean);
                }
            }
            return JsonResult.success(dbResFlow,repo.count(specification));
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }

    }

    @ApiOperation(value="删除workflow_flow",tags={"workflow_flow"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids){
        return this.delete(repo,ids);
    }

    @ApiOperation(value="创建workflow_flow",tags={"workflow_flow"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,path="/create")
    public JsonResult create(@RequestBody CreateBean b){
       try{
            long t = new Date().getTime();
            b.setUpdateAt(t);
            b.setCreateAt(t);
            for(int i=0;i<b.getResFlowStepList().size();i++) {
                b.getResFlowStepList().get(i).setUpdateAt(t);
                b.getResFlowStepList().get(i).setCreateAt(t);
                b.getResFlowStepList().get(i).setCreateBy(getAccount());
                b.getResFlowStepList().get(i).setUpdateBy(getAccount());
                b.getResFlowStepList().get(i).setActive("Y");
            }
            return this.create(repo, DbResFlow.class, b);
       } catch (Exception e) {
           log.info(e.getMessage());
           return JsonResult.fail(e);
         }
    }

    @ApiOperation(value="编辑workflow_flow",tags={"workflow_flow"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,path="/edit")
    public JsonResult edit(@RequestBody EditBean b){
        try{
            long t = new Date().getTime();
            Optional<DbResFlow> optional = repo.findById(b.getId());
            DbResFlow dbResFlow =optional.get();
            b.setUpdateAt(t);
            b.setCreateAt(dbResFlow.getCreateAt());
            for(int i=0;i<b.getResFlowStepList().size();i++) {
                for(int j=0;j<dbResFlow.getResFlowStepList().size();j++) {
                    if (dbResFlow.getResFlowStepList().get(j).getId().equals(b.getResFlowStepList().get(i).getId())) {
                        b.getResFlowStepList().get(i).setUpdateAt(t);
                        b.getResFlowStepList().get(i).setCreateAt(dbResFlow.getResFlowStepList().get(j).getCreateAt());
                        b.getResFlowStepList().get(i).setCreateBy(getAccount());
                        b.getResFlowStepList().get(i).setUpdateBy(getAccount());
                        b.getResFlowStepList().get(i).setActive("Y");
                    }else if(Objects.isNull(b.getResFlowStepList().get(i).getId())){
                        b.getResFlowStepList().get(i).setUpdateAt(t);
                        b.getResFlowStepList().get(i).setCreateAt(t);
                        b.getResFlowStepList().get(i).setCreateBy(getAccount());
                        b.getResFlowStepList().get(i).setUpdateBy(getAccount());
                        b.getResFlowStepList().get(i).setActive("Y");
                    }
                }
            }
            return this.edit(repo, DbResFlow.class, b);

        }catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }

    }

    @ApiOperation(value="禁用workflow_flow",tags={"workflow_flow"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, WorkFlow_FlowCtrl.class);
    }

    @ApiOperation(value="启用workflow_flow",tags={"workflow_flow"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj, BaseRepository... check) {
        return 0;
    }
}
