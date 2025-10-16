package com.danihc.cursos.api.spring_security.services.auth;

import com.danihc.cursos.api.spring_security.dto.RegisteredUser;
import com.danihc.cursos.api.spring_security.dto.SaveUser;
import com.danihc.cursos.api.spring_security.dto.auth.AuthenticationRequest;
import com.danihc.cursos.api.spring_security.dto.auth.AuthenticationResponse;
import com.danihc.cursos.api.spring_security.exceptions.ObjectNotFoundException;
import com.danihc.cursos.api.spring_security.persistence.entities.security.User;
import com.danihc.cursos.api.spring_security.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public RegisteredUser registerOneCustomer(SaveUser newUser) {
        User user = userService.registerOneCustomer(newUser);

        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        userDto.setJwt(jwt);

        return userDto;
    }

    private Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().getName());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    public AuthenticationResponse login(@Valid AuthenticationRequest authRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), // Principal
                authRequest.getPassword()); // Credentials

        // Retorna Authentication con info del user, si el passsword es incorrecto retorna excepcion
        Authentication userAuthenticated = authenticationManager.authenticate(authentication);

        UserDetails user = (UserDetails) userAuthenticated.getPrincipal();
        String jwt = jwtService.generateToken(user, generateExtraClaims((User) user));
        AuthenticationResponse authRsp = new AuthenticationResponse();
        authRsp.setJwt(jwt);

        return authRsp;
    }

    public boolean validateToken(String jwt) {
        try {
            jwtService.extractUsername(jwt);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    public User findLoggedInUser() {
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getPrincipal().toString();

            return userService.findOneByUsername(username)
                    .orElseThrow(() -> new ObjectNotFoundException("Username not found. Username: " + username));
    }
}
