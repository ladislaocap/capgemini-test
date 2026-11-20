package com.capgemini.test.code.infrastructure.web;

import com.capgemini.test.code.application.port.in.CreateUserUseCase;
import com.capgemini.test.code.application.port.in.GetUserUseCase;
import com.capgemini.test.code.domain.user.User;
import com.capgemini.test.code.infrastructure.web.dto.CreateUserRequest;
import com.capgemini.test.code.infrastructure.web.dto.UserResponse;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms/1/users")
public class UserController {
  private final CreateUserUseCase createUser;
  private final GetUserUseCase getUser;

  public UserController(CreateUserUseCase createUser, GetUserUseCase getUser) {
    this.createUser = createUser;
    this.getUser = getUser;
  }

  @PostMapping
  public ResponseEntity<CreatedUserResponse> create(@Valid @RequestBody CreateUserRequest request) {
    User created = createUser.create(request.toCommand());
    URI location = URI.create("/api/v1/rooms/1/users/" + created.id());
    return ResponseEntity.created(location).body(new CreatedUserResponse(created.id()));
  }

  @GetMapping("/{id}")
  public UserResponse getById(@PathVariable long id) {
    return UserResponse.from(getUser.getById(id));
  }

  public record CreatedUserResponse(Long id) {
  }
}
