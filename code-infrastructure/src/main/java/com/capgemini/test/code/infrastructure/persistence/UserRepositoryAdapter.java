package com.capgemini.test.code.infrastructure.persistence;

import com.capgemini.test.code.application.port.out.UserRepository;
import com.capgemini.test.code.domain.user.User;
import com.capgemini.test.code.domain.user.UserRole;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserRepositoryAdapter implements UserRepository {
  private final SpringDataUserRepository repository;

  public UserRepositoryAdapter(SpringDataUserRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByEmail(String email) {
    return repository.existsByEmail(email);
  }

  @Override
  public User save(User user) {
    UserJpaEntity entity = new UserJpaEntity(
        user.roomId(), user.name(), user.email(), user.phone(), user.role().value(), user.dni());
    return toDomain(repository.saveAndFlush(entity));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findByIdAndRoomId(long userId, long roomId) {
    return repository.findByIdAndRoomId(userId, roomId).map(this::toDomain);
  }

  private User toDomain(UserJpaEntity entity) {
    return new User(
        entity.getId(),
        entity.getRoomId(),
        entity.getName(),
        entity.getEmail(),
        entity.getPhone(),
        UserRole.from(entity.getRole()),
        entity.getDni());
  }
}
