package com.capgemini.test.code.application.port.in;

import com.capgemini.test.code.application.command.CreateUserCommand;
import com.capgemini.test.code.domain.user.User;

public interface CreateUserUseCase {
  User create(CreateUserCommand command);
}
