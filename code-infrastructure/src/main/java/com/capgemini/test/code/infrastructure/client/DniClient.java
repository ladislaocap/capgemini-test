package com.capgemini.test.code.infrastructure.client;

import com.capgemini.test.code.infrastructure.client.dto.CheckDniRequest;
import com.capgemini.test.code.infrastructure.client.dto.CheckDniResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "checkDniClient", url = "${external.service.url}")
public interface DniClient {

  @PatchMapping("/check-dni")
  ResponseEntity<CheckDniResponse> check(@RequestBody CheckDniRequest request);
}
