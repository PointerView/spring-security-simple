package com.danihc.cursos.api.spring_security.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain filterChain = http.csrf(AbstractHttpConfigurer::disable) // 1
                .sessionManagement(sessMagConfig ->
                        sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthProvider) // 2
                .authorizeHttpRequests(authReqConfig -> {
                  authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
                  authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
                  authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();
                  authReqConfig.anyRequest().authenticated();
                })
                .build();
        return filterChain;
    }
}

/*
* 1.- http.csrf(AbstractHttpConfigurer::disable)
* Desactiva el metodo de seguridad de cross no se que mas ya que funciona por detras con tokens y como
* ya estamos trabajando con tokens es mejor desactibarlo.
*
* 2.- authenticationProvider(daoAuthProvider)
* El metodo recibe como argumento un objeto de tipo AuthenticationProvider que es el metodo que contiene
* la estrategia de autenticacion, en este caso el objeto es una implementacion de la interfaz AuthenticationProvider
* que es DaoAuthenticationProvider que contiene el UserDetailService y la implementacion del PasswordEncoder, el
* primero seria usado por el AuthenticationManager con su metodo authenticate para obtener el UserDetails mediante
* el username, y el PasswordEncoder para conparar el password del UserDetails encodificado por el PasswordEncoder
* con el password retornado por el repository que ya deberia de estar encirptado.
* Antes se hacia esto con un filtro pero la idea de spring es delegar esto al sistema y no programarlo nosotros
* mismos.
*
*
 */
