package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.Expense;
import com.luizaprestes.challenge.model.Income;
import com.luizaprestes.challenge.repository.IncomeRepository;
import com.luizaprestes.challenge.util.JacksonAdapter;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receitas")
public final class IncomeController {

  private static final String DEFAULT;

  static {
    DEFAULT = "/receitas";
  }

  @Autowired
  private IncomeRepository repository;

  @GetMapping
  public String getIncome() {
    final List<Income> incomeList = repository.findAll();
    return JacksonAdapter.getInstance().serialize(incomeList);
  }

  @GetMapping("/?descricao={description}")
  public String getIncomeByDescription(@PathVariable final String description) {
    final List<Income> incomeList = repository.findAll().stream()
        .filter(income -> income.getDescription().contains(description))
        .collect(Collectors.toList());

    return JacksonAdapter.getInstance().serialize(incomeList);
  }

  @GetMapping("/{year}/{month}")
  public String getExpenseByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<Income> incomeList = repository.findAll().stream()
        .filter(expense -> expense.isFromSameDate(year, month))
        .collect(Collectors.toList());

    return JacksonAdapter.getInstance().serialize(incomeList);
  }

  @PostMapping
  public String saveIncome(@Valid final Income income, final BindingResult result) {
    if (result.hasErrors()) {
      return DEFAULT;
    }

    income.setId(repository.count() + 1);
    income.setDateValue(System.currentTimeMillis());

    repository.save(income);
    return DEFAULT;
  }

  @GetMapping("/{income_id}")
  public String getIncome(@PathVariable final long income_id) {
    final Income income = repository.findById(income_id)
        .orElse(null);
    
    if (income == null) {
      return DEFAULT;
    }
    
    return JacksonAdapter.getInstance().serialize(income_id);
  }

  @PutMapping("/{income_id}")
  public String saveIncome(@PathVariable final long income_id, @Valid final Income income, final BindingResult result) {
    if (result.hasErrors()) {
      return DEFAULT;
    }

    income.setId(income_id);
    repository.save(income);

    return DEFAULT;
  }

  @DeleteMapping("/{income_id}")
  public String deleteIncome(@PathVariable final long income_id) {
    repository.deleteById(income_id);
    return DEFAULT;
  }

}
