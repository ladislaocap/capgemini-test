package com.capgemini.test.code.application.exception;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException() {
    super("error validation email");
  }
}
