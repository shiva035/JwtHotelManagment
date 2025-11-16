package com.codifytech.hospitalManagement.security;

import com.codifytech.hospitalManagement.common.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    public static final String ADMIN = "ADMIN";
    public static final String PATIENT = "PATIENT";

    private final PasswordEncoder passwordEncoder;

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->
                        auth.requestMatchers("/public/**", "/auth/**").permitAll()
                                .requestMatchers("/doctors/**").hasAnyRole(RoleType.ADMIN.name(), RoleType.DOCTOR.name())
                                .requestMatchers("/patients/**").hasRole(RoleType.USER.name())
                            .anyRequest().authenticated()
                ).addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);
//                .formLogin(Customizer.withDefaults());
        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetails(){
//        UserDetails user1 = User.builder()
//                .username("Admin")
//                .password(passwordEncoder.encode("pass"))
//                .roles(ADMIN)
//                .build();
//
//        UserDetails user2 = User.builder()
//                .username("Patient")
//                .password(passwordEncoder.encode("pass"))
//                .roles(PATIENT)
//                .build();
//    return new InMemoryUserDetailsManager(Arrays.asList(user1,user2));
//    }
}
