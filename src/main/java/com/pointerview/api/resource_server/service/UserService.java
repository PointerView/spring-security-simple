package com.pointerview.api.resource_server.service;

import com.pointerview.api.resource_server.dto.SaveUser;
import com.pointerview.api.resource_server.persistence.entity.security.User;

import java.util.Optional;

public interface UserService {
    User registrOneCustomer(SaveUser newUser);

    Optional<User> findOneByUsername(String username);
}
