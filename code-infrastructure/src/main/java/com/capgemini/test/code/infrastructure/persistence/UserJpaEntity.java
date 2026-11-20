package com.capgemini.test.code.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserJpaEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "room_id", nullable = false)
  private long roomId;

  @Column(name = "name", nullable = false, length = 100)
  private String name;

  @Column(name = "email", nullable = false, length = 254, unique = true)
  private String email;

  @Column(name = "phone", nullable = false, length = 32)
  private String phone;

  @Column(name = "role", nullable = false, length = 50)
  private String role;

  @Column(name = "dni", nullable = false, length = 32)
  private String dni;

  protected UserJpaEntity() {
  }

  public UserJpaEntity(long roomId, String name, String email, String phone, String role, String dni) {
    this.roomId = roomId;
    this.name = name;
    this.email = email;
    this.phone = phone;
    this.role = role;
    this.dni = dni;
  }

  public Long getId() {
    return id;
  }

  public long getRoomId() {
    return roomId;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getRole() {
    return role;
  }

  public String getDni() {
    return dni;
  }
}
