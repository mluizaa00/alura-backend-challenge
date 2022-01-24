package com.luizaprestes.challenge.model.persistent;

import com.luizaprestes.challenge.model.type.ExpenseType;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
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

  private String description;
  private long value;

  private String date;
  private long dateValue;

}
