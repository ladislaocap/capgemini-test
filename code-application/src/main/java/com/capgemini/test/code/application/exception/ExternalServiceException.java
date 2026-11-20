package com.capgemini.test.code.application.exception;

public class ExternalServiceException extends RuntimeException {
  public ExternalServiceException(String service, Throwable cause) {
    super(service + " service is unavailable", cause);
  }
}
