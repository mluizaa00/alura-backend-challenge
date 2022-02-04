package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.dto.ExpenseDto;
import com.luizaprestes.challenge.model.persistent.Expense;
import com.luizaprestes.challenge.repository.ExpenseRepository;
import com.luizaprestes.challenge.util.DateUtil;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/despesas")
public final class ExpenseController {

  @Autowired
  private ExpenseRepository repository;

  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  @GetMapping(value = "/v1/despesas", produces = "application/json")
  public ResponseEntity<List<ExpenseDto>> getAll() {
    final List<ExpenseDto> expenseList = repository.findAll().stream()
        .map(Expense::toDto)
        .collect(Collectors.toList());
    
    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<ExpenseDto>> getByDescription(@RequestParam final String description) {
    final List<ExpenseDto> expenseList = repository.findExpensesByDescriptionContaining(description).stream()
        .map(Expense::toDto)
        .collect(Collectors.toList());
    
    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @ResponseBody
  @GetMapping(value = "/{year}/{month}", produces = "application/json")
  public ResponseEntity<List<ExpenseDto>> getByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<ExpenseDto> expenseList = repository.findAll().stream()
        .filter(expense -> DateUtil.isFromSameDate(expense.getDateValue(), year, month))
        .map(Expense::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @GetMapping
  public ResponseEntity<Page<ExpenseDto>> getPaginated(@RequestParam final int page, @RequestParam final int amount, @RequestParam final String ordenator) {
    final Pageable paginator = PageRequest.of(page, amount, Direction.ASC, ordenator);
    final Page<ExpenseDto> expenseList = repository.findAll(paginator)
        .map(Expense::toDto);

    return ResponseEntity.status(HttpStatus.OK)
        .body(expenseList);
  }

  @PostMapping
  public ResponseEntity<ExpenseDto> save(@Valid @RequestBody final ExpenseDto expenseDto, final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var expense = expenseDto.toExpense(repository.count() + 1);
    repository.save(expense);

    final var location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(expense.getId())
        .toUri();

    return ResponseEntity.created(location)
        .body(expenseDto);
  }

  @ResponseBody
  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<ExpenseDto> get(@PathVariable final long id) {
    final var expense = repository.findById(id);
    return expense.map(value -> ResponseEntity.ok(value.toDto()))
        .orElseGet(() -> ResponseEntity
          .notFound()
          .build());
  }

  @PutMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<ExpenseDto> save(@PathVariable final long id, @Valid final ExpenseDto expenseDto, final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var expense = expenseDto.toExpense(id);
    repository.save(expense);

    return ResponseEntity.ok(expense.toDto());
  }

  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @DeleteMapping(value = "/{id}", produces = "application/json")
  public void delete(@PathVariable final long id) {
    repository.deleteById(id);
  }

}
