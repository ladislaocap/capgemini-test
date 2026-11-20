package com.capgemini.test.code.application.service;

import com.capgemini.test.code.application.command.CreateUserCommand;
import com.capgemini.test.code.application.exception.DniRejectedException;
import com.capgemini.test.code.application.exception.UserAlreadyExistsException;
import com.capgemini.test.code.application.port.out.DniValidator;
import com.capgemini.test.code.application.port.out.EmailNotifier;
import com.capgemini.test.code.application.port.out.SmsNotifier;
import com.capgemini.test.code.application.port.out.UserRepository;
import com.capgemini.test.code.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock private UserRepository userRepository;
  @Mock private DniValidator dniValidator;
  @Mock private EmailNotifier emailNotifier;
  @Mock private SmsNotifier smsNotifier;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserService(userRepository, dniValidator, emailNotifier, smsNotifier);
  }

  @Test
  void savesAdminAndSendsEmailNotification() {
    CreateUserCommand command = command("admin", "user@email.com");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> ((User) invocation.getArgument(0)).withId(42L));

    User created = userService.create(command);

    assertEquals(42L, created.id());
    verify(dniValidator).validate(command.dni());
    verify(emailNotifier).send(command.email(), "usuario guardado");
    verify(smsNotifier, never()).send(any(), any());
  }

  @Test
  void sendsSmsNotificationToSuperadmin() {
    CreateUserCommand command = command("superadmin", "super@email.com");
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> ((User) invocation.getArgument(0)).withId(43L));

    userService.create(command);

    verify(smsNotifier).send(command.phone(), "usuario guardado");
    verify(emailNotifier, never()).send(any(), any());
  }

  @Test
  void rejectsDuplicateEmailBeforeCallingExternalServices() {
    CreateUserCommand command = command("admin", "duplicate@email.com");
    when(userRepository.existsByEmail(command.email())).thenReturn(true);

    assertThrows(UserAlreadyExistsException.class, () -> userService.create(command));

    verify(dniValidator, never()).validate(any());
    verify(userRepository, never()).save(any());
  }

  @Test
  void doesNotPersistOrNotifyWhenDniIsRejected() {
    CreateUserCommand command = command("admin", "user@email.com");
    org.mockito.Mockito.doThrow(new DniRejectedException()).when(dniValidator).validate(command.dni());

    assertThrows(DniRejectedException.class, () -> userService.create(command));

    verify(userRepository, never()).save(any());
    verify(emailNotifier, never()).send(any(), any());
    verify(smsNotifier, never()).send(any(), any());
  }

  @Test
  void retrievesUserWithinRoomOne() {
    User user = User.register(1, "pablo", "user@email.com", "677998899", "admin", "23454234W").withId(7L);
    when(userRepository.findByIdAndRoomId(7L, 1L)).thenReturn(Optional.of(user));

    assertEquals(user, userService.getById(7L));
  }

  private CreateUserCommand command(String role, String email) {
    return new CreateUserCommand("pablo", email, "677998899", role, "23454234W");
  }
}
