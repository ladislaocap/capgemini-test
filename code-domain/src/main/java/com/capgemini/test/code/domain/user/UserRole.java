package com.capgemini.test.code.domain.user;

public enum UserRole {
  ADMIN("admin"),
  SUPERADMIN("superadmin");

  private final String value;

  UserRole(String value) {
    this.value = value;
  }

  public String value() {
    return value;
  }

  public static UserRole from(String value) {
    for (UserRole role : values()) {
      if (role.value.equals(value)) {
        return role;
      }
    }
    throw new InvalidUserDataException("rol");
  }
}
