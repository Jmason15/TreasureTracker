package com.finance.treasuretracker.controller;

import com.finance.treasuretracker.model.dto.SummaryViewDTO;
import com.finance.treasuretracker.model.dto.SummaryViewInterface;
import com.finance.treasuretracker.service.SummaryServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/summaryInterface")
public class SummaryController {

private final SummaryServiceInterface summaryServiceInterface;
    public SummaryController(SummaryServiceInterface summaryServiceInterface) {
        this.summaryServiceInterface = summaryServiceInterface;
    }

    public List<SummaryViewDTO> getAllSummaryView(){
        return summaryServiceInterface.findAllSummary();
    }
}
