package com.pccw.backend.repository;

import com.pccw.backend.entity.DbResAttrValue;
import com.pccw.backend.entity.DbResReservation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResReservationRepository extends BaseRepository<DbResReservation> {
}

