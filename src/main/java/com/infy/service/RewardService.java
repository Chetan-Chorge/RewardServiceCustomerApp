package com.infy.service;


import java.time.LocalDate;

import com.infy.dto.RewardResponseDTO;
import com.infy.entity.Transaction;
import com.infy.exception.RewardprogramCustomerException;

public interface RewardService {
 RewardResponseDTO calculateRewards(String customerId, LocalDate startDate, LocalDate endDate) throws RewardprogramCustomerException  ;
   Transaction saveTransaction(Transaction transaction);
}
