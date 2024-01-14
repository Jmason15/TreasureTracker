package com.finance.treasuretracker.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Dropdown")
public class Dropdown {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dropdownId;

    private String display;
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "dropdownType", referencedColumnName = "dropdownTypeId")
    private DropdownType dropdownType;

    // Getters and setters
}
