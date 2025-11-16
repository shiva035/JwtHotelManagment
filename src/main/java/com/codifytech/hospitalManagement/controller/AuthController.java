package com.codifytech.hospitalManagement.controller;

import com.codifytech.hospitalManagement.entity.RefreshToken;
import com.codifytech.hospitalManagement.models.*;
import com.codifytech.hospitalManagement.security.RefreshTokenService;
import com.codifytech.hospitalManagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.codifytech.hospitalManagement.common.MyApplicationConstant.ACCESS_TOKEN;
import static com.codifytech.hospitalManagement.common.MyApplicationConstant.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping()
    public ResponseEntity<String> pageName(){
        return ResponseEntity.ok("Welcome to login service");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO requestDTO){
        log.info("Login Request received {}", requestDTO);
        return ResponseEntity.ok(authService.login(requestDTO));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO requestDTO){
        log.info("Sign Up Request received {}", requestDTO);
        return ResponseEntity.ok(authService.signUp(requestDTO));
    }

    // Refresh token endpoint
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequestDTO refreshRequest) {
        String requestRefreshToken = refreshRequest.getRefreshToken();
        return ResponseEntity.ok(authService.getRefresh_token(requestRefreshToken));
    }
}
