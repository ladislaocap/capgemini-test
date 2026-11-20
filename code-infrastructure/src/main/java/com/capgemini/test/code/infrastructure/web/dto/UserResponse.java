package com.capgemini.test.code.infrastructure.web.dto;

import com.capgemini.test.code.domain.user.User;

public record UserResponse(Long id, String name, String email, String phone, String rol, String dni) {
  public static UserResponse from(User user) {
    return new UserResponse(
        user.id(), user.name(), user.email(), user.phone(), user.role().value(), user.dni());
  }
}
