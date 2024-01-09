package com.finance.treasuretracker.model;
import jakarta.persistence.*;


@Entity
@Table(name = "Bank")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bankId;

    private String name;
}
