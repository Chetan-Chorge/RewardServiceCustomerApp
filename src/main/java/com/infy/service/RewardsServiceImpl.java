package com.infy.service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.CustomerTransactionDTO;
import com.infy.dto.MonthlyRewardDTO;
import com.infy.dto.RewardResponseDTO;
import com.infy.entity.Transaction;
import com.infy.exception.RewardprogramCustomerException;
import com.infy.repository.TransactionRepository;

@Service
@Transactional
public class RewardsServiceImpl implements RewardService {

	@Autowired
	private TransactionRepository transactionRepository;

	private final ModelMapper mapper = new ModelMapper();

	/**
	 * Calculates reward points based on transaction amount. - 2 points for every
	 *     * dollar spent over $100 - 1 point for every dollar spent between $50 and
	 * $100  
	 */
	public static int calculate(double value) {
		int points = 0;
		if (value > 100) {
			points += (int) ((value - 100) * 2);// 2 points for every dollar above 100
// 1 point per dollar between 50 and 100
			points += 50;
		} else if (value > 50 && value <= 100) {
			points += (int) (value - 50);// 1 point per dollar between 50 and 100
		}
		return points;

	}
	

	/**
	 * Calculates total and monthly reward points for a customer within a date *
	 * range. Returns detailed reward response including transaction details.
	 */
	public RewardResponseDTO calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate)
			throws RewardprogramCustomerException {
		List<Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId,
				startDate, endDate);
		if (transactions.isEmpty()) {
			throw new RewardprogramCustomerException("No transactions found for customer ID: " + customerId);
		}

// Get customer name from the first transaction.
		Transaction firstTransaction = transactions.stream().findFirst().orElseThrow(
				() -> new RewardprogramCustomerException("No transactions found for customer ID: " + customerId));

		RewardResponseDTO response = new RewardResponseDTO();
		response.setCustomerId(customerId);
		response.setCustomerName(firstTransaction.getCustomerName());

		int totalPoints = 0;
		List<MonthlyRewardDTO> monthlyRewards = new ArrayList<>();
		List<CustomerTransactionDTO> transactionDTOs = new ArrayList<>();
		for (Transaction tx : transactions) {
			int points = calculate(tx.getAmount());
			totalPoints += points;

// Get month name in full format (e.g., "July")
			String month = tx.getTransactionDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

// Check if month already exists in the monthly rewards list
			Optional<MonthlyRewardDTO> existing = monthlyRewards.stream().filter(m -> m.getMonth().equals(month))
					.findFirst();
			if (existing.isPresent()) {
				existing.get().setRewardPoints(existing.get().getRewardPoints() + points);
			} else {
				monthlyRewards.add(new MonthlyRewardDTO(month, points));
			}
			CustomerTransactionDTO dto = mapper.map(tx, CustomerTransactionDTO.class);
			dto.setRewardPoints(points);
			transactionDTOs.add(dto);
		}
		response.setMonthlyRewards(monthlyRewards);
		response.setTotalRewards(totalPoints);
		response.setTransactions(transactionDTOs);

		return response;
	}

	/** Saves a new transaction to the database. */
	public Transaction saveTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

}
