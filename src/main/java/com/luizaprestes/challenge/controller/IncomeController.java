package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.dto.IncomeDto;
import com.luizaprestes.challenge.model.persistent.Income;
import com.luizaprestes.challenge.repository.IncomeRepository;
import com.luizaprestes.challenge.util.DateUtil;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/v1/receitas")
public final class IncomeController {

  @Autowired
  private IncomeRepository repository;

  @ResponseBody
  @ResponseStatus(HttpStatus.CREATED)
  @GetMapping
  public ResponseEntity<List<IncomeDto>> getAll() {
    final List<IncomeDto> incomeList = repository.findAll().stream()
        .map(Income::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.ok(incomeList);
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<List<IncomeDto>> getByDescription(@RequestParam final String description) {
    final List<IncomeDto> incomeList = repository.findIncomesByDescriptionContaining(description)
        .stream()
        .map(Income::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK)
        .body(incomeList);
  }

  @ResponseBody
  @GetMapping(value = "/{year}/{month}", produces = "application/json")
  public ResponseEntity<List<IncomeDto>> getByDate(@PathVariable final long year,
      @PathVariable final long month) {
    final List<IncomeDto> incomeList = repository.findAll().stream()
        .filter(income -> DateUtil.isFromSameDate(income.getDateValue(), year, month))
        .map(Income::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK)
        .body(incomeList);
  }

  @PostMapping
  public ResponseEntity<IncomeDto> save(@Valid @RequestBody final IncomeDto incomeDTO,
      final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var income = incomeDTO.toIncome(repository.count() + 1);
    repository.save(income);

    final var location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(income.getId())
        .toUri();

    return ResponseEntity.created(location)
        .body(income.toDto());
  }

  @ResponseBody
  @GetMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<IncomeDto> get(@PathVariable final long id) {
    final var income = repository.findById(id);
    return income.map(value -> ResponseEntity.ok(value.toDto()))
        .orElseGet(() -> ResponseEntity
          .notFound()
          .build());
  }

  @PutMapping(value = "/{id}", produces = "application/json")
  public ResponseEntity<Income> save(@PathVariable final long id, @Valid final IncomeDto incomeDTO,
      final BindingResult result) {
    if (result.hasErrors()) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    final var income = incomeDTO.toIncome(id);
    repository.save(income);

    return ResponseEntity.status(HttpStatus.OK)
        .body(income);
  }

  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @DeleteMapping(value = "/{id}", produces = "application/json")
  public void delete(@PathVariable final long id) {
    repository.deleteById(id);
  }

}
