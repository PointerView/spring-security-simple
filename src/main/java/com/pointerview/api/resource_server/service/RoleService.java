package com.pointerview.api.resource_server.service;

import com.pointerview.api.resource_server.persistence.entity.security.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findDefaultRole();
}
