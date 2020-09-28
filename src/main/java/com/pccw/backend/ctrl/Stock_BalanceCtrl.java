package com.pccw.backend.ctrl;


import com.pccw.backend.bean.JsonResult;
import com.pccw.backend.bean.ResultRecode;
import com.pccw.backend.bean.stock_balance.SearchBean;
import com.pccw.backend.repository.ResSkuRepoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/stock_balance")
@CrossOrigin(methods = RequestMethod.POST,origins = "*", allowCredentials = "false")
public class Stock_BalanceCtrl {

	 @Autowired
	 private ResSkuRepoRepository repo;


	// /**
	//  * 
	//  * @param sc
	//  * @return
	//  * @throws IllegalArgumentException
	//  * @throws IllegalAccessException
	//  */
	// @RequestMapping(method=RequestMethod.POST,path="/search")
	// public JsonResult<DbResSkuRepo> test(@RequestBody SearchCondition sc)
	//  {
	//     try {
	// 		Specification<DbResSkuRepo> spec = Convertor.<DbResSkuRepo,SearchCondition>convertSpecification(SearchCondition.class,sc);
	// 		List<DbResSkuRepo> res =repo.findAll(spec,PageRequest.of(sc.getPageIndex(),sc.getPageSize())).getContent();

	// 		// List<DbResRepo> r = repoTest.findAll();
	// 		// log.info(r.toString());

	// 		return JsonResult.success(res);
	// 	} catch (Exception e) {
	// 		// log.error(e, t);
	// 		return JsonResult.fail();
	// 	}
	// }

//    @RequestMapping(method=RequestMethod.POST,path="/search")
//     public JsonResult<Map> test(@RequestBody SearchBean sc)
//      {
//         try {
//             log.info(sc.toString());
//             String repoNum = Objects.isNull(sc.getRepoNum()) ? "" : sc.getRepoNum();
//             String skuNum = Objects.isNull(sc.getSkuNum()) ? "" : sc.getSkuNum();
//             List<Map> res = repo.getStockBalanceInfo(skuNum,repoNum);
//             List<Map> result = ResultRecode.returnResult(res);
//             return JsonResult.success(result);
//     	} catch (Exception e) {
//     		// log.error(e, t);
//     		return JsonResult.fail(e);
//     	}
//     }
}
