package com.specificgroup.user.repos;

import com.specificgroup.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(final long id);

    Optional<User> findByEmail(final String email);
}
