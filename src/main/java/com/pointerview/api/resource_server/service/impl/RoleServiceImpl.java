package com.pointerview.api.resource_server.service.impl;

import com.pointerview.api.resource_server.persistence.entity.security.Role;
import com.pointerview.api.resource_server.persistence.repository.security.RoleRepository;
import com.pointerview.api.resource_server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Value("${security.default.role}")
    private String defaultRole;

    @Override
    public Optional<Role> findDefaultRole() {
        return roleRepository.findByName(defaultRole);
    }
}
