package com.capgemini.test.code.application.port.in;

import com.capgemini.test.code.domain.user.User;

public interface GetUserUseCase {
  User getById(long userId);
}
