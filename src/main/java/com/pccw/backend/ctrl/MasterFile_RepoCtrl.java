package com.pccw.backend.ctrl;


import com.pccw.backend.bean.BaseDeleteBean;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.masterfile_repo.CreateBean;
import com.pccw.backend.bean.masterfile_repo.EditBean;
import com.pccw.backend.bean.masterfile_repo.SearchBean;
import com.pccw.backend.cusinterface.ICheck;
import com.pccw.backend.entity.DbResRepo;
import com.pccw.backend.entity.DbResSkuRepo;
import com.pccw.backend.repository.BaseRepository;
import com.pccw.backend.repository.ResRepoRepository;
import com.pccw.backend.repository.ResSkuRepoRepository;
import com.pccw.backend.util.Convertor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * MF_RepoCtrl
 */

@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/masterfile_repo")
@Api(value="MasterFile_RepoCtrl",tags={"masterfile_repo"})
public class MasterFile_RepoCtrl extends BaseCtrl<DbResRepo> implements ICheck {

    @Autowired
    ResRepoRepository repo;

    @Autowired
    ResSkuRepoRepository skuRepoRepository;

    @ApiOperation(value="查询shop",tags={"masterfile_repo"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/search")
    public JsonResult search(@RequestBody SearchBean b) {
        log.info(b.toString());
        //return this.search(repo,  b);
        try {
            Specification specification = Convertor.convertSpecification(b);
            List<DbResRepo> res =repo.findAll(specification, PageRequest.of(b.getPageIndex(),b.getPageSize())).getContent();
            ArrayList<com.pccw.backend.bean.masterfile_repo.SearchBean> dbResRepo = new ArrayList<>();
            if(res != null && res.size() > 0){
                for (DbResRepo resRepo:res){
                    SearchBean searchBean = new SearchBean();
                    BeanUtils.copyProperties(resRepo, searchBean);
                    searchBean.setCreateAccountName(getAccountName(resRepo.getCreateBy()));
                    searchBean.setUpdateAccountName(getAccountName(resRepo.getUpdateBy()));
                    if(Objects.nonNull(resRepo.getParentRepoId())&& resRepo.getParentRepoId() > 0 ){
                        Optional<DbResRepo> optional = repo.findById(resRepo.getParentRepoId());
                        DbResRepo parent = optional.get();
                        searchBean.setParentRepo(parent.getRepoName());
                    }
                    dbResRepo.add(searchBean);
                }
            }
            return JsonResult.success(dbResRepo,repo.count(specification));
        } catch (Exception e) {
            log.info(e.getMessage());
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="删除shop",tags={"masterfile_repo"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path = "/delete")
    public JsonResult delete(@RequestBody BaseDeleteBean ids){
        return this.delete(repo,ids);
    }

    @ApiOperation(value="创建shop",tags={"masterfile_repo"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/create")
    public JsonResult create(@RequestBody CreateBean b){
        try {
            if(Objects.isNull(b.getIsClosed())){
               b.setIsClosed("N");
            }
            long t = new Date().getTime();
            b.setCreateAt(t);
            b.setCreateBy(getAccount());
            b.setUpdateAt(t);
            b.setUpdateBy(getAccount());
            b.setActive("Y");
            DbResRepo resRepo=new DbResRepo();
            BeanUtils.copyProperties(b, resRepo);
            if(Objects.nonNull(b.getClosedDay())){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                long time = df.parse(b.getClosedDay()).getTime();
                resRepo.setClosedDay(time);
            }
            repo.saveAndFlush(resRepo);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="编辑shop",tags={"masterfile_repo"},notes="说明")
    @RequestMapping(method = RequestMethod.POST,path="/edit")
    public JsonResult edit(@RequestBody EditBean b){
        try {
            long t = new Date().getTime();
            b.setUpdateAt(t);
            b.setUpdateBy(getAccount());
            Optional<DbResRepo> optional = repo.findById(b.getId());
            DbResRepo dbResRepo = optional.get();
            b.setActive(dbResRepo.getActive());
            b.setCreateAt(dbResRepo.getCreateAt());
            b.setCreateBy(dbResRepo.getCreateBy());
            b.setParentRepoId(dbResRepo.getParentRepoId());
            DbResRepo resRepo=new DbResRepo();
            BeanUtils.copyProperties(b, resRepo);
            if(Objects.nonNull(b.getClosedDay())){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                long time = df.parse(b.getClosedDay()).getTime();
                resRepo.setClosedDay(time);
            }
            repo.saveAndFlush(resRepo);
            return JsonResult.success(Arrays.asList());
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @ApiOperation(value="禁用repo",tags={"masterfile_repo"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/disable")
    public JsonResult disable(@RequestBody BaseDeleteBean ids) {
        return this.disable(repo,ids, MasterFile_RepoCtrl.class,skuRepoRepository);
    }

    @ApiOperation(value="启用repo",tags={"masterfile_repo"},notes="注意问题点")
    @RequestMapping(method = RequestMethod.POST,value = "/enable")
    public JsonResult enable(@RequestBody BaseDeleteBean ids) {
        return this.enable(repo,ids);
    }

    @Override
    public long checkCanDisable(Object obj, BaseRepository... check) {
        ResSkuRepoRepository tRepo = (ResSkuRepoRepository)check[0];
        BaseDeleteBean bean = (BaseDeleteBean)obj;
        for (Long id : bean.getIds()) {
            DbResRepo repo = new DbResRepo();
            repo.setId(id);
            List<DbResSkuRepo> skuRepos = tRepo.getDbResSkuReposByRepo(repo);
            if ( skuRepos != null && skuRepos.size()>0 ) {
                return id;
            }
        }
        return 0;
    }
}
