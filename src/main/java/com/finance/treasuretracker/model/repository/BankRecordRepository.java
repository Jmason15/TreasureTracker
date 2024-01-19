package com.finance.treasuretracker.model.repository;

import com.finance.treasuretracker.model.BankRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankRecordRepository extends JpaRepository<BankRecord, Integer> {

    @Query("""
            SELECT br
                        FROM BankRecord br
                        WHERE br.date = (
                            SELECT MAX(br2.date)
                            FROM BankRecord br2
                            WHERE br2.account = br.account
                        )
                        ORDER BY br.date ASC
            
            """)
    List<BankRecord> getCurrentAccountFunds();
}

