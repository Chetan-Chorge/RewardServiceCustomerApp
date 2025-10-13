package com.infy.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infy.dto.CustomerTransactionDTO;
import com.infy.dto.MonthlyRewardDTO;
import com.infy.dto.RewardResponseDTO;
import com.infy.entity.Transaction;
import com.infy.exception.RewardprogramCustomerException;
import com.infy.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RewardsServiceImpl {

	
	@Autowired
	private TransactionRepository transactionRepository;
	
	private final ModelMapper mapper = new ModelMapper();
	
	
	// 1 Calculate the Price
	public static int calculate(double value) {
        int point = 0;
         if (value > 100) {
        	 point += (int) ((value - 100) * 2);
        	 point += 50; // 1 point per dollar between 50 and 100
        }
         else if (value > 50 && value <100) {
        	 point += (int) (value - 50);
             }
        return point;
   }

	public RewardResponseDTO calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate) throws RewardprogramCustomerException {
         List<Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);

        if (transactions.isEmpty()) {
          throw new RewardprogramCustomerException("No transactions found for customer ID: " + customerId);
}
      RewardResponseDTO responses = new RewardResponseDTO();
      responses.setCustomerId(customerId);
      responses.setCustomerName(transactions.get(0).getCustomerName());

       int totalPoints = 0;
       List<MonthlyRewardDTO> monthlyRewards = new ArrayList<>();
        List<CustomerTransactionDTO> transactionDTOs = new ArrayList<>();
          
         for ( Transaction value : transactions) {
           int points = calculate(value.getAmount());
          totalPoints += points;

            String month = value.getTransactionDate().getMonth().toString();


            Optional<MonthlyRewardDTO> existing = monthlyRewards.stream()
                .filter(m -> m.getMonth().equals(month)) .findFirst();

             if (existing.isPresent()) {
                 existing.get().setRewardPoints(existing.get().getRewardPoints() + points);
            } else {
                monthlyRewards.add(new MonthlyRewardDTO(month, points));
                }

            CustomerTransactionDTO dto = mapper.map(value, CustomerTransactionDTO.class);
            dto.setRewardPoints(points);
           transactionDTOs.add(dto);}
 
         responses.setMonthlyRewards(monthlyRewards);
         responses.setTotalRewards(totalPoints);
         responses.setTransactions(transactionDTOs);

        return responses;
    }

	
	
	public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }}