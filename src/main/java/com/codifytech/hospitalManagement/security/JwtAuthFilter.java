package com.codifytech.hospitalManagement.security;

import com.codifytech.hospitalManagement.entity.User;
import com.codifytech.hospitalManagement.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try {
           log.info("In-coming request received {} :", request.getRequestURI());
           String authorizationToken = request.getHeader("Authorization");
           if (authorizationToken == null || !authorizationToken.startsWith("Bearer")) {
               filterChain.doFilter(request, response);
               return;
           }
           String jwtToken = authorizationToken.split("Bearer ")[1];
           String usernameFromToken = jwtUtils.getUsernameFromToken(jwtToken);
           //role extract
           List<String> rolesFromToken = jwtUtils.getRolesFromToken(jwtToken);
           List<GrantedAuthority> authorities = rolesFromToken.stream()
                   .map(SimpleGrantedAuthority::new)
                   .collect(Collectors.toList());
           if(usernameFromToken != null){
               Optional<User> user = userRepository.findByUsername(usernameFromToken);
               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,"", authorities); // old before role: user.get().getAuthorities()
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
           filterChain.doFilter(request,response);
       }catch (Exception exception){
           handlerExceptionResolver.resolveException(request, response, null, exception);
       }

    }
}
