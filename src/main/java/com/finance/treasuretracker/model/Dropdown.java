package com.finance.treasuretracker.model;
import jakarta.persistence.*;

@Entity
@Table(name = "Dropdown")
public class Dropdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dropdownId;

    private String display;
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "dropdownType", referencedColumnName = "dropdownTypeId")
    private DropdownType dropdownType;

    // Getters and setters
}
