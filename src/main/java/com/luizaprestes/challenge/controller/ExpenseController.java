package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.dto.ExpenseDto;
import com.luizaprestes.challenge.model.persistent.Expense;
import com.luizaprestes.challenge.repository.ExpenseRepository;
import com.luizaprestes.challenge.util.DateUtil;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/receitas")
public final class ExpenseController {

  @Autowired
  private ExpenseRepository repository;

  @GetMapping
  public ResponseEntity<List<Expense>> getIncomes() {
    final List<Expense> expenseList = repository.findAll();
    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @GetMapping("/?descricao={description}")
  public ResponseEntity<List<Expense>> getIncomeByDescription(@PathVariable final String description) {
    final List<Expense> expenseList = repository.findExpensesByDescriptionContaining(description);
    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @GetMapping("/{year}/{month}")
  public ResponseEntity<List<Expense>> getIncomeByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<Expense> expenseList = repository.findAll().stream()
        .filter(income -> DateUtil.isFromSameDate(income.getDateValue(), year, month))
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @PostMapping
  public ResponseEntity<Expense> saveIncome(@Valid @RequestBody final ExpenseDto expenseDto, final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var expense = expenseDto.toExpense(repository.count() + 1);
    repository.save(expense);

    final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(expense.getId())
        .toUri();

    return ResponseEntity.created(location)
        .body(expense);
  }

  @GetMapping("/{expense_id}")
  public ResponseEntity<Expense> getIncome(@PathVariable final long expense_id) {
    final var expense = repository.findById(expense_id)
        .orElse(null);

    if (expense == null) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(expense);
  }

  @PutMapping("/{expense_id}")
  public ResponseEntity<Expense> saveIncome(@PathVariable final long expense_id, @Valid final ExpenseDto expenseDto, final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var income = expenseDto.toExpense(expense_id);
    repository.save(income);

    return ResponseEntity.status(HttpStatus.OK)
        .body(income);
  }

  @DeleteMapping("/{expense_id}")
  public ResponseEntity<String> deleteIncome(@PathVariable final long expense_id) {
    repository.deleteById(expense_id);
    return ResponseEntity.status(HttpStatus.OK)
        .body("Successful operation");
  }

}
