package com.capgemini.test.code.domain.user;

import java.util.Objects;
import java.util.regex.Pattern;

public record User(Long id, long roomId, String name, String email, String phone, UserRole role, String dni) {
  private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

  public User {
    if (roomId <= 0) {
      throw new IllegalArgumentException("Room id must be positive");
    }
    if (id != null && id <= 0) {
      throw new IllegalArgumentException("User id must be positive");
    }
    name = validateName(name);
    email = validateEmail(email);
    phone = validatePhone(phone);
    role = Objects.requireNonNull(role, "role");
    dni = validateDni(dni);
  }

  public static User register(long roomId, String name, String email, String phone, String role, String dni) {
    return new User(null, roomId, name, email, phone, UserRole.from(role), dni);
  }

  public User withId(Long generatedId) {
    return new User(generatedId, roomId, name, email, phone, role, dni);
  }

  private static String validateName(String value) {
    if (value == null || value.isBlank() || value.trim().length() > 6) {
      throw new InvalidUserDataException("userName");
    }
    return value.trim();
  }

  private static String validateEmail(String value) {
    if (value == null || value.length() > 254 || !EMAIL_PATTERN.matcher(value).matches()) {
      throw new InvalidUserDataException("email");
    }
    return value;
  }

  private static String validatePhone(String value) {
    if (value == null || value.isBlank() || value.trim().length() > 32) {
      throw new InvalidUserDataException("phone");
    }
    return value.trim();
  }

  private static String validateDni(String value) {
    if (value == null || value.isBlank() || value.trim().length() > 32) {
      throw new InvalidUserDataException("dni");
    }
    return value.trim();
  }
}
