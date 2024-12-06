package com.example.demo.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationVerficationFilter  extends BasicAuthenticationFilter {

    public JWTAuthenticationVerficationFilter (AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            // Missing or invalid token
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            res.getWriter().write("Missing or invalid Authorization header.");
            return; // Stop filter chain
        }

        try {
            // Authenticate the request
            UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
            if (authentication == null) {
                // Invalid token
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                res.getWriter().write("Invalid or expired token.");
                return; // Stop filter chain
            }

            // Set authentication in the context if valid
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JWTVerificationException e) {
            // Token verification failed
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            res.getWriter().write("Invalid or expired token.");
            return; // Stop filter chain
        }

        // Continue with the filter chain if the token is valid
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req) {
        String token = req.getHeader(SecurityConstants.HEADER_STRING);

        if (token != null) {
            try {
                // Parse and validate the JWT token
                String user = JWT.require(HMAC512(SecurityConstants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                        .getSubject();

                if (user != null) {
                    // Return an authenticated token
                    return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                }
            } catch (JWTVerificationException e) {
                // Token is invalid
                throw e;
            }
        }
        return null; // No valid user found
    }
}
