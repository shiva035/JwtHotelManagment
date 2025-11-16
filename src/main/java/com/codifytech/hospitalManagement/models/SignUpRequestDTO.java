package com.codifytech.hospitalManagement.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpRequestDTO {
    private String username;
    private String password;
    private Set<String> roles;
}
