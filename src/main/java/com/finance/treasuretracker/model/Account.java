package com.finance.treasuretracker.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @ManyToOne
    @JoinColumn(name = "bankid", referencedColumnName = "bankId")
    private Bank bank;

    @ManyToOne
    @JoinColumn(name = "typeId", referencedColumnName = "dropdownId")
    private Dropdown type;

    private Boolean credit;

    // Getters and setters
}

