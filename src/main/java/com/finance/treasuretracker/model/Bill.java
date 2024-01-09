package com.finance.treasuretracker.model;



import jakarta.persistence.*;

@Entity
@Table(name = "Bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer billId;

    private String name;
    private Integer dueDay;
    private Double amount;
    private Double alternate;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accounId")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "alternateAccount", referencedColumnName = "accounId")
    private Account alternateAccount;

    @ManyToOne
    @JoinColumn(name = "Frequency", referencedColumnName = "dropdownId")
    private Dropdown frequency;

    // Getters and setters
}

