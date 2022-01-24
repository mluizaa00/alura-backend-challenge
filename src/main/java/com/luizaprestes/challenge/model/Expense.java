package com.luizaprestes.challenge.model;

import java.io.Serializable;
import java.util.UUID;
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
  private UUID id;

  private String description;
  private long value;

  private long date;

}
