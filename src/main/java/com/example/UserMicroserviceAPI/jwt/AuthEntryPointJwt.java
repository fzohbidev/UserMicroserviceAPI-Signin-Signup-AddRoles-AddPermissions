package com.example.UserMicroserviceAPI.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//CUSTOM HANDLING OF UNAUTHORIZED REQUEST , THIS CLASS IS INVOKED WHEN AUTHENTICATION IS REQUIRED
//implement this AuthenticationEntryPoint interface to custom handling the
//component managed by spring boot
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger (AuthEntryPointJwt.class) ;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error: {}",authException.getMessage());
        System.out.println(authException);
        //setting up the response
        response.setContentType (MediaType.APPLICATION_JSON_VALUE);
        response.setStatus (HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put ( "status" , HttpServletResponse.SC_UNAUTHORIZED);
        body.put ("error", "Unauthorized");
        body.put ("message " , authException.getMessage());
        body.put ( "path" , request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        //the body is mapped into JSON format
        mapper.writeValue(response.getOutputStream(), body); ;
    }
}