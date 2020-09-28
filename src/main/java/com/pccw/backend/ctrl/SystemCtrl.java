package com.pccw.backend.ctrl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pccw.backend.annotation.NoAuthorized;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.ResultRecode;
import com.pccw.backend.bean.system.LoginBean;
import com.pccw.backend.bean.system.TreeNode;
import com.pccw.backend.entity.DbResAccount;
import com.pccw.backend.entity.DbResRight;
import com.pccw.backend.entity.DbResRole;
import com.pccw.backend.entity.DbResRoleRight;
import com.pccw.backend.exception.BaseException;
import com.pccw.backend.repository.ResAccountRepository;
import com.pccw.backend.repository.ResRightRepository;
import com.pccw.backend.repository.ResRoleRepository;
import com.pccw.backend.util.Session;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(methods = {RequestMethod.POST,RequestMethod.GET}, origins = "*", allowCredentials = "false")
@RequestMapping("system")
@Api(value = "SystemCtrl", tags = {"system"})
@NoAuthorized
public class SystemCtrl extends BaseCtrl<DbResAccount> {

    @Autowired
    ResAccountRepository repo;

    @Autowired
    ResRoleRepository roleRepository;

    @Autowired
    ResRightRepository rightRepository;

    @Autowired
    Session session;

    @Autowired
    HttpServletRequest request;

    @ApiOperation(value = "用户登录", tags = {"system"}, notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public JsonResult login(@RequestBody LoginBean bean) {
        try {
            DbResAccount rwe = repo.getDbResAccountsByAccountNameAndAccountPassword(bean.getUsername(), bean.getPassword());
            if (Objects.isNull(rwe))
                return JsonResult.fail(BaseException.getAccAndPwdException());

            //获取用户权限
            List<Long> rightIdList = getUserRightIds(rwe);
            //构建所有权限的树形结构
            HashMap<Long, TreeNode> nodeMap = getAllRightMap();
            //根据权限id构建权限树
//            HashMap<Long, TreeNode> nodeMap = generateRightTree(rightIdList);
            //构建用户可操作权限的K,V(module，button)数据结构
            Map<String, List> accountButtonMap = getAccountButtonMap(rightIdList, nodeMap);
            //按照用户权限，筛选出对应菜单
            List<TreeNode> userMenu = getUserMenu(rightIdList, nodeMap);
//            List<TreeNode> userMenu = getUserMenu(nodeMap);

            //处理存入redis的用户信息
            Map<String, Object> map = new HashMap<>();
            //用户的操作权限
            map.put("right",accountButtonMap);
            //用户的角色id
            map.put("role", rwe.getAccountRoles().stream().map(role -> {
                return role.getRoleId();
            }).collect(Collectors.toList()));
            //用户名
            map.put("accountName", rwe.getAccountName());
            //用户id
            map.put("account", rwe.getId());
            //用户所属组织
            map.put("orgIds",repo.findRepoByAccountId(rwe.getId()));
            JSONObject object = new JSONObject(map);

            //取sessionId为token，存session
            String sessionId = request.getSession().getId();
            boolean set = session.set(sessionId, object);
            if (!set) {
                return JsonResult.fail(BaseException.getRedisException());
            }

            List<Object> list = new ArrayList();
            list.add(sessionId);
            list.add(userMenu);
            list.add(accountButtonMap);
            return JsonResult.success(list);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value = "用户登出", tags = {"system"}, notes = "注意问题点")
    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public JsonResult logout(@RequestBody LoginBean bean) {
        try {
//            session.delete(session.getToken());
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }


    /**
     * 获取登录人权限
     * @param rwe
     * @return 登录人权限id集合
     */
    public List<Long> getUserRightIds(DbResAccount rwe) {
        //获取登录人角色
        List<Long> roleIds = rwe.getAccountRoles().stream().map(role -> {
            return role.getRoleId();
        }).collect(Collectors.toList());

        List<DbResRole> roles = roleRepository.findAllById(roleIds);

        //获取登录人权限id
        List<Long> rightId = new ArrayList();
        for (DbResRole role : roles) {
            for (DbResRoleRight right : role.getResRoleRightList()) {
                rightId.add(right.getRightId());
            }
        }
        //如果登录人的id中包含0（SMP最高权限）
        if(rightId.contains(0L)){
            return new ArrayList<Long>(Arrays.asList(0L));
        }
        //去重后的权限id
        return rightId.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 构建用户菜单
     * @param nodeMap
     * @return
     */
//    public List<TreeNode> getUserMenu(HashMap<Long, TreeNode> nodeMap) {
//        return nodeMap.get(0L).getChildren().stream().sorted((n1, n2) -> {
//            if (n1.getSortNo().longValue() >= n2.getSortNo().longValue()) {
//                return n1.getSortNo().compareTo(n2.getSortNo());
//            } else {
//                return n1.getSortNo().compareTo(n2.getSortNo());
//            }
//        }).collect(Collectors.toList());
//    }

    /**
     * 按照登录人权限构建树
     * @param rightIdList
     * @return
     */
//    public HashMap<Long, TreeNode> generateRightTree(List<Long> rightIdList) {
//        //根据rightId，递归查找父节点和子节点
//        List<Map<String,Object>> rightTreeList = rightRepository.findRightTreeByIds(rightIdList);
//
//        List<Map<String, Object>> maps = ResultRecode.returnHumpNameForList(rightTreeList);
//        List<DbResRight> rights = maps.stream().map(r -> {
//            return JSON.parseObject(JSON.toJSONString(r), DbResRight.class);
//        }).collect(Collectors.toList());
//
//        List<TreeNode> nodes = rights.stream().map(r -> {
//            DbResRight parentRight = rightRepository.findDbResRightById(r.getRightPid());
//            String moduleName = Objects.nonNull(parentRight) ? parentRight.getRightName() : null;
//            return new TreeNode(r.getId(), r.getRightPid(), r.getRightName(), moduleName, r.getRightUrl(), true, r.getRightType(), r.getRightIdentifier(), r.getSortNum(), new ArrayList<TreeNode>());
//        }).collect(Collectors.toList());
//        //构建map
//        Iterator<TreeNode> nodeIterator = nodes.iterator();
//        HashMap<Long, TreeNode> nodeMap = new HashMap<Long, TreeNode>();
//        while (nodeIterator.hasNext()) {
//            TreeNode node = nodeIterator.next();
//            nodeMap.put(node.getId(), node);
//        }
//
//        //标记非子结点，并收集子节点
//        Iterator<Long> iterator = nodeMap.keySet().iterator();
//        while (iterator.hasNext()) {
//            Long key = iterator.next();
//            TreeNode treeNode = nodeMap.get(key);
//            if (Objects.nonNull(treeNode.getParentId())) {
//                TreeNode parentNode = nodeMap.get(treeNode.getParentId());
//                parentNode.setLeaf(false);
//                List<TreeNode> children = parentNode.getChildren();
//                children.add(treeNode);
//            }
//
//        }
//        return nodeMap;
//    }

    /**
     * 构建用户可操作权限的K,V(module，button)数据结构
     *
     * @param rightIdList 登录人权限id
     * @param nodeMap     所有权限的树形结构
     * @return K, V(module ， button)
     */
    private Map<String, List> getAccountButtonMap(List<Long> rightIdList, HashMap<Long, TreeNode> nodeMap) {
        Map<String, List> accountButtonMap = new HashMap<>();
        HashMap<Long, TreeNode> map1 = new HashMap<>();
        map1.putAll(nodeMap);
        Iterator<Long> iterator = rightIdList.iterator();
        while (iterator.hasNext()) {
            Long key = iterator.next();
            TreeNode treeNode = map1.get(key);

            if (treeNode.getTpye().equals("Button")) {
                TreeNode parentTreeNode = map1.get(treeNode.getParentId());
                if (accountButtonMap.containsKey(parentTreeNode.getIdentifier())){
                    List list = accountButtonMap.get(parentTreeNode.getIdentifier());
                    List nList = new ArrayList(list);
                    nList.add(treeNode.getIdentifier());
                    accountButtonMap.put(parentTreeNode.getIdentifier(),nList);
                } else {
                    List list = new ArrayList();
                    list.add(treeNode.getIdentifier());
                    accountButtonMap.put(parentTreeNode.getIdentifier(),list);
                }
            } else if (treeNode.getTpye().equals("Menu")) {
                accountButtonMap.put(treeNode.getIdentifier(),Arrays.asList());
            } else if (treeNode.getTpye().equals("List")) {
                List<TreeNode> children = treeNode.getChildren();
                setListRight(accountButtonMap, children);
            }
        }
        return accountButtonMap;
    }

    /**
     *  递归寻找下级目录
     * @param accountButtonMap
     * @param children
     */
    private void setListRight(Map<String, List> accountButtonMap, List<TreeNode> children) {
        for (int i = 0; i < children.size(); i++) {
            if(children.get(i).getTpye().equals("Menu")){
                accountButtonMap.put(children.get(i).getIdentifier(), Arrays.asList());
            }else {
                setListRight(accountButtonMap,children.get(i).getChildren());
            }
        }
    }

    /**
     * 根据用户权限，筛选出对应的权限树
     *
     * @param rightIdList 登录人权限id
     * @param nodeMap     所有权限的树形结构
     * @return 对应权限的树
     */
    public List<TreeNode> getUserMenu(List<Long> rightIdList, HashMap<Long, TreeNode> nodeMap) {
        Iterator<Long> idIterator = rightIdList.iterator();
        Map<Long, TreeNode> accoutRightMap = new HashMap();
        //如果有SMP最大权限则直接返回最大权限
        if(rightIdList.contains(0L)){
            return nodeMap.get(0L).getChildren().stream().sorted((n1,n2)->{
                if(n1.getSortNo().longValue() >= n2.getSortNo().longValue()){
                    return n1.getSortNo().compareTo(n2.getSortNo());
                }else {
                    return n1.getSortNo().compareTo(n2.getSortNo());
                }
            }).collect(Collectors.toList());
        }
        //找到当前节点的父节点，并把当前节点放入父节点，并递归
        while (idIterator.hasNext()) {
            Long key = idIterator.next();
            TreeNode treeNode = nodeMap.get(key);
            Long parentId = treeNode.getParentId();
            if (treeNode.getTpye().equals("Button")) {//如果当前节点是Button，则从父节点开始递归
                TreeNode parentNode = nodeMap.get(parentId);
                parentNode.setChildren(Arrays.asList());
                findParent(accoutRightMap, nodeMap, parentNode, parentNode.getParentId());

            } else {
                if (treeNode.getTpye().equals("Menu")) {
                    treeNode.setChildren(Arrays.asList());
                }
                findParent(accoutRightMap, nodeMap, treeNode, parentId);
            }
        }
        //构建返回到前端的菜单树
        List<TreeNode> recode = accoutRightMap.values().stream().collect(Collectors.toList());
        List<TreeNode> userMenu = recode.get(0).getChildren();
        return userMenu.stream().sorted((n1,n2)->{
            if(n1.getSortNo().longValue() >= n2.getSortNo().longValue()){
                return n1.getSortNo().compareTo(n2.getSortNo());
            }else {
                return n1.getSortNo().compareTo(n2.getSortNo());
            }
        }).collect(Collectors.toList());
    }

    /**
     * 构建所有权限的树形结构
     *
     * @return
     */
    public HashMap<Long, TreeNode> getAllRightMap() {
        //查询所有权限
        List<DbResRight> allRight = rightRepository.findAll();
        //构建TreeNode数据结构
        List<TreeNode> nodes = allRight.stream().map(r -> {
            DbResRight parentRight = rightRepository.findDbResRightById(r.getRightPid());
            String moduleName = Objects.nonNull(parentRight) ? parentRight.getRightName() : null;
            return new TreeNode(r.getId(), r.getRightPid(), r.getRightName(), moduleName, r.getRightUrl(), true, r.getRightType(),r.getRightIdentifier(),r.getSortNum(), new ArrayList<TreeNode>());
        }).collect(Collectors.toList());

        //构建map
        Iterator<TreeNode> nodeIterator = nodes.iterator();
        HashMap<Long, TreeNode> nodeMap = new HashMap<Long, TreeNode>();
        while (nodeIterator.hasNext()) {
            TreeNode node = nodeIterator.next();
            nodeMap.put(node.getId(), node);
        }

        //标记非子结点，并收集子节点
        try {
            Iterator<Long> iterator = nodeMap.keySet().iterator();
            while (iterator.hasNext()) {
                Long key = iterator.next();
                TreeNode treeNode = nodeMap.get(key);
                if (Objects.nonNull(treeNode.getParentId())) {
                    TreeNode parentNode = nodeMap.get(treeNode.getParentId());
                    if (parentNode == null) {
                        log.error(treeNode.toString());
                        throw BaseException.getException("888","parent node not found");
                    }
                    parentNode.setLeaf(false);
                    List<TreeNode> children = parentNode.getChildren();
                    children.add(treeNode);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodeMap;
    }

    /**
     * 寻找父节点
     *
     * @param accoutRightMap 用户权限的map
     * @param nodeMap        所有权限的Map
     * @param treeNode       当前节点
     * @param parentId       父节点id
     */
    private void findParent(Map<Long, TreeNode> accoutRightMap, HashMap<Long, TreeNode> nodeMap, TreeNode treeNode, Long parentId) {
        if (nodeMap.containsKey(parentId)) {
            TreeNode parentNode = new TreeNode();
            if (accoutRightMap.containsKey(parentId)) {
                parentNode = accoutRightMap.get(parentId);
                List<TreeNode> children = parentNode.getChildren();

                List<Long> ids = children.stream().map(r -> {
                    return r.getId();
                }).collect(Collectors.toList());

                if (!ids.contains(treeNode.getId())) {
                    children.add(treeNode);
                }

            } else {
                parentNode = nodeMap.get(parentId);
                List list = new ArrayList();
                list.add(treeNode);
                parentNode.setChildren(list);
            }
            accoutRightMap.put(parentId, parentNode);
            Long pId = parentNode.getParentId();
            findParent(accoutRightMap, nodeMap, parentNode, pId);
        }
    }
}
