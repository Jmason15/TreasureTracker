package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.dto.SummaryViewInterface;

import java.util.List;

public interface SummaryServiceInterface {
    List<SummaryViewInterface> findAllSummary();
}
