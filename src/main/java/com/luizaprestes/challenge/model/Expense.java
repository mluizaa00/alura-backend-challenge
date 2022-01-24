package com.luizaprestes.challenge.model;

import com.luizaprestes.challenge.model.type.ExpenseType;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity(name = "challenge_expense")
@NoArgsConstructor
@AllArgsConstructor
public final class Expense implements Serializable {

  @Id
  private long id;

  private ExpenseType type;

  @NotBlank
  private String description;
  @Min(1)
  private long value;

  @NotBlank
  private String date;
  private long dateValue;

}
