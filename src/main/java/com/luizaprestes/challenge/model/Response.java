package com.luizaprestes.challenge.model;

import java.util.List;
import lombok.Data;

@Data
public final class Response<T> {

  private T data;
  private List<String> errors;

}
