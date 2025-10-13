package com.infy.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.infy.entity.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

   List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);

   List<Transaction> findAll(); 
}
