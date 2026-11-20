package com.capgemini.test.code.infrastructure.web.dto;

import com.capgemini.test.code.application.command.CreateUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank @Size(max = 6) String name,
    @NotBlank @Email @Size(max = 254) String email,
    @NotBlank @Size(max = 32) String phone,
    @NotBlank String rol,
    @NotBlank @Size(max = 32) String dni) {

  public CreateUserCommand toCommand() {
    return new CreateUserCommand(name, email, phone, rol, dni);
  }
}
