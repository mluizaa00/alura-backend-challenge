package com.luizaprestes.challenge.exception;

public final class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
  }

  public UserNotFoundException(final String message) {
    super(message);
  }

  public UserNotFoundException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
