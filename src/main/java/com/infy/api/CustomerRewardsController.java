package com.infy.api;


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
import com.infy.service.RewardsServiceImpl;



@RestController
@RequestMapping("/api/rewards")
@Validated
public class CustomerRewardsController {

	@Autowired
	private RewardsServiceImpl rewardsService;

	@PostMapping("/customer")
    public ResponseEntity<String> addTransaction(@RequestBody Transaction transaction) {
      String message = "Customer added successfully";
      return  new ResponseEntity<String>(message,HttpStatus.CREATED);
   }
	
	

	
	@GetMapping("/calculate/{customerId}")
     public ResponseEntity<RewardResponseDTO> getRewards(
         @PathVariable Long customerId,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws RewardprogramCustomerException {  

         return ResponseEntity.ok(rewardsService.calculateRewards(customerId, startDate, endDate));}}

