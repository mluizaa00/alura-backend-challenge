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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public final class ExpenseController {

  @Autowired
  private ExpenseRepository repository;

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = "/v1/despesas", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<ExpenseDto>> getAll() {
    final List<ExpenseDto> expenseList = repository.findAll().stream()
        .map(Expense::toDto)
        .collect(Collectors.toList());
    
    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @RequestMapping(value = "/v1/despesas/?descricao={description}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<ExpenseDto>> getByDescription(@PathVariable final String description) {
    final List<ExpenseDto> expenseList = repository.findExpensesByDescriptionContaining(description).stream()
        .map(Expense::toDto)
        .collect(Collectors.toList());
    
    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @RequestMapping(value = "/v1/despesas/{year}/{month}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<ExpenseDto>> getByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<ExpenseDto> expenseList = repository.findAll().stream()
        .filter(expense -> DateUtil.isFromSameDate(expense.getDateValue(), year, month))
        .map(Expense::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @RequestMapping(value = "/v1/despesas", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<ExpenseDto> save(@Valid @RequestBody final ExpenseDto expenseDto, final BindingResult result) {
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
        .body(expenseDto);
  }

  @RequestMapping(value = "/v1/despesas/{expense_id}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<ExpenseDto> get(@PathVariable final long expense_id) {
    final var expense = repository.findById(expense_id)
        .orElse(null);

    if (expense == null) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(expense.toDto());
  }

  @RequestMapping(value = "/v1/despesas/{expense_id}", method = RequestMethod.PUT, produces = "application/json")
  public ResponseEntity<ExpenseDto> save(@PathVariable final long expense_id, @Valid final ExpenseDto expenseDto, final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var expense = expenseDto.toExpense(expense_id);
    repository.save(expense);

    return ResponseEntity.status(HttpStatus.OK)
        .body(expense.toDto());
  }

  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/v1/despesas/{expense_id}", method = RequestMethod.DELETE, produces = "application/json")
  public void delete(@PathVariable final long expense_id) {
    repository.deleteById(expense_id);
  }

}
