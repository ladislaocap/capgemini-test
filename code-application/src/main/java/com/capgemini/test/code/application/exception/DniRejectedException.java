package com.capgemini.test.code.application.exception;

public class DniRejectedException extends RuntimeException {
  public DniRejectedException() {
    super("error validation dni");
  }
}
