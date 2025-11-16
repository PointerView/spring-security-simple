package com.danihc.cursos.api.spring_security.controllers;

import com.danihc.cursos.api.spring_security.dto.LogoutResponse;
import com.danihc.cursos.api.spring_security.dto.auth.AuthenticationRequest;
import com.danihc.cursos.api.spring_security.dto.auth.AuthenticationResponse;
import com.danihc.cursos.api.spring_security.persistence.entities.security.User;
import com.danihc.cursos.api.spring_security.services.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;


    // Dice si el token es valido o no
    @GetMapping("/validate-token")
    @PreAuthorize("permitAll")
    public ResponseEntity<Boolean> validate(@RequestParam String jwt){
        boolean isTokenValid = authenticationService.validateToken(jwt);
        return ResponseEntity.ok(isTokenValid);
    }

    // == LOGIN
    @PostMapping("/authenticate")
    @PreAuthorize("permitAll")
    //@CrossOrigin
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest authenticationRequest){

        AuthenticationResponse rsp = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(rsp);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest req){
        authenticationService.logout(req);
        return ResponseEntity.ok(new LogoutResponse("Logout exitoso"));
    }

    // Retorna la info del perfil en base a un token
    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('READ_MY_PROFILE')")
    public ResponseEntity<User> findMyProfile(){
        User user = authenticationService.findLoggedInUser();
        return ResponseEntity.ok(user);
    }
}
