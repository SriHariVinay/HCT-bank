package com.hct.bank.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "acc_balance")
public class AccBalance {
    @Id
    @Column(name = "acc_id")
    private long accId;
    private double balance;
}
