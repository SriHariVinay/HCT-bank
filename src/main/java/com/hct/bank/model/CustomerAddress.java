package com.hct.bank.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "cust_address")
public class CustomerAddress {
    @Id
    @Column(name = "address_id")
    private long addressId;
    private String country;
    private String city;
    @Column(name = "address_lane")
    private String addressLane;
    private long pin;

    @Column(name = "last_updated",nullable = false)
    private Timestamp lastUpdated;

}
