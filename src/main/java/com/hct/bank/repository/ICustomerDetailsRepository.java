package com.hct.bank.repository;

import com.hct.bank.model.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerDetailsRepository extends JpaRepository<CustomerDetails,Long> {

}
