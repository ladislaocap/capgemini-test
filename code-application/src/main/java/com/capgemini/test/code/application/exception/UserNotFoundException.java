package com.capgemini.test.code.application.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(long userId) {
    super("User " + userId + " was not found in room 1");
  }
}
