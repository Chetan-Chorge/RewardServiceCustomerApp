package com.infy.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardRequestDTO {

   @NotNull(message = "{RewardRequest.customerId.absent}")
   @Positive(message = "{RewardRequest.customerId.Invalid}")
   private Long customerId;

   @NotNull(message = "{RewardRequest.startDate.absent}")
   private LocalDate startDate;

   @NotNull(message = "{RewardRequest.EndDate.absent}")
   private LocalDate endDate;

}