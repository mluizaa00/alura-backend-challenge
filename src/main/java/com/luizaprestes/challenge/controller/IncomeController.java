package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.dto.IncomeDto;
import com.luizaprestes.challenge.model.persistent.Income;
import com.luizaprestes.challenge.repository.IncomeRepository;
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
public final class IncomeController {

  @Autowired
  private IncomeRepository repository;

  @GetMapping
  public ResponseEntity<List<Income>> getIncomes() {
    final List<Income> incomeList = repository.findAll();
    return ResponseEntity.status(HttpStatus.OK)
        .body(incomeList);
  }

  @GetMapping("/?descricao={description}")
  public ResponseEntity<List<Income>> getIncomeByDescription(@PathVariable final String description) {
    final List<Income> incomeList = repository.findIncomesByDescriptionContaining(description);
    return ResponseEntity.status(HttpStatus.OK)
        .body(incomeList);
  }

  @GetMapping("/{year}/{month}")
  public ResponseEntity<List<Income>> getIncomeByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<Income> incomeList = repository.findAll().stream()
        .filter(income -> DateUtil.isFromSameDate(income.getDateValue(), year, month))
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK)
        .body(incomeList);
  }

  @PostMapping
  public ResponseEntity<Income> saveIncome(@Valid @RequestBody final IncomeDto incomeDTO, final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var income = incomeDTO.toIncome(repository.count() + 1);
    repository.save(income);

    final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(income.getId())
        .toUri();

    return ResponseEntity.created(location)
        .body(income);
  }

  @GetMapping("/{income_id}")
  public ResponseEntity<Income> getIncome(@PathVariable final long income_id) {
    final var income = repository.findById(income_id)
        .orElse(null);

    if (income == null) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(income);
  }

  @PutMapping("/{income_id}")
  public ResponseEntity<Income> saveIncome(@PathVariable final long income_id, @Valid final IncomeDto incomeDTO, final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var income = incomeDTO.toIncome(income_id);
    repository.save(income);

    return ResponseEntity.status(HttpStatus.OK)
        .body(income);
  }

  @DeleteMapping("/{income_id}")
  public ResponseEntity<String> deleteIncome(@PathVariable final long income_id) {
    repository.deleteById(income_id);
    return ResponseEntity.status(HttpStatus.OK)
        .body("Successful operation");
  }

}
