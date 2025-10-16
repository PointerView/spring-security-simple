package com.danihc.cursos.api.spring_security.services;

import com.danihc.cursos.api.spring_security.persistence.entities.security.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();
}
