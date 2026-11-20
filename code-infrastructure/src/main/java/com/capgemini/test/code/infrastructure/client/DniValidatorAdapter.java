package com.capgemini.test.code.infrastructure.client;

import com.capgemini.test.code.application.exception.DniRejectedException;
import com.capgemini.test.code.application.exception.ExternalServiceException;
import com.capgemini.test.code.application.port.out.DniValidator;
import com.capgemini.test.code.infrastructure.client.dto.CheckDniRequest;
import feign.FeignException;
import org.springframework.stereotype.Component;

@Component
public class DniValidatorAdapter implements DniValidator {
  private final DniClient dniClient;

  public DniValidatorAdapter(DniClient dniClient) {
    this.dniClient = dniClient;
  }

  @Override
  public void validate(String dni) {
    try {
      dniClient.check(new CheckDniRequest(dni));
    } catch (FeignException.Conflict exception) {
      throw new DniRejectedException();
    } catch (FeignException exception) {
      throw new ExternalServiceException("DNI validation", exception);
    }
  }
}
