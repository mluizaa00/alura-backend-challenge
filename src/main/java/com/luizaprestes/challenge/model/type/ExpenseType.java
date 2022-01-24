package com.luizaprestes.challenge.model.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
public enum ExpenseType {

  FOOD("Alimentacao"),
  HEALTH("Saude"),
  HOUSING("Moradia"),
  TRANSPORT("Transporte"),
  EDUCATION("Educacao"),
  FUN("Lazer"),
  UNEXPECTED("Imprevistos"),
  OTHERS("Outros");

  private final String type;

  @Nullable
  public static ExpenseType getType(final String type) {
    return Arrays.stream(values())
        .filter(expenseType -> expenseType.getType().equalsIgnoreCase(type))
        .findFirst()
        .orElse(null);
  }

}
