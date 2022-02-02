package com.luizaprestes.challenge.model.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public final class ErrorDto implements Serializable {

  private final String field;
  private final String message;

  private final long time;

}
