package com.danihc.cursos.api.spring_security.services.impl;

import com.danihc.cursos.api.spring_security.dto.SaveUser;
import com.danihc.cursos.api.spring_security.exceptions.InvalidPasswordException;
import com.danihc.cursos.api.spring_security.exceptions.ObjectNotFoundException;
import com.danihc.cursos.api.spring_security.persistence.entities.security.Role;
import com.danihc.cursos.api.spring_security.persistence.entities.security.User;
import com.danihc.cursos.api.spring_security.persistence.repositories.security.UserRepository;
import com.danihc.cursos.api.spring_security.services.RoleService;
import com.danihc.cursos.api.spring_security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public User registerOneCustomer(SaveUser newUser) {
        validatePassword(newUser);

        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setName(newUser.getName());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));

        Role defaultRole = roleService.findDefaultRole()
                .orElseThrow(() -> new ObjectNotFoundException("Role not found. Default Role"));

        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private void validatePassword(SaveUser newUser) {
        if(!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }

        if(!newUser.getPassword().equals(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
    }
}
