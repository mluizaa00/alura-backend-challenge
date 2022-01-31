package com.luizaprestes.challenge.model.persistent;

import com.luizaprestes.challenge.model.dto.IncomeDto;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "challenge_income")
@NoArgsConstructor
@AllArgsConstructor
public final class Income implements Serializable {

  @Id
  private long id;

  private String description;
  private long value;

  private String date;
  private long dateValue;

  public IncomeDto toDto() {
    return IncomeDto.builder()
        .date(date)
        .description(description)
        .value(value)
        .build();
  }

}
