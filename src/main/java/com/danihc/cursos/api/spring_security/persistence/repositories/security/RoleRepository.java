package com.danihc.cursos.api.spring_security.persistence.repositories.security;

import com.danihc.cursos.api.spring_security.persistence.entities.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String role);
}
