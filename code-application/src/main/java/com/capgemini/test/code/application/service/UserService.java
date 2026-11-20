package com.capgemini.test.code.application.service;

import com.capgemini.test.code.application.command.CreateUserCommand;
import com.capgemini.test.code.application.exception.DniRejectedException;
import com.capgemini.test.code.application.exception.UserAlreadyExistsException;
import com.capgemini.test.code.application.exception.UserNotFoundException;
import com.capgemini.test.code.application.port.in.CreateUserUseCase;
import com.capgemini.test.code.application.port.in.GetUserUseCase;
import com.capgemini.test.code.application.port.out.DniValidator;
import com.capgemini.test.code.application.port.out.EmailNotifier;
import com.capgemini.test.code.application.port.out.SmsNotifier;
import com.capgemini.test.code.application.port.out.UserRepository;
import com.capgemini.test.code.domain.user.User;
import com.capgemini.test.code.domain.user.UserRole;

public class UserService implements CreateUserUseCase, GetUserUseCase {
  private static final long ROOM_ID = 1L;
  private static final String SAVED_MESSAGE = "usuario guardado";

  private final UserRepository userRepository;
  private final DniValidator dniValidator;
  private final EmailNotifier emailNotifier;
  private final SmsNotifier smsNotifier;

  public UserService(
      UserRepository userRepository,
      DniValidator dniValidator,
      EmailNotifier emailNotifier,
      SmsNotifier smsNotifier) {
    this.userRepository = userRepository;
    this.dniValidator = dniValidator;
    this.emailNotifier = emailNotifier;
    this.smsNotifier = smsNotifier;
  }

  @Override
  public User create(CreateUserCommand command) {
    User candidate = User.register(
        ROOM_ID, command.name(), command.email(), command.phone(), command.role(), command.dni());

    if (userRepository.existsByEmail(candidate.email())) {
      throw new UserAlreadyExistsException();
    }

    dniValidator.validate(candidate.dni());
    User saved = userRepository.save(candidate);

    if (saved.role() == UserRole.ADMIN) {
      emailNotifier.send(saved.email(), SAVED_MESSAGE);
    } else {
      smsNotifier.send(saved.phone(), SAVED_MESSAGE);
    }

    return saved;
  }

  @Override
  public User getById(long userId) {
    return userRepository.findByIdAndRoomId(userId, ROOM_ID)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }
}
