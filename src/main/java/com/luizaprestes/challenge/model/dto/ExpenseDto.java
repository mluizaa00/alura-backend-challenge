package com.luizaprestes.challenge.model.dto;

import com.luizaprestes.challenge.model.persistent.Expense;
import com.luizaprestes.challenge.model.type.ExpenseType;
import com.luizaprestes.challenge.util.DateUtil;
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
public final class ExpenseDto implements Serializable {

  private ExpenseType type;

  @NotBlank
  private String description;

  @Min(1)
  private long value;

  @NotBlank
  private String date;

  public Expense toExpense(final long id) {
    return Expense.builder()
        .id(id)
        .type(type == null ? ExpenseType.OTHERS : type)
        .date(date)
        .description(description)
        .dateValue(DateUtil.parse(date))
        .value(value)
        .build();
  }

}
