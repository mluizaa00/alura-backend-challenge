package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.Expense;
import com.luizaprestes.challenge.model.Income;
import com.luizaprestes.challenge.model.MonthlyResume;
import com.luizaprestes.challenge.model.type.ExpenseType;
import com.luizaprestes.challenge.repository.ExpenseRepository;
import com.luizaprestes.challenge.repository.IncomeRepository;
import com.luizaprestes.challenge.util.JacksonAdapter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resumo")
public final class ResumeController {

  @Autowired
  private ExpenseRepository expenseRepository;

  @Autowired
  private IncomeRepository incomeRepository;

  @GetMapping("/{year}/{month}")
  public String getMonthlyResumeByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<Expense> expenseList = expenseRepository.findAll().stream()
        .filter(expense -> expense.isFromSameDate(year, month))
        .collect(Collectors.toList());

    final List<Income> incomeList = incomeRepository.findAll().stream()
        .filter(expense -> expense.isFromSameDate(year, month))
        .collect(Collectors.toList());

    long totalIncome = 0;
    for (final Income income : incomeList) {
      totalIncome += income.getValue();
    }

    final Map<ExpenseType, Long> expenseTypeMap = new HashMap<>();

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

    return JacksonAdapter.getInstance().serialize(resume);
  }

}
