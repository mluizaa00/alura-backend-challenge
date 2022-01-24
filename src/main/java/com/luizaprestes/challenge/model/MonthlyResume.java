package com.luizaprestes.challenge.model;

import com.luizaprestes.challenge.model.type.ExpenseType;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class MonthlyResume implements Serializable {

  private long totalIncome;
  private long totalExpenses;

  private long total;

  private Map<ExpenseType, Long> expensesByType;

}
