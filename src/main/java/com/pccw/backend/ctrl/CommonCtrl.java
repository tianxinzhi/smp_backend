package com.pccw.backend.ctrl;

import com.pccw.backend.annotation.NoAuthorized;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.LabelAndValue;
import com.pccw.backend.bean.TreeNode;
import com.pccw.backend.bean.masterfile_repo.LoadTreeBean;
import com.pccw.backend.entity.DbResAccount;
import com.pccw.backend.entity.DbResAdjustReason;
import com.pccw.backend.entity.DbResAttrValue;
import com.pccw.backend.entity.DbResRepo;
import com.pccw.backend.repository.*;
import com.pccw.backend.util.CollectionBuilder;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AuthRightCtrl
 */

@Slf4j
@RestController
@RequestMapping("/common")
@CrossOrigin(origins = "*", allowCredentials = "false")
@NoAuthorized
public class CommonCtrl  extends BaseCtrl{

    @Autowired
    ResRightRepository right_repo;
    @Autowired
    ResSkuRepository sku_repo;
    @Autowired
    ResSpecRepository spec_repo;
    @Autowired
    ResTypeRepository type_repo;
    @Autowired
    ResAttrRepository attr_repo;
    @Autowired
    ResAttrValueRepository attr_value_repo;
    @Autowired
    ResClassRepository class_repo;
    @Autowired
    ResRepoRepository repo_repo;
    @Autowired
    ResAttrAttrValueRepository attr_attr_value_repo;
    @Autowired
    ResAdjustReasonRepository adjustReasonRepository;
    @Autowired
    ResRoleRepository roleRepository;
    @Autowired
    ResFlowRepository  flowRepository;
    @Autowired
    ResStockTypeRepository stockTypeRepository;
    @Autowired
    ResTrxTypeRepository trxTypeRepository;
    @Autowired
    ResAccountRepository accountRepository;

    @ApiOperation(value="获取res_right表的信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/rightModule")
    public JsonResult<TreeNode> search() {
//         return this.addRowJsonResultHandle(right_repo,new TreeNode(0L,-1L,"SMP"));
        return this.addRowJsonResultHandle(right_repo, CollectionBuilder.builder(new HashMap<>()).put("id",0).put("pid",-1).put("name","SMP").build());
    }

    @ApiOperation(value="获取res_sku表的skuCode和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/skuModule")
    public JsonResult<LabelAndValue> searchSku() {
        return this.JsonResultHandle(sku_repo,new LabelAndValue());
    }

    @ApiOperation(value="获取res_spec表的SpecName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/specModule")
    public JsonResult<LabelAndValue> searchSpec() {
        return this.JsonResultHandle(spec_repo,new LabelAndValue());
    }

    @ApiOperation(value="获取res_type表的TypeName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/typeModule")
    public JsonResult<LabelAndValue> searchType() {
        return this.JsonResultHandle(type_repo,new LabelAndValue());
    }

    @ApiOperation(value="获取res_attr表的AttrName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/attrModule")
    public JsonResult<LabelAndValue> searchAttr() {
        return this.JsonResultHandle(attr_repo,new LabelAndValue());
    }

    @ApiOperation(value="获取res_attr_value表的AttrValue和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/attrValueModule")
    public JsonResult<LabelAndValue> searchAttrValue() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<DbResAttrValue> spec = new Specification<DbResAttrValue>() {
            @Override
            public Predicate toPredicate(Root<DbResAttrValue> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("active").as(String.class), "Y");
                return predicate;
            }
        };
        List<DbResAttrValue> list = attr_value_repo.findAll(spec,sort);
        List<LabelAndValue> res = list.stream().map(item -> {
            return  Objects.nonNull(item.getAttrValue()) ? new LabelAndValue(item.getId(), item.getAttrValue(), null,null) : new LabelAndValue(item.getId(), item.getValueFrom()+"~"+item.getValueTo(),null,null );
        }).collect(Collectors.toList());

        return JsonResult.success(res);
    }

    @ApiOperation(value="获取res_class表的ClassName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/classValueModule")
    public JsonResult searchClass() {
        return this.JsonResultHandle(class_repo);
    }

    @ApiOperation(value="获取res_repo表的RepoCode，RepoType，id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/repoModule")
    public JsonResult<LoadTreeBean> searchRepo(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<DbResRepo> spec = new Specification<DbResRepo>() {
            @Override
            public Predicate toPredicate(Root<DbResRepo> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("active").as(String.class), "Y");
                return predicate;
            }
        };
        List<DbResRepo> list =  repo_repo.findAll(spec,sort);
        List<LoadTreeBean> res = list.stream().map(item -> {
            return new LoadTreeBean(item.getId(),item.getParentRepoId(),item.getRepoCode(),item.getRepoType(),item.getIsClosed() );
        }).collect(Collectors.toList());
        res.add(new LoadTreeBean(0L,-1L,"RM","","") );
        return JsonResult.success(res);
    }

    @ApiOperation(value="获取res_attr_attr_value表的AttrId和AttrValueId信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/attrAttrValueModule")
    public JsonResult<LabelAndValue> searchAttrAttrValue(){
        return this.JsonResultHandle(attr_attr_value_repo,new LabelAndValue());
    }

    @ApiOperation(value="获取res_adjust_reason表的adjustReasonName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/adjustReasonModule")
    public JsonResult<LabelAndValue> searchAdjustReason(){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<DbResAdjustReason> spec = new Specification<DbResAdjustReason>() {
            @Override
            public Predicate toPredicate(Root<DbResAdjustReason> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("active").as(String.class), "Y");
                return predicate;
            }
        };
        List<DbResAdjustReason> list =  adjustReasonRepository.findAll(spec,sort).stream().filter
                (reason -> !reason.getAdjustReasonName().equals("Other Reason")).collect(Collectors.toList());
        return this.customSearchJsonResultHandle(new LabelAndValue(),list);
    }

    @ApiOperation(value="获取res_role表的roleName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/roleModule")
    public JsonResult<LabelAndValue> searchRole(){
        return this.JsonResultHandle(roleRepository,new LabelAndValue());
    }

    @ApiOperation(value="获取res_flow表的Nature和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/flowModule")
    public JsonResult searchFlowNature(){
        return this.JsonResultHandle(flowRepository,new LabelAndValue());
    }

    @ApiOperation(value="获取res_stock_type表的stockTypeName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/stockTypeModule")
    public JsonResult<LabelAndValue> searchStockType(){
        return this.JsonResultHandle(stockTypeRepository,new LabelAndValue());
    }

    @ApiOperation(value="获取res_trx_type表的transactionTypeName和id信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/trxTypeModule")
    public JsonResult<LabelAndValue> searchTrxType(){
//        return this.JsonResultHandle(trxTypeRepository,new LabelAndValue());
        return this.JsonResultHandle(trxTypeRepository);
    }

    @ApiOperation(value="获取用户的session信息",tags={"common"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.GET,path="/searchUserSession")
    public JsonResult<Map> searchUserSession(){
        Map user = (Map) session.getUser();
        return JsonResult.success(Arrays.asList(user));
    }

    @ApiOperation(value = "提交right变更时，更新用户菜单", tags = {"system"}, notes = "注意问题点")
    @RequestMapping(method = RequestMethod.GET,value = "/menuReload")
    public JsonResult menuReload(){
        Map user = (Map)session.getUser();
        String accountId = user.get("account").toString();
        long id = Long.parseLong(accountId);
        DbResAccount account = accountRepository.findDbResAccountById(id);
        SystemCtrl systemCtrl = new SystemCtrl();
        //获取用户权限
        List<Long> rightIdList = systemCtrl.getUserRightIds(account);
        //根据权限id构建权限树
//        HashMap<Long, com.pccw.backend.bean.system.TreeNode> nodeMap = systemCtrl.generateRightTree(rightIdList);
        //构建所有权限的树形结构
        HashMap<Long, com.pccw.backend.bean.system.TreeNode> nodeMap = systemCtrl.getAllRightMap();
        //按照用户权限，筛选出对应菜单
//        List<com.pccw.backend.bean.system.TreeNode> userMenu = systemCtrl.getUserMenu(nodeMap);
        List<com.pccw.backend.bean.system.TreeNode> userMenu = systemCtrl.getUserMenu(rightIdList,nodeMap);
        return JsonResult.success(userMenu);
    }

}
