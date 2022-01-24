package com.luizaprestes.challenge.controller;

import com.luizaprestes.challenge.model.Income;
import com.luizaprestes.challenge.repository.IncomeRepository;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public final class IncomeController {

  @Autowired
  private IncomeRepository repository;

  @PostMapping("/receitas")
  public String income(@Valid final Income income, final BindingResult result) {
    if (result.hasErrors()) {
      return "/receitas";
    }

    income.setId(UUID.randomUUID());
    income.setDateValue(System.currentTimeMillis());

    repository.save(income);
    return "/receitas";
  }

}
