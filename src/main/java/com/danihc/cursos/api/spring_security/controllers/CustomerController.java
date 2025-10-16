package com.danihc.cursos.api.spring_security.controllers;

import com.danihc.cursos.api.spring_security.dto.RegisteredUser;
import com.danihc.cursos.api.spring_security.dto.SaveUser;
import com.danihc.cursos.api.spring_security.persistence.entities.security.User;
import com.danihc.cursos.api.spring_security.services.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    @PreAuthorize("permitAll")
    public ResponseEntity<RegisteredUser> registerOne(@RequestBody @Valid SaveUser newUser){
        RegisteredUser registeredUser = authenticationService.registerOneCustomer(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @GetMapping
    @PreAuthorize("denyAll")
    public ResponseEntity<List<User>> findAll(){
        return ResponseEntity.ok(List.of());
    }
}
