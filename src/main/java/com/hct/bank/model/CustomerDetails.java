package com.hct.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class CustomerDetails {
    @Id
    @Column(name = "cust_id")
    private long custId;
    @Column(nullable = false)
    private String name;
    @Column(name = "address_id")
    private Long addressId;
    @Column(nullable = false)
    private long phone;
    @Column(nullable = false)
    private String email;
    private Timestamp created;
    private Timestamp lastUpdated;

}
