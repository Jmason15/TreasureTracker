package com.finance.treasuretracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BankRecord")
public class BankRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bankRecordId;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    private Account account;

    @Temporal(TemporalType.DATE)
    private Date date;

    private Double amount;

    // Getters and setters
}

