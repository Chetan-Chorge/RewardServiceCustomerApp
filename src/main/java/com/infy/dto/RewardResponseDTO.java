package com.infy.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RewardResponseDTO {


   @NotNull(message = "{RewardResponse.customerId.absent}")
   @Positive(message = "{RewardResponse.customerId.Invalid}")
   private Long customerId;

   @NotBlank(message = "Customer name is required.")
   @Size(max = 100, message = "Customer name must not exceed 100 characters.")
   private String customerName;

   @NotNull(message = "{RewardResponse.monthlyRewards.absent}")
   @Size(min = 1, message = "{RewardResponse.monthlyRewards.Invalid}")
   private List<MonthlyRewardDTO> monthlyRewards = new ArrayList<>();

   @Min(value = 0, message = "{RewardResponse.totalRewards.Invalid}")
   private int totalRewards;

   @NotNull(message = "{RewardResponse.transactions.absent}")
   @Size(min = 1, message = "{RewardResponse.transactions.Invalid}")
   private List<CustomerTransactionDTO> transactions = new ArrayList<>();

   public Long getCustomerId() {
	return customerId;
   }

   public void setCustomerId(Long customerId) {
	this.customerId = customerId;
   }

   public String getCustomerName() {
	return customerName;
   }

   public void setCustomerName(String customerName) {
	this.customerName = customerName;
   }

   public List<MonthlyRewardDTO> getMonthlyRewards() {
	return monthlyRewards;
   }

   public void setMonthlyRewards(List<MonthlyRewardDTO> monthlyRewards) {
	this.monthlyRewards = monthlyRewards;
   }

   public int getTotalRewards() {
	return totalRewards;
   }

   public void setTotalRewards(int totalRewards) {
	this.totalRewards = totalRewards;
   }

   public List<CustomerTransactionDTO> getTransactions() {
	return transactions;
   }

   public void setTransactions(List<CustomerTransactionDTO> transactions) {
	this.transactions = transactions;
   }

  

   

   


}
