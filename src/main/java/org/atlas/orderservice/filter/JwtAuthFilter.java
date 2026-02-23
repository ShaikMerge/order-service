package org.atlas.orderservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.atlas.orderservice.util.JwtAuthUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter  extends OncePerRequestFilter {

    private final JwtAuthUtil  jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer")) {
            String token = authHeader.substring(7);

            if(jwtUtil.validateJwtToken(token) ) {
                String email = jwtUtil.extractEmail(token);
                Long userId = jwtUtil.extractUserId(token);
                String role = jwtUtil.extractRole(token);
                String fullName = jwtUtil.extractUserName(token);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                Collections.singletonList(new SimpleGrantedAuthority(role))
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("userId", userId);
                request.setAttribute("email", email);
                request.setAttribute("role", role);
                request.setAttribute("fullName" , fullName);

                log.debug("JWT validated for user: {} ({})", email, userId);
            }

        }
        filterChain.doFilter(request, response);

    }
}
