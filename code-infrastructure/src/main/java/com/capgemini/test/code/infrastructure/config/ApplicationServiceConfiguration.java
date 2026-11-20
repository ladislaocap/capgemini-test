package com.capgemini.test.code.infrastructure.config;

import com.capgemini.test.code.application.port.out.DniValidator;
import com.capgemini.test.code.application.port.out.EmailNotifier;
import com.capgemini.test.code.application.port.out.SmsNotifier;
import com.capgemini.test.code.application.port.out.UserRepository;
import com.capgemini.test.code.application.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationServiceConfiguration {
  @Bean
  UserService userService(
      UserRepository userRepository,
      DniValidator dniValidator,
      EmailNotifier emailNotifier,
      SmsNotifier smsNotifier) {
    return new UserService(userRepository, dniValidator, emailNotifier, smsNotifier);
  }

}
