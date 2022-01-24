package com.luizaprestes.challenge.model.persistent;

import com.luizaprestes.challenge.model.dto.ExpenseDto;
import com.luizaprestes.challenge.model.type.ExpenseType;
import com.luizaprestes.challenge.util.DateUtil;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "challenge_expense")
@NoArgsConstructor
@AllArgsConstructor
public final class Expense implements Serializable {

  @Id
  private long id;

  @Enumerated
  private ExpenseType type;

  private String description;
  private long value;

  private String date;
  private long dateValue;

  public ExpenseDto toDto() {
    return ExpenseDto.builder()
        .type(type == null ? ExpenseType.OTHERS.getType() : type.getType())
        .date(date)
        .description(description)
        .value(value)
        .build();
  }

}
