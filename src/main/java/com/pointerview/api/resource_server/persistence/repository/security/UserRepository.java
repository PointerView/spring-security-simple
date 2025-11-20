package com.pointerview.api.resource_server.persistence.repository.security;

import com.pointerview.api.resource_server.persistence.entity.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
