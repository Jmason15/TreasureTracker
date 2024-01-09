package com.finance.treasuretracker.model;





import jakarta.persistence.*;
import java.util.Date;

@Entity
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
