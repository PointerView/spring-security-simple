package com.pointerview.api.resource_server.config.security.authorization;

import com.pointerview.api.resource_server.exception.ObjectNotFoundException;
import com.pointerview.api.resource_server.persistence.entity.security.GrantedPermission;
import com.pointerview.api.resource_server.persistence.entity.security.Operation;
import com.pointerview.api.resource_server.persistence.entity.security.User;
import com.pointerview.api.resource_server.persistence.repository.security.OperationRepository;
import com.pointerview.api.resource_server.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserService userService;
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext requestContext) {

        HttpServletRequest request = requestContext.getRequest();

        String url = extractUrl(request);
        String httpMethod = request.getMethod();

        boolean isPublic = isPublic(url, httpMethod);
        if(isPublic){
            return new AuthorizationDecision(true);
        }

        boolean isGranted = isGranted(url, httpMethod, authentication.get());

        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(String url, String httpMethod, Authentication authentication) {

        if(!(authentication instanceof JwtAuthenticationToken)){
            throw new AuthenticationCredentialsNotFoundException("User not logged in");
        }

        List<Operation> operations = obtainOperations(authentication);

        boolean isGranted = operations.stream().anyMatch(getOperationPredicate(url, httpMethod));

        System.out.println("IS GRANTED: " + isGranted);
        return isGranted;
    }

    private static Predicate<Operation> getOperationPredicate(String url, String httpMethod) {
        return operation -> {

            String basePath = operation.getModule().getBasePath();

            Pattern pattern = Pattern.compile(basePath.concat(operation.getPath()));
            Matcher matcher = pattern.matcher(url);

            return matcher.matches() && operation.getHttpMethod().equals(httpMethod);
        };
    }

    private List<Operation> obtainOperations(Authentication authentication) {

        JwtAuthenticationToken authToken = (JwtAuthenticationToken) authentication;

        Jwt jwt = authToken.getToken();
        String username = jwt.getSubject();
        User user = userService.findOneByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found. Username: " + username));

        List<Operation> operations = user.getRole().getPermissions().stream()
                .map(GrantedPermission::getOperation)
                .toList();

        List<String> scopes = extractScopes(jwt);

        if(!scopes.contains("ALL")){
            operations = operations.stream()
                    .filter(operation -> scopes.contains(operation.getName()))
                    .collect(Collectors.toList());
        }

        return operations;
    }

    private List<String> extractScopes(Jwt jwt) {

        List<String> scopes = new ArrayList<>();
        try{
            scopes = (List<String>) jwt.getClaims().get("scope");

        }catch (Exception e) {
            System.err.println("Hubo un problema al extraer los scopes del cliente");
        }

        return scopes;
    }

    private boolean isPublic(String url, String httpMethod) {

        List<Operation> publicAccessEndpoints = operationRepository
                .findByPubliccAcces();

        boolean isPublic = publicAccessEndpoints.stream().anyMatch(getOperationPredicate(url, httpMethod));


        System.out.println("IS PUBLIC: " + isPublic);

        return isPublic;
    }

    private String extractUrl(HttpServletRequest request) {

        String contextPath = request.getContextPath();
        String url = request.getRequestURI();
        url = url.replace(contextPath, "");
        System.out.println(url);

        return url;
    }
}
