package com.danihc.cursos.api.spring_security.config.security;

import com.danihc.cursos.api.spring_security.config.security.filter.JwtAuthenticationFilter;
import com.danihc.cursos.api.spring_security.config.security.handler.CustomAutenticationEntryPoint;
import com.danihc.cursos.api.spring_security.persistence.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity // Habilita la seguridad web basada en Http Servlet
// @EnableMethodSecurity(prePostEnabled = true) // Habilita la configuración de seguridad mediante anotaciones
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SecurityFilterChain filterChain = http.csrf(AbstractHttpConfigurer::disable) // 1
                .sessionManagement(sessMagConfig ->
                        sessMagConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //.authenticationProvider(daoAuthProvider) // 2
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(HttpSecurityConfig::buildRequestMatchers)
                .exceptionHandling(exceptionConfig -> {
                    exceptionConfig.authenticationEntryPoint(authenticationEntryPoint);
                })
                .build();
        return filterChain;
    }

    // Toda la gestion de authorization gestionado mediante handler HTTP
    private static void buildRequestMatchers(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        // Autorizacion de endpoints de products
        authReqConfig.requestMatchers(HttpMethod.GET, "/products")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(RegexRequestMatcher.regexMatcher(HttpMethod.GET, "/product/[0-9]*"))
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/products")
                .hasRole(Role.ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/products/{productId}/disabled")
                .hasRole(Role.ADMINISTRATOR.name());

        // Autorizacion de endpoints de categories
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.GET, "/categories/{categoryId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.POST, "/categories")
                .hasRole(Role.ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.PUT, "/categories/{categoryId}/disabled")
                .hasRole(Role.ADMINISTRATOR.name());

        // Autorizacion de endpoints de perfil
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/profile")
                .hasAnyRole(Role.ADMINISTRATOR.name(), Role.ASSISTANT_ADMINISTRATOR.name(),
                        Role.CUSTOMER.name());

        // Autorizacion de endpoints publicos
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        authReqConfig.anyRequest().authenticated();
    }

    // Gestion de restricciones mediante anotaciones AOP
    private static void buildRequestMatchersV2(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
        // Autorizacion de endpoints publicos
        authReqConfig.requestMatchers(HttpMethod.POST, "/customers").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST, "/auth/authenticate").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET, "/auth/validate-token").permitAll();

        authReqConfig.anyRequest().authenticated();
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
* 3.- addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
* El metodo recibe dos argumentos, uno es el filter a agregar y el otro es otro filtro el cual se usara como
* punto de ejecucion del primero, siempre se ejecutara el primer filtro antes del indicado en el segundo argumento.
* Esto es muy util para tener control sobre el orden de ejecucion de los filtros.
* En este caso se usa un filtro que se ejecuta siempre antes del UsernamePasswordAuthenticationFilter para poder
* validar el token y agregarlo al SecurityContext y esto no quiere decir que UsernamePasswordAuthenticationFilter
* este activo, solo se obtiene su posición en la cadena como referencia para indicar la posicion de ejecucion
* del nustro filtro personalizado pero no importa si esta o no activo el UsernamePasswordAuthenticationFilter ya que
* en configuraciones STATELESS muchos filters estan inactivos pero se pueden obtener su orden de ejecucion. Poner
* la ejecucion antes de UsernamePasswordAuthenticationFilter es porque ahi es donde spring pondria sus filtros
* de autenticacion tradicionales
*
*/
