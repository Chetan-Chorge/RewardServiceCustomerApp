package com.infy.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRewardDTO {


  @NotBlank(message = "{MonthlyReward.month.absent}")
  @Pattern(regexp = "^(January|February|March|April|May|June|July|August|September|October|November|December)$",
  message = "{MonthlyReward.month.Invalid}")
   private String month;

 @Min(value = 0, message = "{MonthlyReward.month.rewardPoints}")
 private int rewardPoints;

 public MonthlyRewardDTO(
		@NotBlank(message = "{MonthlyReward.month.absent}") @Pattern(regexp = "^(January|February|March|April|May|June|July|August|September|October|November|December)$", message = "{MonthlyReward.month.Invalid}") String month,
		@Min(value = 0, message = "{MonthlyReward.month.rewardPoints}") int rewardPoints) {
	super();
	this.month = month;
	this.rewardPoints = rewardPoints;
 }

 public String getMonth() {
	return month;
 }

 public void setMonth(String month) {
	this.month = month;
 }

 public int getRewardPoints() {
	return rewardPoints;
 }

 public void setRewardPoints(int rewardPoints) {
	this.rewardPoints = rewardPoints;
 }


 
 
 
 
 
}

