package com.payflow.service;

import com.payflow.entity.Transaction;
import com.payflow.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /*
     * By placing @Autowired here, we tell Spring's IoC container to find a bean 
     * implementing TransactionRepository (created by Spring Data JPA at runtime) 
     * and inject it automatically when it instantiates TransactionService.
     */
    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction sendMoney(Transaction transaction) {
        // For now, this just saves the transaction record.
        // Balance deduction logic is not required per the assignment brief.
        return transactionRepository.save(transaction);
    }
}
