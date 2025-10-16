package com.danihc.cursos.api.spring_security.services.impl;

import com.danihc.cursos.api.spring_security.persistence.entities.security.Role;
import com.danihc.cursos.api.spring_security.persistence.repositories.security.RoleRepository;
import com.danihc.cursos.api.spring_security.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Value("${security.default.role}")
    String defaultRole;

    @Override
    public Optional<Role> findDefaultRole() {
        return roleRepository.findByName(defaultRole);
    }
}
