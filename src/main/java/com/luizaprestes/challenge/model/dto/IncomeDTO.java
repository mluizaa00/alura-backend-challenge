package com.luizaprestes.challenge.model.dto;

import com.luizaprestes.challenge.model.persistent.Income;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class IncomeDTO implements Serializable {

  @NotBlank
  private String description;

  @Min(1)
  private long value;

  @NotBlank
  private String date;

  public Income toIncome(final long id) {
    return Income.builder()
        .id(id)
        .date(date)
        .description(description)
        .dateValue(System.currentTimeMillis())
        .value(value)
        .build();
  }

}
