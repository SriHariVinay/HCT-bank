package com.hct.bank.repository;

import com.hct.bank.model.AccToCustMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface IAccToCustMapRepository extends JpaRepository<AccToCustMap,Long> {
    @Query("select accId from AccToCustMap where custId = ?1")
    public long findAccIdByCustId(Long custId);
}
