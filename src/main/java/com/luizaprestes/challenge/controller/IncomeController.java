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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public final class IncomeController {

  @Autowired
  private IncomeRepository repository;

  @ResponseStatus(HttpStatus.CREATED)
  @RequestMapping(value = "/v1/receitas", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<Income>> getAll() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(repository.findAll());
  }

  @RequestMapping(value = "/v1/receitas/?descricao={description}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<IncomeDto>> getByDescription(@PathVariable final String description) {
    final List<IncomeDto> incomeList = repository.findIncomesByDescriptionContaining(description).stream()
        .map(Income::toDto)
        .collect(Collectors.toList());
    
    return ResponseEntity.status(HttpStatus.OK)
        .body(incomeList);
  }

  @RequestMapping(value = "/v1/receitas/{year}/{month}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<List<IncomeDto>> getByDate(@PathVariable final long year, @PathVariable final long month) {
    final List<IncomeDto> incomeList = repository.findAll().stream()
        .filter(income -> DateUtil.isFromSameDate(income.getDateValue(), year, month))
        .map(Income::toDto)
        .collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK)
        .body(incomeList);
  }

  @RequestMapping(value = "/v1/receitas", method = RequestMethod.POST, produces = "application/json")
  public ResponseEntity<Income> save(@Valid @RequestBody final IncomeDto incomeDTO, final BindingResult result) {
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

  @RequestMapping(value = "/v1/receitas/{income_id}", method = RequestMethod.GET, produces = "application/json")
  public ResponseEntity<IncomeDto> get(@PathVariable final long income_id) {
    final var income = repository.findById(income_id)
        .orElse(null);

    if (income == null) {
      return ResponseEntity
          .badRequest()
          .build();
    }

    return ResponseEntity.status(HttpStatus.OK)
        .body(income.toDto());
  }

  @RequestMapping(value = "/v1/receitas/{income_id}", method = RequestMethod.PUT, produces = "application/json")
  public ResponseEntity<Income> save(@PathVariable final long income_id, @Valid final IncomeDto incomeDTO, final BindingResult result) {
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

  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @RequestMapping(value = "/v1/receitas/{income_id}", method = RequestMethod.DELETE, produces = "application/json")
  public void delete(@PathVariable final long income_id) {
    repository.deleteById(income_id);
  }

}
