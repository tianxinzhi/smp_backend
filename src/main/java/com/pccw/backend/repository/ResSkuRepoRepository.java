package com.pccw.backend.repository;


import com.pccw.backend.entity.DbResRepo;
import com.pccw.backend.entity.DbResSku;
import com.pccw.backend.entity.DbResSkuRepo;
import com.pccw.backend.entity.DbResStockType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface  ResSkuRepoRepository extends BaseRepository<DbResSkuRepo>{

    @Query(value = "select ROWNUM id, a.* from (select RS.SKU_DESC description,RS.SKU_CODE sku,RR.REPO_CODE AS store,\n" +
            "sum(coalesce(case when RST.STOCKTYPE_NAME='Intransit(DEL)' then RSR.QTY end,0)) DELQTY,\n" +
            "sum(coalesce(case when RST.STOCKTYPE_NAME='Faulty(FAU)' then RSR.QTY end,0))    FAUQTY,\n" +
            "sum(coalesce(case when RST.STOCKTYPE_NAME='Reserved(RES)' then RSR.QTY end,0))  RESQTY,\n" +
            "sum(coalesce(case when RST.STOCKTYPE_NAME='Available(AVL)' then RSR.QTY end,0)) AVLQTY,\n" +
            "sum(coalesce(case when RST.STOCKTYPE_NAME='Demo(DEM)' then RSR.QTY end,0))      DEMQTY,\n" +
            "sum(coalesce(case when RST.STOCKTYPE_NAME='Reserved With AO(RAO)' then RSR.QT end,0)) RAOQTY,\n" +
            "sum(coalesce(case when RST.STOCKTYPE_NAME='Reserved With Remote(RRO)' then RSR.QTY end,0)) RROQTY\n" +
            "FROM   \n" +
            "  RES_SKU RS,\n" +
            "  RES_REPO RR,\n" +
            "\tRES_STOCK_TYPE RST,\n" +
            "\tRES_SKU_REPO RSR\n" +
            "WHERE\n" +
            "   RS.ID = RSR.SKU_ID\n" +
            "   AND RR.ID = RSR.REPO_ID\n" +
            "   AND RST.ID = RSR.STOCK_TYPE_ID\n" +
            "\t AND RS.sku_code LIKE CONCAT(CONCAT('%',:skuNum),'%')\n" +
            "AND RSR.REPO_ID=coalesce(:repoNum,RSR.REPO_ID)\n"+
            "GROUP BY RS.SKU_CODE,RR.REPO_CODE,RS.SKU_DESC) a",nativeQuery = true)
    List<Map> getStockBalanceInfo(@Param("skuNum") String skuNum, @Param("repoNum") String repoNum);

    @Query(value = "select * from res_sku_repo t where t.repo_id = ?1 and t.sku_id = ?2 and t.stock_type_id = ?3",nativeQuery = true)
    DbResSkuRepo findQtyByRepoAndShopAndType(@Param("shop") long shop, @Param("sku") long sku, @Param("stockType") long stockType);

    @Query(value = "select SUM(t.QTY) qty from res_sku_repo t where t.repo_id = coalesce(?1,t.repo_id) and t.sku_id = ?2 and t.item_id = nvl(?3,t.item_id) and t.stock_type_id = ?4",nativeQuery = true)
    Long findQtyByRepoAndSkuAndItemAndType(@Param("shop") long shop, @Param("sku") long sku,@Param("item") long item, @Param("stockType") long stockType);

    @Query(value = "select SUM(t.QTY) qty from res_sku_repo t where t.repo_id = coalesce(?1,t.repo_id) and t.sku_id = ?2 and t.stock_type_id = ?3",nativeQuery = true)
    Long findQtyByRepoAndSkuAndType(@Param("shop") long shop, @Param("sku") long sku, @Param("stockType") long stockType);

//    @Query(value = "select  t.qty from res_sku_repo t where  t.sku_id = ?1 and t.stock_type_id = ?2 and t.repo_id is null ",nativeQuery = true)
//    Long findQtyBySkuAndType(@Param("sku") long sku, @Param("stockType") long stockType);
//
//    @Query(value = "select * from res_sku_repo t where t.repo_id is null and t.sku_id = ?1 and t.stock_type_id = ?2",nativeQuery = true)
//    DbResSkuRepo findEntityBySkuAndType( @Param("sku") long sku, @Param("stockType") long stockType);

    @Modifying
    @Query(value = "update res_sku_repo set qty = qty + ?4 where repo_id = ?1 and sku_id = ?2 and stock_type_id = ?3",nativeQuery = true)
    int updateQtyByRepoAndShopAndTypeAndQty(@Param("shop") long shop, @Param("sku") long sku, @Param("stockType") long stockType, @Param("qty") long qty);


    /**
     * @param id
     * @param repoType
     * repoTo类型是商店，查询stock_type为Available(AVL) ；repoTo类型是仓库，查询所有；
     * @return
     */
//    @Query(value = "select rs.id  dtl_Sku_Id, rs.sku_code  sku_Code from res_sku rs \n" +
//            "where exists \n" +
//            "    (select * from res_sku_repo  rsr\n" +
//            "       where rsr.repo_id= ?1 and  rsr.sku_id=rs.id\n" +
//            "        and rsr.stock_type_id =\n" +
//            "         decode(    ?2 \n" +
//            "                   ,'S' \n" +
//            "                   ,3\n" +
//            "                   ,rsr.stock_type_id ) )",nativeQuery = true)
    @Query(value = "select rs.id  dtl_Sku_Id, rs.sku_code  sku_Code from res_sku rs \n" +
            "where exists \n" +
            "    (select * from res_sku_repo  rsr\n" +
            "       where rsr.repo_id= ?1 and  rsr.sku_id=rs.id\n" +
            "        and rsr.stock_type_id =\n" +
            "         (case when ?2='S' then 3 \n" +
            "      else  rsr.stock_type_id  end) )",nativeQuery = true)
    List<Map<String, Object>> findByTypeIdAndRepoId(@Param("idFrom") Long id, @Param("repoType") String repoType);

    List<DbResSkuRepo> findDbResSkuRepoByRepo(DbResRepo repo);

    List<DbResSkuRepo> findDbResSkuRepoByRepoAndSku(DbResRepo repo, DbResSku sku);

    DbResSkuRepo findDbResSkuRepoByRepoAndSkuAndStockType(DbResRepo repo, DbResSku sku, DbResStockType stockType);

    List<DbResSkuRepo> getDbResSkuReposBySku(DbResSku sku);

    List<DbResSkuRepo> getDbResSkuReposByStockType(DbResStockType stockType);

    List<DbResSkuRepo> getDbResSkuReposByRepo(DbResRepo repo);

    @Query(value = "select REPO.id, s.sku_code sku,s.sku_desc skuDesc, t.stocktype_name stockType,r.repo_code shop,REPO.qty ,t4.qty resQty\n" +
            ",r.id shopId,t.id stockTypeId,s.ID skuId from RES_SKU_REPO repo inner join\n" +
            "res_sku s on REPO.SKU_ID = s.id\n" +
            "INNER JOIN RES_STOCK_TYPE t\n" +
            "on repo.stock_type_id = t.id\n" +
            "inner join RES_REPO r on  repo.repo_id = r.id \n" +
            "inner join (select sku_id,repo_id,qty from res_sku_repo where stock_type_id=4)t4\n" +
            "on repo.repo_id = t4.repo_id and repo.sku_id = t4.sku_id\n" +
            "where repo.stock_type_id<>4\n" +
            "order by r.repo_code desc, s.sku_code desc ,repo.id desc" ,nativeQuery = true)
    List<Map> getBalanceQty();
        //where if(:sku,1=1,s.SKU_CODE like CONCAT('%',:sku,'%'))  and if(:stockType,2=2,t.id = :stockType) and if(:shop,3=3,r.id = :shop)
//    List<Map> getBalanceQty(@Param("sku")String sku, @Param("stockType")long stockType, @Param("shop")long shop);

    @Query(value = "SELECT\n" +
            "\tSKU_ID as sku_id,\n" +
            "\tQTY as current_balance\n" +
            "FROM\n" +
            "\tRES_SKU_REPO\n" +
            "WHERE\n" +
            "\tREPO_ID = ?1\n" +
            "AND SKU_ID IN ?2\n" +
            "AND STOCK_TYPE_ID = ?3",nativeQuery = true)
    List<Map<String,Object>> findCurrentQty(Long channelId,List<Long> skuIds,Long stockTpyeId);

    @Query(value = "SELECT\n" +
            "\tRSR.SKU_ID,\n" +
            "\tRS.SKU_CODE\n" +
            "FROM\n" +
            "\tRES_SKU_REPO rsr,\n" +
            "\tRES_SKU rs\n" +
            "WHERE\n" +
            "\trsr.REPO_ID = ?1\n" +
            "AND rsr.STOCK_TYPE_ID = ?2\n" +
            "AND rs.id = RSR.SKU_ID",nativeQuery = true)
    List<Map<String,Object>> findSkuByRepoId(Long channelId,Long stockTpyeId);
}
