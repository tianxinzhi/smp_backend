package com.pccw.backend.ctrl;

import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.auth_account.CreateBean;
import com.pccw.backend.bean.auth_account.EditBean;
import com.pccw.backend.bean.auth_account.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResAccount;
import com.pccw.backend.entity.DbResAccountRole;
import com.pccw.backend.entity.DbResRole;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResAccountRepository;
import com.pccw.backend.repository.ResRoleRepository;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("auth_account")
@Api(value="Auth_AccountCtrl",tags={"auth_account"})
public class Auth_AccountCtrl extends BaseCtrl<DbResAccount> implements ICheck {

    @Autowired
    ResAccountRepository repo;
    @Autowired
    ResRoleRepository roleRepo;

    @ApiOperation(value="创建用户",tags={"auth_account"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/create")
    public JsonResult create(@RequestBody CreateBean bean) {
        try {
            System.out.println("bean:"+bean);
            DbResAccount account = new DbResAccount();
            List<DbResAccountRole> accountRoles = new LinkedList<>();
            for (Long aLong : bean.getRoles()) {
                DbResAccountRole accountRole = new DbResAccountRole();
                accountRole.setRoleId(aLong);
                accountRole.setActive("Y");
                accountRole.setCreateAt(System.currentTimeMillis());
                accountRole.setUpdateAt(System.currentTimeMillis());
                accountRole.setCreateBy(getAccount());
                accountRole.setUpdateBy(getAccount());
                accountRoles.add(accountRole);
            }
            BeanUtils.copyProperties(bean,account);
            account.setActive("Y");
            account.setCreateAt(System.currentTimeMillis());
            account.setUpdateAt(System.currentTimeMillis());
            account.setCreateBy(this.getAccount());
            account.setUpdateBy(this.getAccount());
            account.setAccountRoles(accountRoles);
            repo.saveAndFlush(account);
            return JsonResult.success(Arrays.asList());
        } catch (BeansException e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="删除用户",tags={"auth_account"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids) {
        return this.delete(repo,ids);
    }

    @ApiOperation(value="修改用户",tags={"auth_account"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/edit")
    public JsonResult edit(@RequestBody EditBean b) {
        try {
            System.out.println("bean:"+b);
            DbResAccount account = repo.findById(b.getId()).get();
            List<DbResAccountRole> accountRoles = account.getAccountRoles();
            accountRoles.clear();
            for (Long aLong : b.getRoles()) {
                DbResAccountRole accountRole = new DbResAccountRole();
                accountRole.setRoleId(aLong);
                accountRole.setActive("Y");
                accountRole.setCreateAt(System.currentTimeMillis());
                accountRole.setUpdateAt(System.currentTimeMillis());
                accountRole.setCreateBy(getAccount());
                accountRole.setUpdateBy(getAccount());
                accountRoles.add(accountRole);
            }
            BeanUtils.copyProperties(b,account);
            account.setActive("Y");
            //account.setCreateAt(System.currentTimeMillis());
            account.setUpdateAt(System.currentTimeMillis());
            account.setUpdateBy(this.getAccount());
            repo.saveAndFlush(account);
            return JsonResult.success(Arrays.asList());
        } catch (BeansException e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="搜索用户",tags={"auth_account"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/search")
    public JsonResult search(@RequestBody SearchBean bean){
        try {
            Specification<DbResAccount> spec = Convertor.<DbResAccount>convertSpecification(bean);
            List<DbResAccount> skuList = repo.findAll(spec, PageRequest.of(bean.getPageIndex(),bean.getPageSize())).getContent();
            List<SearchBean> resList = new LinkedList<>();
            for (DbResAccount account : skuList) {
                SearchBean resBean = new SearchBean();
    //            resBean.setAccountName(account.getAccountName());
    //            resBean.setAccountPassword(account.getAccountPassword());
    //            resBean.setId(account.getId());
                BeanUtils.copyProperties(account,resBean);
                String[] roleNames = new String[account.getAccountRoles().size()];
                Long[] roles = new Long[account.getAccountRoles().size()];
                for (int i=0;i<account.getAccountRoles().size();i++) {
                    DbResRole role = roleRepo.findById(account.getAccountRoles().get(i).getRoleId()).get();
                    roleNames[i] = role.getRoleName();
                    roles[i] = role.getId();
                }
                resBean.setRoles(roles);
                resBean.setRoleNames(roleNames);
                resBean.setCreateAccountName(getAccountName(resBean.getCreateBy()));
                resBean.setUpdateAccountName(getAccountName(resBean.getUpdateBy()));
                resList.add(resBean);
            }
            return JsonResult.success(resList,repo.count(spec));
        } catch (IllegalArgumentException | IllegalAccessException | BeansException e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="禁用auth_account",tags={"auth_account"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, Auth_AccountCtrl.class);
    }

    @ApiOperation(value="启用auth_account",tags={"auth_account"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj, BaseRepository... check) {
        return 0;
    }

}
