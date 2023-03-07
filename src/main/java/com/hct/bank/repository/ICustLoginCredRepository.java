package com.hct.bank.repository;

import com.hct.bank.model.CustLoginCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustLoginCredRepository extends JpaRepository<CustLoginCredentials,Long> {
}
