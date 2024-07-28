package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.dto.SummaryViewDTO;
import com.finance.treasuretracker.model.dto.SummaryViewInterface;

import java.util.List;

public interface SummaryServiceInterface {
    List<SummaryViewDTO> findAllSummary();
}
