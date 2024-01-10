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
@Table(name = "TransactionRecord")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transactionId;

    @Temporal(TemporalType.DATE)
    private Date date;

    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "accounId")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "billId", referencedColumnName = "BillId")
    private Bill bill;

    private Boolean paid;

    // Getters and setters
}
