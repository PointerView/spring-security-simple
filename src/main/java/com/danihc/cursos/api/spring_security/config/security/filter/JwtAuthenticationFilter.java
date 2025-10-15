package com.danihc.cursos.api.spring_security.config.security.filter;

import com.danihc.cursos.api.spring_security.exceptions.ObjectNotFoundException;
import com.danihc.cursos.api.spring_security.persistence.entities.User;
import com.danihc.cursos.api.spring_security.services.UserService;
import com.danihc.cursos.api.spring_security.services.auth.AuthenticationService;
import com.danihc.cursos.api.spring_security.services.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("ENTRO EN EL FILTRO JWT AUTHENTICATION FILTER");
        // 1.- Obtener encabezado http llamado Authorization
        String authorizationHeader = request.getHeader("Authorization");
        if(!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // 2.- Obtener token JWT desde el encabezado
        String jwt = authorizationHeader.split(" ")[1];

        // 3.- Obtener el subject/username desde el token
        // esta accion a su vez valida el formato del token, firma y fecha de expiracion
        String username = jwtService.extractUsername(jwt);
        // 4.- Setear objeto Authentication dentro del security context holder
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                username, null, user.getAuthorities()
        );
        authToken.setDetails(new WebAuthenticationDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        // 5.- Ejecutar el resto de filtros
        filterChain.doFilter(request, response);
    }
}
