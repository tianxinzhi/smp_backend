package com.pccw.backend.ctrl;


import com.pccw.backend.bean.*;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.Base;
import com.pccw.backend.entity.DbResAccount;
import com.pccw.backend.exception.BaseException;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResAccountRepository;
import com.pccw.backend.util.Convertor;
import com.pccw.backend.util.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * BaseCtrl
 * BaseCtrl includs CURD
 */

@Slf4j
public class BaseCtrl<T>{

    @Autowired
    Session<Map> session;

    @Autowired
    ResAccountRepository accountRepository;

    public <G extends BaseSearchBean> JsonResult search(BaseRepository repo, G b) {
        try {
            log.info(b.toString());
            Specification<T> spec = Convertor.<T>convertSpecification(b);
            long count = repo.count(spec);
            List<T> res =repo.findAll(spec,PageRequest.of(b.getPageIndex(),b.getPageSize())).getContent();
            for (T re : res) {
                Base base = (Base)re;
                base.setCreateAccountName(getAccountName(base.getCreateBy()));
                base.setUpdateAccountName(getAccountName(base.getUpdateBy()));
            }
            return JsonResult.success(res,count);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

    public JsonResult delete(BaseRepository repo, BaseDeleteBean ids){
        try {
            repo.deleteByIdIn(ids.getIds());

            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }


    public JsonResult create(BaseRepository repo, Class<T> cls, BaseBean b) {
        try {
            long t = new Date().getTime();
            b.setCreateAt(t);
            b.setUpdateAt(t);
            b.setActive("Y");
            b.setCreateBy(getAccount());
            b.setUpdateBy(getAccount());
            saveAndFlush(repo, cls, b);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    public JsonResult edit(BaseRepository repo, Class<T> cls, BaseBean b){
        try {
            b.setUpdateAt(new Date().getTime());
            b.setActive("Y");
            b.setUpdateBy(getAccount());
            saveAndFlush(repo, cls, b);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }
    private void saveAndFlush(BaseRepository repo, Class<T> cls, BaseBean b) throws Exception {
        try {
            T entity = cls.newInstance();
            BeanUtils.copyProperties(b, entity);
            log.info(entity.toString());
            repo.<T>saveAndFlush(entity);
        } catch (Exception e) {
            throw e;
        }
    }

    public JsonResult disable(BaseRepository repo, BaseDeleteBean ids, Class<?> cl, BaseRepository... checks) {
        try {

            ICheck check = (ICheck) cl.newInstance();
            long id = check.checkCanDisable(ids,checks);
            if(id>0){
                return JsonResult.fail(BaseException.getDataUsedException(id));
            }
            for (Long priKey : ids.getIds()) {
                Base base = (Base)repo.findById(priKey).get();
                base.setActive("N");
                base.setUpdateAt(System.currentTimeMillis());
                base.setUpdateBy(getAccount());
                repo.saveAndFlush(base);
            }
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    public JsonResult enable(BaseRepository repo, BaseDeleteBean ids) {
        try {
            for (Long priKey : ids.getIds()) {
                Base base = (Base)repo.findById(priKey).get();
                base.setActive("Y");
                base.setUpdateAt(System.currentTimeMillis());
                base.setUpdateBy(getAccount());
                repo.saveAndFlush(base);
            }
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail(e);
        }
    }

    long getAccount() {
        Map user = session.getUser();
        return Long.parseLong(user.get("account").toString());
    }

    String getAccountName(long account) {
        String name = "";
        if(account==0){
            name = "System";
        }
        DbResAccount accountName = accountRepository.findDbResAccountById(account);
        if(accountName != null){
            name = accountName.getAccountName();
        }
        return name;
    }

    /**
     * 将默认查询结果集按照指定bean格式,并装入JsonResult返回
     * @param repo repository对象
     * @param bean 指定的bean对象
     * @param <E>
     * @return
     */
    public <E> JsonResult JsonResultHandle(BaseRepository repo, GeneralBean bean){
        try {
            List<GeneralBean> res = getDefualtSearchBeans(repo, bean);
            return JsonResult.success(res);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    /**
     * 将默认查询结果集按照指定bean格式,并装入JsonResult，并在末尾插入一个bean
     * @param repo repository对象
     * @param bean 插入的bean对象
     * @param <E>
     * @return
     */
    public <E> JsonResult addRowJsonResultHandle(BaseRepository repo, GeneralBean bean){
        try {
            List<GeneralBean> res = getDefualtSearchBeans(repo, bean);
            res.add(bean);
            return JsonResult.success(res);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    /**
     * 将自定义查询的结果集按照指定bean格式,并装入JsonResult
     * @param bean
     * @param list
     * @param <E>
     * @return
     */
    public <E> JsonResult customSearchJsonResultHandle(GeneralBean bean, List<E> list){
        try {
            List<GeneralBean> res = Convertor.getCollect(bean, list);
            return JsonResult.success(res);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    /**
     * 默认查询全部的数据结果集
     * @param repo repository对象
     * @param bean 指定的bean对象
     * @param <E>
     * @return
     */
    private <E> List<GeneralBean> getDefualtSearchBeans(BaseRepository repo, GeneralBean bean) throws IllegalAccessException {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<E> spec = new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("active").as(String.class), "Y");
                return predicate;
            }
        };
        List<E> list = repo.findAll(spec,sort);
//        List<E> list = repo.findAll();
        return Convertor.getCollect(bean, list);
    }

    /**
     * 使用JsonResultParamMapPro对结果进行处理
     * @param repo
     * @param <E>
     * @return
     */
    public <E> JsonResult JsonResultHandle(BaseRepository repo){
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Specification<E> spec = new Specification<E>() {
            @Override
            public Predicate toPredicate(Root<E> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("active").as(String.class), "Y");
                return predicate;
            }
        };
        List<E> list = repo.findAll(spec,sort);
        List<Map> maps = Convertor.entityTransfer(list);
        return JsonResult.success(maps);
    }

    public <E> JsonResult addRowJsonResultHandle(BaseRepository repo,Map map){
        JsonResult result = this.JsonResultHandle(repo);
        result.getData().add(map);
        return result;
    }
}
