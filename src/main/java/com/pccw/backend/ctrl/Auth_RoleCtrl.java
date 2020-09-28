package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.auth_role.CreateBean;
import com.pccw.backend.bean.auth_role.EditBean;
import com.pccw.backend.bean.auth_role.RoleRightEditBean;
import com.pccw.backend.bean.auth_role.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResAccountRole;
import com.pccw.backend.entity.DbResRight;
import com.pccw.backend.entity.DbResRole;
import com.pccw.backend.entity.DbResRoleRight;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResAccountRoleRepository;
import com.pccw.backend.repository.ResRightRepository;
import com.pccw.backend.repository.ResRoleRepository;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * AuthRightCtrl
 */

@Slf4j
@RestController
@RequestMapping("/auth_role")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@Api(value="AuthRoleCtrl",tags={"auth_role"})
public class Auth_RoleCtrl extends BaseCtrl<DbResRole> implements ICheck {

    @Autowired
    ResRoleRepository repo;

    @Autowired
    ResRightRepository repoRight;

    @Autowired
    ResAccountRoleRepository accountRoleRepository;

    @RequestMapping(method = RequestMethod.POST,path="/search")
    @ApiOperation(value="搜索角色",tags={"auth_role"},notes="注意问题点")
    public JsonResult search(@ApiParam(name="SearchBean",value="搜索条件",required=true) @RequestBody SearchBean b) {
        try {
        Specification spec = Convertor.convertSpecification(b);
        List<DbResRole> roleList = repo.findAll(spec,PageRequest.of(b.getPageIndex(),b.getPageSize())).getContent();
        List<SearchBean> resultBeans = new LinkedList<>();
        for (DbResRole resRole : roleList) {
            List<Map> rightList = new ArrayList<>();
            SearchBean resultBean = new SearchBean();
            BeanUtils.copyProperties(resRole,resultBean);
            resultBean.setCreateAccountName(getAccountName(resRole.getCreateBy()));
            resultBean.setUpdateAccountName(getAccountName(resRole.getUpdateBy()));
            String[] rightNames = new String[resRole.getResRoleRightList().size()];
            long[] rightIds  = new long[resRole.getResRoleRightList().size()];
            for(int i=0;i<resRole.getResRoleRightList().size();i++) {
                Optional<DbResRight> optional = repoRight.findById(resRole.getResRoleRightList().get(i).getRightId());
                DbResRight resRight = optional.get();
                rightNames[i] = resRight.getRightName();
                rightIds[i] = resRight.getId();
                //详情
                HashMap<Object, Object> hm = new HashMap<>();
//                hm.put("rightName",resRight.getRightName());
//                hm.put("rightUrl",resRight.getRightUrl());
                  hm.put("value",resRight.getRightUrl());
                  hm.put("label",resRight.getRightName());
                rightList.add(hm);
               }
            resultBean.setId(resRole.getId());
            resultBean.setRightName(rightNames);
            resultBean.setRightId(rightIds);
            resultBean.setRightData(rightList);
            System.out.println("result:" + resultBean);
            resultBeans.add(resultBean);
            }
           return JsonResult.success(resultBeans,repo.count(spec));
        }catch (Exception e) {
                log.info(e.getMessage());
                return JsonResult.fail(e);
            }
    }

    @RequestMapping(method = RequestMethod.POST,path = "/delete")
    @ApiOperation(value="删除角色",tags={"auth_role"},notes="注意问题点")
    public JsonResult delete(@RequestBody BaseDeleteBean ids){
        return this.delete(repo,ids);
    }

    @RequestMapping(method = RequestMethod.POST,path="/create")
    @ApiOperation(value="新增角色",tags={"auth_role"},notes="注意问题点")
    public JsonResult create(@RequestBody CreateBean b){
        try {
            long t = new Date().getTime();
            b.setCreateAt(t);
            b.setActive("Y");
            b.setUpdateAt(t);
            b.setCreateBy(getAccount());
            b.setUpdateBy(getAccount());
            DbResRole role =new DbResRole();
            BeanUtils.copyProperties(b,role);
            List<DbResRoleRight> roleRightList = new LinkedList<>();
            for(String value:b.getRightId()){
                DbResRoleRight roleRight = new DbResRoleRight();
                roleRight.setRightId(Long.parseLong(value));
                roleRight.setActive("Y");
                roleRight.setCreateAt(System.currentTimeMillis());
                roleRight.setUpdateAt(System.currentTimeMillis());
                roleRight.setCreateBy(getAccount());
                roleRight.setUpdateBy(getAccount());
                roleRightList.add(roleRight);
            }
            role.setResRoleRightList(roleRightList);
            repo.saveAndFlush(role);
            return JsonResult.success(Arrays.asList());

        }catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }

    }

    @RequestMapping(method = RequestMethod.POST,path="/edit")
    @ApiOperation(value="修改角色",tags={"auth_role"},notes="注意问题点")
    public JsonResult edit(@RequestBody EditBean b){
        try {
            DbResRole role = repo.findById(b.getId()).get();
            role.setRoleDesc(b.getRoleDesc());
            role.setRoleName(b.getRoleName());
            role.setUpdateAt(System.currentTimeMillis());
            role.setUpdateBy(getAccount());
            List<DbResRoleRight> roleRightList = role.getResRoleRightList();
            roleRightList.clear();
            for(String valueId:b.getRightId()){
                RoleRightEditBean roleRight = new RoleRightEditBean();
                roleRight.setRightId(Long.parseLong(valueId));
                roleRight.setRoleId(role.getId());
                roleRight.setActive("Y");
                roleRight.setCreateAt(System.currentTimeMillis());
                roleRight.setUpdateAt(System.currentTimeMillis());
                roleRight.setCreateBy(getAccount());
                roleRight.setUpdateBy(getAccount());
                roleRightList.add(roleRight);
            }
            role.setResRoleRightList(roleRightList);
            repo.saveAndFlush(role);
            return JsonResult.success(Arrays.asList());

        }catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="禁用auth_role",tags={"auth_role"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, Auth_RoleCtrl.class,accountRoleRepository);
    }

    @ApiOperation(value="启用auth_role",tags={"auth_role"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj, BaseRepository... check) {
        ResAccountRoleRepository tRepo = (ResAccountRoleRepository)check[0];
        BaseDeleteBean bean = (BaseDeleteBean)obj;
        for (Long id : bean.getIds()) {
            List<DbResAccountRole> accountRoles = tRepo.getDbResAccountRolesByRoleId(id);
            if ( accountRoles != null && accountRoles.size()>0 ) {
                return id;
            }
        }
        return 0;
    }
}
