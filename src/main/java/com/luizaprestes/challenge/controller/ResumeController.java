package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.persistent.Expense;
import com.luizaprestes.challenge.model.persistent.Income;
import com.luizaprestes.challenge.model.MonthlyResume;
import com.luizaprestes.challenge.model.type.ExpenseType;
import com.luizaprestes.challenge.repository.ExpenseRepository;
import com.luizaprestes.challenge.repository.IncomeRepository;
import com.luizaprestes.challenge.util.DateUtil;
import com.luizaprestes.challenge.util.JacksonAdapter;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resumo")
public final class ResumeController {

  @Autowired
  private ExpenseRepository expenseRepository;

  @Autowired
  private IncomeRepository incomeRepository;

  @ResponseBody
  @GetMapping("/{year}/{month}")
  public ResponseEntity<MonthlyResume> getMonthlyResumeByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<Expense> expenseList = expenseRepository.findAll().stream()
        .filter(expense -> DateUtil.isFromSameDate(expense.getDateValue(), year, month))
        .collect(Collectors.toList());

    final List<Income> incomeList = incomeRepository.findAll().stream()
        .filter(income -> DateUtil.isFromSameDate(income.getDateValue(), year, month))
        .collect(Collectors.toList());

    long totalIncome = 0;
    for (final Income income : incomeList) {
      totalIncome += income.getValue();
    }

    final EnumMap<ExpenseType, Long> expenseTypeMap = new EnumMap<>(ExpenseType.class);

    long totalExpenses = 0;
    for (final Expense expense : expenseList) {
      totalExpenses += expense.getValue();

      final long currentExpenseFromType = expenseTypeMap.get(expense.getType());
      expenseTypeMap.put(expense.getType(), currentExpenseFromType + expense.getValue());
    }

    final MonthlyResume resume = MonthlyResume.builder()
        .totalIncome(totalIncome)
        .totalExpenses(totalExpenses)
        .total(totalIncome - totalExpenses)
        .expensesByType(expenseTypeMap)
        .build();

    return ResponseEntity.ok(resume);
  }

}
