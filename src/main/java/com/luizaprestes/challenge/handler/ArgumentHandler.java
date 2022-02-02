package com.luizaprestes.challenge.handler;

import com.luizaprestes.challenge.model.dto.ErrorDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public final class ArgumentHandler {

  @Autowired
  private MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public List<ErrorDto> handle(final MethodArgumentNotValidException exception) {
    final List<ErrorDto> list = new ArrayList<>(0);

    final List<FieldError> fields = exception.getBindingResult().getFieldErrors();
    fields.forEach(error -> {
      final String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
      list.add(new ErrorDto(error.getField(), message, System.currentTimeMillis()));
    });

    return list;
  }

}
