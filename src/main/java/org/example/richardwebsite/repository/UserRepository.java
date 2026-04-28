package org.example.richardwebsite.repository;

import org.example.richardwebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCaseAndRoleIgnoreCase(
            String username,
            String role,
            Pageable pageable);

    Page<User> findByRoleIgnoreCase(String role, Pageable pageable);
}