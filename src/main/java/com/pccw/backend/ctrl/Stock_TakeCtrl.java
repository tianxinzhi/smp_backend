package com.pccw.backend.ctrl;

import com.pccw.backend.annotation.NoAuthorized;
import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.ResultRecode;
import com.pccw.backend.bean.stock_take.*;
import com.pccw.backend.entity.DbResRepo;
import com.pccw.backend.entity.DbResSku;
import com.pccw.backend.entity.DbResStockTake;
import com.pccw.backend.entity.DbResStockTakeDtl;
import com.pccw.backend.repository.ResRepoRepository;
import com.pccw.backend.repository.ResSkuRepoRepository;
import com.pccw.backend.repository.ResSkuRepository;
import com.pccw.backend.repository.ResStockTakeRepository;
import lombok.extern.slf4j.Slf4j;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: ChenShuCheng
 * @create: 2020-07-30 10:54
 **/
@Slf4j
@RestController
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
@RequestMapping("/stock_take")
public class Stock_TakeCtrl extends BaseCtrl<DbResStockTake>{

    @Autowired
    ResStockTakeRepository stockTakeRepository;

    @Autowired
    ResSkuRepoRepository skuRepoRepository;

    @Autowired
    ResRepoRepository repoRepository;

    @Autowired
    ResSkuRepository skuRepository;

    @Autowired
    HttpServletResponse response;

    private String excelPath =  "excel/stock_take.xls";

    @RequestMapping(method = RequestMethod.POST,path = "/search")
    public JsonResult search(@RequestBody SearchBean b){

        try {
            log.info(b.toString());
            JsonResult<DbResStockTake> jsonResult = this.search(stockTakeRepository, b);
            List<SearchVO> result = jsonResult.getData().stream().map(entity -> {
                SearchVO searchVO = new SearchVO();
                BeanUtils.copyProperties(entity, searchVO);
                DbResRepo repo = repoRepository.findById(entity.getChannelId()).get();
                searchVO.setChannelCode(repo.getRepoCode());
                return searchVO;
            }).collect(Collectors.toList());
            result = result.stream().sorted(Comparator.comparing(SearchVO::getUpdateAt).reversed()).collect(Collectors.toList());
            return JsonResult.success(result);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST,path = "/searchSkuQty")
    public JsonResult searchSkuQty(@Validated @RequestBody SearchQtyBean b){

        try {
            List<Map<String,Object>> currentQty = skuRepoRepository.findCurrentQty(b.getChannelId(), b.getSkuIds(),3L);
            List<Map<String, Object>> result = ResultRecode.returnHumpNameForList(currentQty);
            return JsonResult.success(result);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }


    @RequestMapping(method = RequestMethod.POST,path = "/create")
    public JsonResult create(@RequestBody CreateBean b){

        try {
           return this.create(stockTakeRepository,DbResStockTake.class,b);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST,path = "/edit")
    public JsonResult edit(@RequestBody EditBean b){

        try {
            return this.edit(stockTakeRepository,DbResStockTake.class,b);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @RequestMapping(method = RequestMethod.POST,path = "/searchSkuByRepoId")
    public JsonResult findSkuByRepoId(@RequestBody SearchQtyBean b){

        try {
            List<Map<String,Object>> currentQty = skuRepoRepository.findSkuByRepoId(b.getChannelId(),3L);
            List<Map<String, Object>> result = ResultRecode.returnHumpNameForList(currentQty);
            return JsonResult.success(result);
        } catch (Exception e) {
            return JsonResult.fail(e);
        }
    }

    @RequestMapping(value = "/print",method = RequestMethod.GET)
    @NoAuthorized
    public JsonResult print(Long id) throws Exception{
        DbResStockTake stockTake = stockTakeRepository.findById(id).get();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map map = new HashMap<>();
        map.put("number",stockTake.getStockTakeNumber());
        map.put("channel",repoRepository.findById(stockTake.getChannelId()).get().getRepoCode());
        map.put("time",stockTake.getCompleteTime() == null ?sdf.format(stockTake.getUpdateAt()):sdf.format(stockTake.getCompleteTime()));
        map.put("display",stockTake.getDisplayQuantity());
        String skuCode = "";
        List<Map> dtls = new LinkedList<>();
        for (DbResStockTakeDtl dtl : stockTake.getLine()) {
            Map kMap = new HashMap();
            DbResSku sku = skuRepository.findById(dtl.getSkuId()).get();
            skuCode += sku.getSkuCode()+",";
            kMap.put("sku",sku.getSkuCode()==null?"":sku.getSkuCode());
            kMap.put("one",dtl.getStockTakeOne()==null?"":dtl.getStockTakeOne());
            kMap.put("two",dtl.getStockTakeTwo()==null?"":dtl.getStockTakeTwo());
            kMap.put("three",dtl.getStockTakeThree()==null?"":dtl.getStockTakeThree());
            kMap.put("balance",dtl.getStockTakeBalance()==null?"":dtl.getStockTakeBalance());
            kMap.put("currentBalance",dtl.getCurrentBalance()==null?"":dtl.getCurrentBalance());
            kMap.put("difference",dtl.getDifference()==null?"":dtl.getDifference());
            dtls.add(kMap);
        }
        map.put("sku",skuCode);
        map.put("list",dtls);

        System.out.println("导出前查询list:=====================");
        System.out.println(map.toString());

        InputStream in = new ClassPathResource(excelPath).getInputStream();
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = URLEncoder.encode("stock_take"+sdf.format(new Date())+".xls", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename="+filename);
        /*response.setContentType("application/ms-excel;charset=UTF-8");*/
        response.setContentType("application/vnd..ms-excel;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        Context context = new Context();
        context.putVar("param",map);

//        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
//        Transformer transformer = jxlsHelper.createTransformer(in, out);
//        jxlsHelper.processTemplate(context, transformer);
        JxlsHelper.getInstance().processTemplate(in, out, context);
        System.out.println("导出完毕！");
        return JsonResult.success(Arrays.asList());
    }

}
