package com.finance.treasuretracker.model;


import jakarta.persistence.*;

@Entity
public class DropdownType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dropdownTypeId;
    private String typeName;

    // Constructors, getters, and setters
}

