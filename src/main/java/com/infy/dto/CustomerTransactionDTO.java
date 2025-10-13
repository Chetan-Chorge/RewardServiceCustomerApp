package com.infy.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

@Data
public class CustomerTransactionDTO {


@NotNull(message = "{CUSTOMER.TransactionDate.absent}")
   @PastOrPresent(message = "{CUSTOMER.TransactionDate.Invalid}")
   private LocalDate transactionDate;

   @NotNull(message = "{CUSTOMER.Amount.absent}")
   @DecimalMin(value = "0.0",  message = "{CUSTOMER.Amount.Invalid}")
   private Double amount;

   @Min(value = 0, message = "{CUSTOMER.RewardsPoints.Invalid}")
   private int rewardPoints;

   public LocalDate getTransactionDate() {
	return transactionDate;
   }

   public void setTransactionDate(LocalDate transactionDate) {
	this.transactionDate = transactionDate;
   }

   public Double getAmount() {
	return amount;
   }

   public void setAmount(Double amount) {
	this.amount = amount;
   }

   public int getRewardPoints() {
	return rewardPoints;
   }

   public void setRewardPoints(int rewardPoints) {
	this.rewardPoints = rewardPoints;
   }

  
}

