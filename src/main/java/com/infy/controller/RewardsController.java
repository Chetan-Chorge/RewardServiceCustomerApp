package com.infy.controller;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.infy.dto.RewardResponseDTO;
import com.infy.entity.Transaction;
import com.infy.exception.RewardprogramCustomerException;
import com.infy.service.RewardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rewards")
@Validated
public class RewardsController {

	@Autowired
	private RewardService rewardService;

	/**
	 * Endpoint to add a new customer transaction. Accepts a JSON payload
	 * representing a Transaction object.      * Validates the input and returns a
	 * success message.    
	 */
	@PostMapping("/customer")
	public ResponseEntity<String> addTransaction(@RequestBody @Valid Transaction transaction) {
		Transaction id = rewardService.saveTransaction(transaction);
		String message = "Customer added successfully" + "-" + id.getCustomerId();
		return new ResponseEntity<String>(message, HttpStatus.CREATED);
	}

	
	
	/**
	 * Endpoint to calculate reward points for a customer. Accepts customerId as a
	 * path variable and startDate/endDate as query parameters. Returns detailed
	 * reward response including monthly breakdown and transactions.
	 */
	@GetMapping("calculate/{customerId}")
	public ResponseEntity<RewardResponseDTO> getRewards(@PathVariable Long customerId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
			throws RewardprogramCustomerException {

		if (startDate.isAfter(endDate)) {
			throw new RewardprogramCustomerException("Start date must be before or equal to end date.");
		}
		return ResponseEntity.ok(rewardService.calculateRewards(customerId, startDate, endDate));
	}
}
