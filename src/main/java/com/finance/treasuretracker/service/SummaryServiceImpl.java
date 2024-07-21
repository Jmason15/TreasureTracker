package com.finance.treasuretracker.service;

import com.finance.treasuretracker.model.dto.SummaryViewInterface;
import com.finance.treasuretracker.model.repository.SummaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SummaryServiceImpl implements SummaryServiceInterface{
    private final SummaryRepository summaryRepository;
    public SummaryServiceImpl(SummaryRepository summaryRepository) {
        this.summaryRepository = summaryRepository;
    }

    @Override
    public List<SummaryViewInterface> findAllSummary() {
        return summaryRepository.findAllSummary();
    }
}
