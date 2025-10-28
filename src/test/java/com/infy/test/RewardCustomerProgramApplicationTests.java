package com.infy.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.infy.controller.RewardsController;
import com.infy.dto.CustomerTransactionDTO;
import com.infy.dto.RewardResponseDTO;
import com.infy.entity.Transaction;
import com.infy.exception.RewardprogramCustomerException;
import com.infy.repository.TransactionRepository;
import com.infy.service.RewardsServiceImpl;
import com.infy.utility.ErrorInfo;
import com.infy.utility.ExceptionControllerAdvice;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@SpringBootTest
class RewardProgramCustomerApplicationTests {

 @InjectMocks
 private RewardsServiceImpl rewardService;

 @InjectMocks
 private RewardsController rewardsController;

 @Mock
 private TransactionRepository transactionRepository;

 @Mock
 private ModelMapper mapper;

 @Mock
 private Environment environment;

 @InjectMocks
 private ExceptionControllerAdvice advice;

 @Test
 void tRewardsNoTransactionsThrowsException() {
 Long customerId = 1L;
 LocalDate startDate = LocalDate.now().minusMonths(1);
LocalDate endDate = LocalDate.now();

 Mockito.when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate)).thenReturn(Collections.emptyList());

 assertThrows(RewardprogramCustomerException.class,() -> rewardService.calculateRewards(customerId, startDate, endDate));
 }

 
@Test
public void testCalculateRewards_validTransactions() throws RewardprogramCustomerException {
 Long customerId = 101L;
 LocalDate startDate = LocalDate.of(2025, 7, 1);
 LocalDate endDate = LocalDate.of(2025, 9, 30);

 Transaction tx1 = new Transaction();
tx1.setCustomerId(customerId);
 tx1.setCustomerName("John Doe");
 tx1.setAmount((double) 120);
tx1.setTransactionDate(LocalDate.of(2025, 7, 15));

Transaction tx2 = new Transaction();
tx2.setCustomerId(customerId);
tx2.setCustomerName("John Doe");
tx2.setAmount((double) 75);
tx2.setTransactionDate(LocalDate.of(2025, 8, 10));

 List<Transaction> transactions = Arrays.asList(tx1, tx2);

 when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate)).thenReturn(transactions);

 RewardResponseDTO response = rewardService.calculateRewards(customerId, startDate, endDate);

assertEquals(customerId, response.getCustomerId());
assertEquals("John Doe", response.getCustomerName());
assertEquals(115, response.getTotalRewards()); // 115 from tx1, 5 from tx2
assertEquals(2, response.getTransactions().size());
assertEquals(2, response.getMonthlyRewards().size());
}


 @Test
 void testCalculateMethod() {
 assertEquals(0, RewardsServiceImpl.calculate(40)); // No points
 assertEquals(0, RewardsServiceImpl.calculate(50)); // No points
 assertEquals(25, RewardsServiceImpl.calculate(75)); // 75 - 50
 assertEquals(90, RewardsServiceImpl.calculate(120)); // (120 - 100)*2 + 50
 }

 
 /** Test reward calculation when no transactions are found. Verifies that the correct exception is thrown.*/
@Test
public void testCalculateRewardss_noTransactionstest1() {
Long customerId = 102L;
LocalDate startDate = LocalDate.of(2025, 7, 1);
LocalDate endDate = LocalDate.of(2025, 9, 30);

when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate)).thenReturn(List.of());
try {
rewardService.calculateRewards(customerId, startDate, endDate);}
catch (RewardprogramCustomerException ex) {
assertEquals("No transactions found for customer ID: " + customerId, ex.getMessage());
}
 }



 @Test
 public void testCalculateRewards_noTransactions() {
 Long customerId = 102L;
LocalDate startDate = LocalDate.of(2025, 7, 1);
LocalDate endDate = LocalDate.of(2025, 9, 30);

when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate)).thenReturn(List.of());

RewardprogramCustomerException ex = Assertions.assertThrows(RewardprogramCustomerException.class,
() -> rewardService.calculateRewards(customerId, startDate, endDate));

assertEquals("No transactions found for customer ID: " + customerId, ex.getMessage());
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



// controller test cases

@Test
public void testGetRewards_invalidDateRange() {
Long customerId = 102L;
LocalDate startDate = LocalDate.of(2025, 10, 1);
LocalDate endDate = LocalDate.of(2025, 9, 1);

 RewardprogramCustomerException ex = assertThrows(RewardprogramCustomerException.class,
 () -> rewardsController.getRewards(customerId, startDate, endDate));

assertEquals("Start date must be before or equal to end date.", ex.getMessage());
}


 @Test
 public void testGetRewards_Test2() {
 Long customerId = 103L;
 LocalDate startDate = LocalDate.of(2025, 10, 1);
 LocalDate endDate = LocalDate.of(2025, 9, 1);

 RewardprogramCustomerException ex = assertThrows(RewardprogramCustomerException.class,
() -> rewardsController.getRewards(customerId, startDate, endDate));

assertEquals("Start date must be before or equal to end date.", ex.getMessage());
}

 
 @Test
public void testGetRewards_invalidDateRangeTest2() {
Long customerId = 103L;
 LocalDate startDate = LocalDate.of(2025, 10, 1);
 LocalDate endDate = LocalDate.of(2025, 9, 1);

 RewardprogramCustomerException ex = assertThrows(RewardprogramCustomerException.class,
() -> rewardsController.getRewards(customerId, startDate, endDate));

assertEquals("Start date must be before or equal to end date.", ex.getMessage());
}

   //Utility  


   @Test
   public void testRewardprogramCustomerExceptionHandler() {
       RewardprogramCustomerException ex = new RewardprogramCustomerException("Custom.ERROR");
       when(environment.getProperty("Custom.ERROR")).thenReturn("Custom error occurred");

       ResponseEntity<ErrorInfo> response = advice.meetingSchedulerExceptionHandler(ex);

       assertEquals(400, response.getStatusCodeValue());
       assertEquals("Custom error occurred", response.getBody().getErrorMessage());
   }

   
   
   /** Test handling of general Exception
    */
   @Test
   public void testGeneralExceptionHandler() {
       Exception ex = new Exception("Something went wrong");
       when(environment.getProperty("General.EXCEPTION_MESSAGE")).thenReturn("Internal server error");

       ResponseEntity<ErrorInfo> response = advice.generalExceptionHandler(ex);

       assertEquals(500, response.getStatusCodeValue());
       assertEquals("Internal server error", response.getBody().getErrorMessage());
   }

   /**Test handling of MethodArgumentNotValidException*/
   @Test
   public void testMethodArgumentNotValidExceptionHandler() {
       ObjectError error = new ObjectError("field", "Invalid field");
       MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
       when(ex.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
       when(ex.getBindingResult().getAllErrors()).thenReturn(Collections.singletonList(error));

       ResponseEntity<ErrorInfo> response = advice.validatorExceptionHandler(ex);

       assertEquals(400, response.getStatusCodeValue());
       assertEquals("Invalid field", response.getBody().getErrorMessage());
   }

   /**
    * ✅ Test handling of ConstraintViolationException
    */
   @Test
   public void testConstraintViolationExceptionHandler() {
       ConstraintViolation<?> violation = mock(ConstraintViolation.class);
       when(violation.getMessage()).thenReturn("Constraint violated");

       ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

       ResponseEntity<ErrorInfo> response = advice.validatorExceptionHandler(ex);

       assertEquals(400, response.getStatusCodeValue());
       assertEquals("Constraint violated", response.getBody().getErrorMessage());
   }
  
   
   @Test
   void testCalculateMethodLogic() {
   assertEquals(0, RewardsServiceImpl.calculate(45));
   assertEquals(5, RewardsServiceImpl.calculate(55));
   assertEquals(50, RewardsServiceImpl.calculate(100));
   assertEquals(90, RewardsServiceImpl.calculate(120)); // 20*2 + 50

   }

   @Test
   void testCalculateRewards_noTransactions_throwsException() {

   Long customerId = 2L;

   LocalDate startDate = LocalDate.of(2025, 1, 1);

   LocalDate endDate = LocalDate.of(2025, 3, 31);

   when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate))

   .thenReturn(Collections.emptyList());

   RewardprogramCustomerException exception = assertThrows(RewardprogramCustomerException.class, () ->

   rewardService.calculateRewards(customerId, startDate, endDate));
   assertTrue(exception.getMessage().contains("No transactions found"));

   }


   @Tag(name ="New1")
   @Test
   void testCalculateRewards_transactionExactlyAtThresholds() throws RewardprogramCustomerException {
   Long customerId = 5L;
   LocalDate date = LocalDate.of(2025, 2, 10);
   Transaction tx50 = new Transaction(1L, customerId, "David", date, 50.0);
   Transaction tx100 = new Transaction(2L, customerId, "David", date, 100.0);
   when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, date, date))
   .thenReturn(Arrays.asList(tx50, tx100));
   RewardResponseDTO response = rewardService.calculateRewards(customerId, date, date);

   assertEquals(50, response.getTotalRewards());

   }

   @Test
   void testCalculateRewards_nullTransactionList_throwsException() {
   Long customerId = 3L;

   LocalDate startDate = LocalDate.of(2025, 1, 1);

   LocalDate endDate = LocalDate.of(2025, 1, 31);

   when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate))
   .thenReturn(null);
   assertThrows(NullPointerException.class, () ->
   rewardService.calculateRewards(customerId, startDate, endDate));
   }


   @Test
   void testCustomerTransactionDTO_rewardPointsSetCorrectly() throws RewardprogramCustomerException {
   Long customerId = 4L;
   LocalDate date = LocalDate.of(2025, 2, 10);

   Transaction tx = new Transaction(1L, customerId, "Charlie", date, 105.0);
   when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, date, date))

   .thenReturn(Collections.singletonList(tx));
   RewardResponseDTO response = rewardService.calculateRewards(customerId, date, date);

   CustomerTransactionDTO dto = response.getTransactions().get(0);
   assertEquals(60, dto.getRewardPoints());

   }

}
 