package com.codifytech.hospitalManagement.service;

import com.codifytech.hospitalManagement.entity.RefreshToken;
import com.codifytech.hospitalManagement.models.LoginRequestDTO;
import com.codifytech.hospitalManagement.models.LoginResponseDTO;
import com.codifytech.hospitalManagement.entity.User;
import com.codifytech.hospitalManagement.models.SignUpRequestDTO;
import com.codifytech.hospitalManagement.models.SignUpResponseDTO;
import com.codifytech.hospitalManagement.repository.UserRepository;
import com.codifytech.hospitalManagement.security.JwtUtils;
import com.codifytech.hospitalManagement.security.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.codifytech.hospitalManagement.common.MyApplicationConstant.ACCESS_TOKEN;
import static com.codifytech.hospitalManagement.common.MyApplicationConstant.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    public LoginResponseDTO login(LoginRequestDTO requestDTO){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        User user = (User) authenticate.getPrincipal();
        return getLoginResponseDTO(user);
    }

    private LoginResponseDTO getLoginResponseDTO(User user) {
        String token = jwtUtils.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        Map<String, String> tokens = new LinkedHashMap<>();
        tokens.put("accessToken", token);
        tokens.put("RefreshToken", refreshToken.getToken());
        return new LoginResponseDTO(tokens, user.getId());
    }

    public LoginResponseDTO getRefresh_token(String requestRefreshToken) {
        try {
           return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(RefreshToken::getUser)
                    .map(user -> {
                        // Revoke old refresh token and create new one (rotation for security)
                        refreshTokenService.deleteByUserId(user.getId());
                        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

                        // Authenticate using UsernamePasswordAuthenticationToken
                        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
                        Authentication authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        return (User) authentication.getPrincipal();
                    }).map(this::getLoginResponseDTO)
                    .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        }catch (Exception ex){
            throw new RuntimeException("Invalid refresh token");
        }
    }
    public SignUpResponseDTO signUp(SignUpRequestDTO requestDTO){
        User user = userRepository.findByUsername(requestDTO.getUsername()).orElse(null);
        if(user != null) throw new IllegalArgumentException("User already exist");
        user =  userRepository.save(User
                .builder()
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .roles(requestDTO.getRoles())
                .build());
        return SignUpResponseDTO.builder().userId(user.getId()).username(user.getUsername()).build();
    }
}
