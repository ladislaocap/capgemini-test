package com.capgemini.test.code.domain.user;

public class InvalidUserDataException extends RuntimeException {
  private final String field;

  public InvalidUserDataException(String field) {
    super("error validation " + field);
    this.field = field;
  }

  public String field() {
    return field;
  }
}
