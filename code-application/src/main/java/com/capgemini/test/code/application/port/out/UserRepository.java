package com.capgemini.test.code.application.port.out;

import com.capgemini.test.code.domain.user.User;
import java.util.Optional;

public interface UserRepository {
  boolean existsByEmail(String email);

  User save(User user);

  Optional<User> findByIdAndRoomId(long userId, long roomId);
}
