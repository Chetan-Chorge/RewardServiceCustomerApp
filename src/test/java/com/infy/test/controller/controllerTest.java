package com.infy.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.infy.controller.RewardsController;
import com.infy.exception.RewardprogramCustomerException;
import com.infy.repository.TransactionRepository;
import com.infy.service.RewardsServiceImpl;

@SpringBootTest
public class controllerTest {

	 @InjectMocks
	 private RewardsServiceImpl rewardService;

	 @InjectMocks
	 private RewardsController rewardsController;

	 @Mock
	 private TransactionRepository transactionRepository;
	 
	 
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
	
}
