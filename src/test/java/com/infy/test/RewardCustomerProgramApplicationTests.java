package com.infy.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.dto.CustomerTransactionDTO;
import com.infy.dto.RewardResponseDTO;
import com.infy.entity.Transaction;
import com.infy.exception.RewardprogramCustomerException;
import com.infy.repository.TransactionRepository;
import com.infy.service.RewardsServiceImpl;
@SpringBootTest
class RewardCustomerProgramApplicationTests {


	   @InjectMocks
	   private RewardsServiceImpl rewardService;

	   @Mock
	   private TransactionRepository transactionRepository;

	   @Mock
	   private ModelMapper mapper;

	   @Test
	   void tRewardsNoTransactionsThrowsException() {
	       Long customerId = 1L;
	       LocalDate startDate = LocalDate.now().minusMonths(1);
	       LocalDate endDate = LocalDate.now();

	       Mockito.when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate))
	              .thenReturn(Collections.emptyList());

	       assertThrows(RewardprogramCustomerException.class, () ->
	           rewardService.calculateRewards(customerId, startDate, endDate)
	       );
	   }

	   @Test
	   void testcalculateRewardsWithTransactionsReturnsCorrectRewards() throws RewardprogramCustomerException {
	       Long customerId = 1L;
	       LocalDate startDate = LocalDate.of(2025, 1, 1);
	       LocalDate endDate = LocalDate.of(2025, 12, 31);

	       Transaction tx1 = new Transaction(1L, customerId, "John Doe", LocalDate.of(2025, 1, 15), 120.0);
	       Transaction tx2 = new Transaction(2L, customerId, "John Doe", LocalDate.of(2025, 2, 10), 75.0);
	       Transaction tx3 = new Transaction(3L, customerId, "John Doe", LocalDate.of(2025, 2, 20), 40.0);

	       List<Transaction> transactions = List.of(tx1, tx2, tx3);

	       Mockito.when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate))
	              .thenReturn(transactions);

	       CustomerTransactionDTO dto1 = new CustomerTransactionDTO();
	       CustomerTransactionDTO dto2 = new CustomerTransactionDTO();
	       CustomerTransactionDTO dto3 = new CustomerTransactionDTO();

	       Mockito.when(mapper.map(tx1, CustomerTransactionDTO.class)).thenReturn(dto1);
	       Mockito.when(mapper.map(tx2, CustomerTransactionDTO.class)).thenReturn(dto2);
	       Mockito.when(mapper.map(tx3, CustomerTransactionDTO.class)).thenReturn(dto3);

	       RewardResponseDTO response = rewardService.calculateRewards(customerId, startDate, endDate);

	       assertEquals(customerId, response.getCustomerId());
	       assertEquals("John Doe", response.getCustomerName());
	       assertEquals(115, response.getTotalRewards()); // tx1:90,tx2: 25,tx3: 0

	       assertEquals(2, response.getMonthlyRewards().size());
	       assertEquals(90, response.getMonthlyRewards().stream().filter(m -> m.getMonth().equals("JANUARY")).findFirst().get().getRewardPoints());
	       assertEquals(25, response.getMonthlyRewards().stream().filter(m -> m.getMonth().equals("FEBRUARY")).findFirst().get().getRewardPoints());

	       assertEquals(3, response.getTransactions().size());
	       assertEquals(90, response.getTransactions().get(0).getRewardPoints());
	       assertEquals(25, response.getTransactions().get(1).getRewardPoints());
	       assertEquals(0, response.getTransactions().get(2).getRewardPoints());
	   }
	   
	   @Test
	   void testCalculateMethod() {
	       assertEquals(0, RewardsServiceImpl.calculate(40));     // No points
	       assertEquals(0, RewardsServiceImpl.calculate(50));     // No points
	       assertEquals(25, RewardsServiceImpl.calculate(75));    // 75 - 50
	       assertEquals(90, RewardsServiceImpl.calculate(120));   // (120 - 100)*2 + 50
	   }
	   


	    @Test
	   public void testSaveTransaction_ValidTransaction_ReturnsSavedTransaction() {
	       Transaction transaction = new Transaction();
	       transaction.setCustomerId(1001L);
	       transaction.setAmount(150.0);
	       transaction.setTransactionDate(LocalDate.of(2025, 10, 9));

	       Mockito.when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

	       Transaction savedTransaction = rewardService.saveTransaction(transaction);
	       assertEquals(transaction.getCustomerId(), savedTransaction.getCustomerId());
	       assertEquals(transaction.getAmount(), savedTransaction.getAmount());
	       assertEquals(transaction.getTransactionDate(), savedTransaction.getTransactionDate());

	       Mockito.verify(transactionRepository, Mockito.times(1)).save(transaction);
	    }
	 

	}
