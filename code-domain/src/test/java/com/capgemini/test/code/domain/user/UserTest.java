package com.capgemini.test.code.domain.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
  @Test
  void registersUserWithSupportedRole() {
    User user = User.register(1, "pablo", "email@email.com", "677998899", "admin", "23454234W");

    assertEquals(UserRole.ADMIN, user.role());
    assertEquals("pablo", user.name());
  }

  @Test
  void rejectsNameLongerThanSixCharacters() {
    InvalidUserDataException exception = assertThrows(
        InvalidUserDataException.class,
        () -> User.register(1, "longname", "email@email.com", "677998899", "admin", "23454234W"));

    assertEquals("userName", exception.field());
  }

  @Test
  void rejectsUnsupportedRole() {
    InvalidUserDataException exception = assertThrows(
        InvalidUserDataException.class,
        () -> User.register(1, "pablo", "email@email.com", "677998899", "user", "23454234W"));

    assertEquals("rol", exception.field());
  }
}
