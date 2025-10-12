package com.danihc.cursos.api.spring_security.services;

import com.danihc.cursos.api.spring_security.dto.SaveUser;
import com.danihc.cursos.api.spring_security.persistence.entities.User;

public interface UserService {
    User registerOneCustomer(SaveUser newUser);
}
