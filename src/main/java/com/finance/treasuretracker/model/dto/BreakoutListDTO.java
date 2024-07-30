package com.finance.treasuretracker.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BreakoutListDTO {
    private String desctiption;
    private BigDecimal totalYear;
    private BigDecimal totalMonth;
    private BigDecimal remYear;
    private BigDecimal remMonth;
}
