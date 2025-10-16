package com.danihc.cursos.api.spring_security.services;

import com.danihc.cursos.api.spring_security.dto.SaveUser;
import com.danihc.cursos.api.spring_security.persistence.entities.security.User;

import java.util.Optional;

public interface UserService {
    User registerOneCustomer(SaveUser newUser);

    Optional<User> findOneByUsername(String username);
}
