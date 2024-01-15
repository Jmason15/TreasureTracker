package com.finance.treasuretracker.model;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    private String name;
    private Integer dueDay;
    private Double amount;
    private Double alternate;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accountId")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "alternateAccount", referencedColumnName = "accountId")
    private Account alternateAccount;

    @ManyToOne
    @JoinColumn(name = "Frequency", referencedColumnName = "dropdownId")
    private Dropdown frequency;

    // Getters and setters
}

