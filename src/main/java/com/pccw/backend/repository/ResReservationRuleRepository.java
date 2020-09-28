package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResReservationRule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResReservationRuleRepository extends BaseRepository<DbResReservationRule> {
    List<DbResReservationRule> getDbResReservationRulesBySkuIdAndPaymentStatusAndCustomerType(Long skuId,String paymentStatus,String customerType);
}

