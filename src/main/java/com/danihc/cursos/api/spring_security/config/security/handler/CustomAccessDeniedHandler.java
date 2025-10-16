package com.danihc.cursos.api.spring_security.config.security.handler;

import com.danihc.cursos.api.spring_security.dto.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    //@Autowired
    //private AccessDeniedHandler accessDeniedHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Llamar a una configuracion de manejo de excepcion para generalizarla en varias excepciones
        //accessDeniedHandler.handle(request, response, new AccessDeniedException(""));

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(accessDeniedException.getLocalizedMessage());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage("Acceso denegado. No tienes los permisos necesarios para acceder a esta función." +
                "Por favor, contacta al administrador si crees que esto es un error.");
        apiError.setTimestamp(LocalDateTime.now());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());

        ObjectMapper objMapper = new ObjectMapper();
        objMapper.registerModule(new JavaTimeModule());

        String apiErrorAsJson = objMapper.writeValueAsString(apiError);

        response.getWriter().write(apiErrorAsJson);
    }
}
