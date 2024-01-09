package com.finance.treasuretracker.model;
import jakarta.persistence.*;
@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accounId;

    @ManyToOne
    @JoinColumn(name = "bankid", referencedColumnName = "bankId")
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "typeId", referencedColumnName = "dropdownId")
    private Dropdown type;

    private Boolean credit;

    // Getters and setters
}

