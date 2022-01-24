package com.luizaprestes.challenge.model;

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
@Entity(name = "challenge_income")
@NoArgsConstructor
@AllArgsConstructor
public final class Income implements Serializable {

  @Id
  private UUID id;

  @NotBlank
  private String description;
  @Min(1)
  private long value;

  @NotBlank
  private String date;
  private long dateValue;

}
