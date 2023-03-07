package com.hct.bank.repository;

import com.hct.bank.model.CustomerAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomerAddressRepository extends JpaRepository<CustomerAddress,Long> {
}
