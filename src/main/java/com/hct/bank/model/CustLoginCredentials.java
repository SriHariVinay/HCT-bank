package com.hct.bank.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "cust_login_credentials")
public class CustLoginCredentials {
    @Id
    @Column(name = "cust_id")
    private long custId;
    @Column(nullable = false)
    private String password;
}
