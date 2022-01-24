package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.Expense;
import com.luizaprestes.challenge.repository.ExpenseRepository;
import com.luizaprestes.challenge.util.JacksonAdapter;
import java.util.List;
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
@RequestMapping("/despesas")
public final class ExpenseController {

  private static final String DEFAULT;

  static {
    DEFAULT = "/despesas";
  }

  @Autowired
  private ExpenseRepository repository;

  @GetMapping
  public String getExpenses() {
    final List<Expense> incomeList = repository.findAll();
    return JacksonAdapter.getInstance().serialize(incomeList);
  }

  @PostMapping
  public String saveExpense(@Valid final Expense expense, final BindingResult result) {
    if (result.hasErrors()) {
      return DEFAULT;
    }

    expense.setId(repository.count() + 1);
    expense.setDateValue(System.currentTimeMillis());

    repository.save(expense);
    return DEFAULT;
  }

  @GetMapping("/{expense_id}")
  public String getIncome(@PathVariable final long expense_id) {
    final Expense income = repository.findById(expense_id)
        .orElse(null);
    
    if (income == null) {
      return DEFAULT;
    }
    
    return JacksonAdapter.getInstance().serialize(expense_id);
  }

  @PutMapping("/{expense_id}")
  public String saveExpense(@PathVariable final long expense_id, @Valid final Expense expense, final BindingResult result) {
    if (result.hasErrors()) {
      return DEFAULT;
    }

    expense.setId(expense_id);
    repository.save(expense);

    return DEFAULT;
  }

  @DeleteMapping("/{expense_id}")
  public String saveExpense(@PathVariable final long expense_id) {
    repository.deleteById(expense_id);
    return DEFAULT;
  }

}
