package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.dto.BreakoutListDTO;
import com.finance.treasuretracker.model.dto.SummaryViewDTO;

import java.util.List;

public interface SummaryServiceInterface {
    List<SummaryViewDTO> findAllSummary();

    List<BreakoutListDTO> getBreakoutList();
}
