package com.hct.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "acc_cust_map")
@AllArgsConstructor
@NoArgsConstructor
public class AccToCustMap {
    @Id
    @Column(name = "acc_id")
    private long accId;
    @Column(name = "cust_id",nullable = false)
    private long custId;


}
